/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein is
 * confidential and proprietary to MediaTek Inc. and/or its licensors. Without
 * the prior written permission of MediaTek inc. and/or its licensors, any
 * reproduction, modification, use or disclosure of MediaTek Software, and
 * information contained herein, in whole or in part, shall be strictly
 * prohibited.
 *
 * MediaTek Inc. (C) 2010. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER
 * ON AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL
 * WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NONINFRINGEMENT. NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH
 * RESPECT TO THE SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY,
 * INCORPORATED IN, OR SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES
 * TO LOOK ONLY TO SUCH THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO.
 * RECEIVER EXPRESSLY ACKNOWLEDGES THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO
 * OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES CONTAINED IN MEDIATEK
 * SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK SOFTWARE
 * RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S
 * ENTIRE AND CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE
 * RELEASED HEREUNDER WILL BE, AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE
 * MEDIATEK SOFTWARE AT ISSUE, OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE
 * CHARGE PAID BY RECEIVER TO MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek
 * Software") have been modified by MediaTek Inc. All revisions are subject to
 * any receiver's applicable license agreements with MediaTek Inc.
 */

package com.mediatek.engineermode.misc;

import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import android.util.Log;
import com.mediatek.engineermode.FeatureSupport;
import com.mediatek.engineermode.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Misc feature config Activity.
 */
public class MiscConfig extends PreferenceActivity {
    private static final String TAG = "EM/MiscConfig";
    private static final String KEY_MISC_CONFIG = "1";
    private static final int INDEX_2G_ONLY_ROAMING = 0;
    private static final int INDEX_SELF_REGISTER = 2;
    private static final int MSG_QUERY = 0;
    private static final int MSG_SET = 1;
    private static final String CMD_QUERY = "AT+ECFGGET=\"sms_over_sgs\"";
    private static final String CMD_SET = "AT+ECFGSET=\"sms_over_sgs\"";
    private static final String VALUE_ENABLE = "1";
    private static final String VALUE_DISABLE = "0";

    private CheckBoxPreference mPreferences[];
    private int mConfig = 0;
    private Toast mToast;
    private Phone mPhone = null;
    private CheckBoxPreference mSmsSgsPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.misc);

        String[] features = getResources().getStringArray(R.array.misc_features);
        mPreferences = new CheckBoxPreference[features.length];
        for (int i = 0; i < features.length; i++) {
            mPreferences[i] = new CheckBoxPreference(this);
            mPreferences[i].setSummary(features[i]);
            mPreferences[i].setPersistent(false);
            if (i == INDEX_SELF_REGISTER &&
                    !FeatureSupport.isSupported(FeatureSupport.FK_CT4GREG_APP)) {
                Log.d("@M_" + TAG, "Not show entry for CT4GREG.");
                continue;
            }
            getPreferenceScreen().addPreference(mPreferences[i]);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConfig = Settings.Global.getInt(getContentResolver(), KEY_MISC_CONFIG, 0);
        Log.d("@M_" + TAG, "Get " + KEY_MISC_CONFIG + " = " + mConfig);
        for (int i = 0; i < mPreferences.length; i++) {
            mPreferences[i].setChecked((mConfig & (1 << i)) != 0);
        }
        querySmsSgsValue();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {
        Log.d("@M_" + TAG, "Enter onPreferenceTreeClick()");
        for (int i = 0; i < mPreferences.length; i++) {
            if (mPreferences[i].equals(preference)) {
                if (mPreferences[i].isChecked()) {
                    mConfig |= (1 << i);
                } else {
                    mConfig &= ~(1 << i);
                }
                Log.d("@M_" + TAG, "Set " + KEY_MISC_CONFIG + " = " + mConfig);
                Settings.Global.putInt(getContentResolver(), KEY_MISC_CONFIG, mConfig);
                return true;
            }
        }
        if (preference == mSmsSgsPreference) {
            setSgsValue(mSmsSgsPreference.isChecked() ? VALUE_ENABLE : VALUE_DISABLE);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void querySmsSgsValue() {
        int subId = SubscriptionManager.getDefaultDataSubId();
        Log.i("@M_" + TAG, "sub id " + subId);
        int phoneId = SubscriptionManager.getPhoneId(subId);
        Log.i("@M_" + TAG, "phone id " + phoneId);
        int phoneCount = TelephonyManager.getDefault().getPhoneCount();
        Log.i("@M_" + TAG, "phone count " + phoneCount);
        mPhone = PhoneFactory.getPhone(phoneId >= 0 && phoneId < phoneCount ? phoneId : 0);

        Message msg = mHandler.obtainMessage(MSG_QUERY);
        if (mPhone != null) {
            mPhone.invokeOemRilRequestStrings(new String[] {CMD_QUERY, "+ECFGGET:"}, msg);
        }
        Log.i("@M_" + TAG, "send " + CMD_QUERY + ", " + "+ECFGGET:");

        mSmsSgsPreference = new CheckBoxPreference(this);
        mSmsSgsPreference.setSummary(getString(R.string.misc_config_sgs));
        mSmsSgsPreference.setPersistent(false);
        getPreferenceScreen().addPreference(mSmsSgsPreference);
    }

    private void setSgsValue(String value) {
        Message msg = mHandler.obtainMessage(MSG_SET);
        if (mPhone != null) {
            mPhone.invokeOemRilRequestStrings(
                new String[] {CMD_SET + ",\"" + value + "\"", ""}, msg);
        }
        Log.i("@M_" + TAG, "send " + CMD_SET + ",\"" + value + "\"");
    }

    private void parseSmsSgsValue(String data) {
        mSmsSgsPreference.setChecked(VALUE_ENABLE.equals(parseCommandResponse(data)));
    }

    private String parseCommandResponse(String data) {
        Log.d("@M_" + TAG, "raw data: " + data);
        Pattern p = Pattern.compile("\\+ECFGGET:\\s*\".*\"\\s*,\\s*\"(.*)\"");
        Matcher m = p.matcher(data);
        while (m.find()) {
            String value = m.group(1);
            Log.d("@M_" + TAG, "value: " + value);
            return value;
        }
        Log.e("@M_" + TAG, "wrong format: " + data);
        showToast("wrong format: " + data);
        return "";
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("@M_" + TAG, "handleMessage() " + msg.what);
            if (msg.what == MSG_QUERY) {
                AsyncResult ar = (AsyncResult) msg.obj;
                if (ar.exception == null && ar.result != null) {
                    if (ar.result instanceof String[]) {
                        String[] data = (String[]) ar.result;
                        if (data.length > 0) {
                            parseSmsSgsValue(data[0]);
                            return;
                        }
                    }
                }
                showToast("Query failed");
            } else if (msg.what == MSG_SET) {
                AsyncResult ar = (AsyncResult) msg.obj;
                if (ar.exception == null) {
                    showToast("Set successful");
                } else {
                    showToast("Set failed");
                }
            }
        }
    };

    private void showToast(String msg) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
