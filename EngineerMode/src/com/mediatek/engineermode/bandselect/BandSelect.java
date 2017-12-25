/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein
 * is confidential and proprietary to MediaTek Inc. and/or its licensors.
 * Without the prior written permission of MediaTek inc. and/or its licensors,
 * any reproduction, modification, use or disclosure of MediaTek Software,
 * and information contained herein, in whole or in part, shall be strictly prohibited.
 */
/* MediaTek Inc. (C) 2010. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER ON
 * AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NONINFRINGEMENT.
 * NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH RESPECT TO THE
 * SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY, INCORPORATED IN, OR
 * SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES TO LOOK ONLY TO SUCH
 * THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO. RECEIVER EXPRESSLY ACKNOWLEDGES
 * THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES
 * CONTAINED IN MEDIATEK SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK
 * SOFTWARE RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S ENTIRE AND
 * CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE RELEASED HEREUNDER WILL BE,
 * AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE MEDIATEK SOFTWARE AT ISSUE,
 * OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE CHARGE PAID BY RECEIVER TO
 * MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek Software")
 * have been modified by MediaTek Inc. All revisions are subject to any receiver's
 * applicable license agreements with MediaTek Inc.
 */

package com.mediatek.engineermode.bandselect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.telephony.RadioAccessFamily;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.mediatek.engineermode.FeatureSupport;
import com.mediatek.engineermode.R;

import java.util.ArrayList;

/**
 * @author mtk54044 //add declartion
 *
 */
public class BandSelect extends Activity implements OnClickListener {
    private static final String LOG_TAG = "BandModeSim1";
    private static final int WCDMA = 0x04;
    private static final int TDSCDMA = 0x08;
    private static final String GSM_BASEBAND = "gsm.baseband.capability";
    private static final String PREF_FILE = "band_select_";
    private static final String PREF_KEYS[] = {"gsm", "umts", "lte_fdd", "lte_tdd", "cdma"};

    private static final int INDEX_GSM_BAND = 0;
    private static final int INDEX_UMTS_BAND = 1;
    private static final int INDEX_LTE_FDD_BAND = 2;
    private static final int INDEX_LTE_TDD_BAND = 3;
    private static final int INDEX_CDMA_BAND = 4;
    private static final int INDEX_BAND_MAX = 5;

    private Phone mPhone = null;
    private Phone mCdmaPhone = null;

    private final ArrayList<BandModeMap> mModeArray = new ArrayList<BandModeMap>();
    private final ArrayList<BandModeMap> mCdmaModeArray = new ArrayList<BandModeMap>();

    private Button mBtnSet;
    private Button mBtnReset;

    private boolean mIsThisAlive = false;
    private int mSimType;
    private boolean mSettingCdma = false;
    private boolean mSettingLte = false;

    private final Handler mResponseHander = new Handler() {

        public void handleMessage(final Message msg) {
            if (!mIsThisAlive) {
                return;
            }
            AsyncResult asyncResult;
            switch (msg.what) {
            case BandModeContent.EVENT_QUERY_SUPPORTED:
                asyncResult = (AsyncResult) msg.obj;
                if (asyncResult.exception == null) {
                    setMode(asyncResult, BandModeContent.EVENT_QUERY_SUPPORTED);
                } else {
                    showDialog(BandModeContent.EVENT_QUERY_SUPPORTED);
                    setSupportedMode(new long[INDEX_BAND_MAX]);
                }
                break;
            case BandModeContent.EVENT_QUERY_CURRENT:
                asyncResult = (AsyncResult) msg.obj;
                if (asyncResult.exception == null) {
                    setMode(asyncResult, BandModeContent.EVENT_QUERY_CURRENT);
                } else {
                    removeDialog(BandModeContent.EVENT_QUERY_SUPPORTED);
                    showDialog(BandModeContent.EVENT_QUERY_CURRENT);
                    setCurrentMode(new long[INDEX_BAND_MAX]);
                }
                break;
            case BandModeContent.EVENT_SET:
                asyncResult = (AsyncResult) msg.obj;
                mSettingLte = false;
                if (asyncResult.exception == null) {
                    if (!mSettingCdma && !mSettingLte) {
                        showDialog(BandModeContent.EVENT_SET_OK);
                    }
                } else {
                    showDialog(BandModeContent.EVENT_SET_FAIL);
                }
                break;
            case BandModeContent.EVENT_QUERY_CURRENT_CDMA:
                asyncResult = (AsyncResult) msg.obj;
                if (asyncResult.exception == null) {
                    setModeCdma(asyncResult, BandModeContent.EVENT_QUERY_CURRENT_CDMA);
                } else {
                    removeDialog(BandModeContent.EVENT_QUERY_SUPPORTED);
                    showDialog(BandModeContent.EVENT_QUERY_CURRENT);
                    setCurrentModeCdma(0);
                }
                break;
            case BandModeContent.EVENT_SET_CDMA:
                asyncResult = (AsyncResult) msg.obj;
                mSettingCdma = false;
                if (asyncResult.exception == null) {
                    if (!mSettingCdma && !mSettingLte) {
                        showDialog(BandModeContent.EVENT_SET_OK);
                    }
                } else {
                    showDialog(BandModeContent.EVENT_SET_FAIL);
                }
                break;
            default:
                break;
            }
        }
    };

    private static int getModemType(int simType) {
        int mode;
        final int mask = 1;
        if (1 == mask) {
            mode = WCDMA;
        } else if (2 == mask) {
            mode = TDSCDMA;
        } else {
            mode = 0;
        }
        return mode;
    }

    private static class BandModeMap {
        public CheckBox mChkBox;
        public int mIndex;
        public int mBit;

        /**
         * @param chkbox
         *            the CheckBox from the layout
         * @param index
         *            the integer of the modem value (index)
         * @param bit
         *            the integer of the modem value (bit offset)
         */
        BandModeMap(final CheckBox chkbox, final int index, final int bit) {
            mChkBox = chkbox;
            mIndex = index;
            mBit = bit;
        }
    }

    private ArrayList<CheckBox> addCheckboxToTable(int tableResId, int arrayResId) {
        TableLayout table = (TableLayout) findViewById(tableResId);
        String[] labels = getResources().getStringArray(arrayResId);
        ArrayList<CheckBox> ret = new ArrayList<CheckBox>(labels.length);

        for (int i = 0; i < labels.length; i++) {
            TableRow row = new TableRow(this);
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(labels[i]);
            row.addView(checkBox);
            table.addView(row);
            ret.add(checkBox);
        }

        return ret;
    }

    private void initLteArray() {
        findViewById(R.id.TableLayout02).setVisibility(View.VISIBLE);
        ArrayList<CheckBox> checkBox =
                addCheckboxToTable(R.id.TableLayout02, R.array.band_mode_lte_fdd);
        for (int i = 0; i < checkBox.size(); i++) {
            mModeArray.add(new BandModeMap(checkBox.get(i), INDEX_LTE_FDD_BAND, i));
        }
        checkBox = addCheckboxToTable(R.id.TableLayout02, R.array.band_mode_lte_tdd);
        for (int i = 0; i < checkBox.size(); i++) {
            mModeArray.add(new BandModeMap(checkBox.get(i), INDEX_LTE_TDD_BAND, i));
        }
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mSimType = intent.getIntExtra("mSimType", PhoneConstants.SIM_ID_1);
        int modemType = getModemType(mSimType);
        ITelephony iTelephony =
                ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
        // For L version, use gsm.baseband.capability to detect TD/FD, use SIM switch API to
        // detect 2G/3G
        if (mSimType == PhoneConstants.SIM_ID_1) {

            setContentView(R.layout.bandselect);
            mBtnSet = (Button) findViewById(R.id.BandSel_Btn_Set);
            mBtnReset = (Button) findViewById(R.id.BandSel_Btn_Reset);
            mModeArray.add(new BandModeMap(
                    (CheckBox) findViewById(R.id.BandSel_GSM_EGSM900),
                    INDEX_GSM_BAND, BandModeContent.GSM_EGSM900_BIT));
            mModeArray.add(new BandModeMap(
                    (CheckBox) findViewById(R.id.BandSel_GSM_DCS1800),
                    INDEX_GSM_BAND, BandModeContent.GSM_DCS1800_BIT));
            mModeArray.add(new BandModeMap(
                    (CheckBox) findViewById(R.id.BandSel_GSM_PCS1900),
                    INDEX_GSM_BAND, BandModeContent.GSM_PCS1900_BIT));
            mModeArray.add(new BandModeMap(
                    (CheckBox) findViewById(R.id.BandSel_GSM_GSM850),
                    INDEX_GSM_BAND, BandModeContent.GSM_GSM850_BIT));
            mModeArray.add(new BandModeMap(
                    (CheckBox) findViewById(R.id.BandSel_UMTS_BAND_I),
                    INDEX_UMTS_BAND, BandModeContent.UMTS_BAND_I_BIT));
            mModeArray.add(new BandModeMap(
                    (CheckBox) findViewById(R.id.BandSel_UMTS_BAND_II),
                    INDEX_UMTS_BAND, BandModeContent.UMTS_BAND_II_BIT));
            mModeArray.add(new BandModeMap(
                    (CheckBox) findViewById(R.id.BandSel_UMTS_BAND_III),
                    INDEX_UMTS_BAND, BandModeContent.UMTS_BAND_III_BIT));
            mModeArray.add(new BandModeMap(
                    (CheckBox) findViewById(R.id.BandSel_UMTS_BAND_IV),
                    INDEX_UMTS_BAND, BandModeContent.UMTS_BAND_IV_BIT));
            mModeArray.add(new BandModeMap(
                    (CheckBox) findViewById(R.id.BandSel_UMTS_BAND_V),
                    INDEX_UMTS_BAND, BandModeContent.UMTS_BAND_V_BIT));
            mModeArray.add(new BandModeMap(
                    (CheckBox) findViewById(R.id.BandSel_UMTS_BAND_VI),
                    INDEX_UMTS_BAND, BandModeContent.UMTS_BAND_VI_BIT));
            mModeArray.add(new BandModeMap(
                    (CheckBox) findViewById(R.id.BandSel_UMTS_BAND_VII),
                    INDEX_UMTS_BAND, BandModeContent.UMTS_BAND_VII_BIT));
            mModeArray.add(new BandModeMap(
                    (CheckBox) findViewById(R.id.BandSel_UMTS_BAND_VIII),
                    INDEX_UMTS_BAND, BandModeContent.UMTS_BAND_VIII_BIT));
            mModeArray.add(new BandModeMap(
                    (CheckBox) findViewById(R.id.BandSel_UMTS_BAND_IX),
                    INDEX_UMTS_BAND, BandModeContent.UMTS_BAND_IX_BIT));
            mModeArray.add(new BandModeMap(
                    (CheckBox) findViewById(R.id.BandSel_UMTS_BAND_X),
                    INDEX_UMTS_BAND, BandModeContent.UMTS_BAND_X_BIT));
            if (FeatureSupport.isSupported(FeatureSupport.FK_LTE_SUPPORT)) {
                initLteArray();
            }
        } else {
            setContentView(R.layout.bandmodesim2);
            mBtnSet = (Button) findViewById(R.id.bandmodesim2_Btn_Set);
            mBtnReset = (Button) findViewById(R.id.bandmodesim2_Btn_Reset);
            mModeArray.add(new BandModeMap(
                    (CheckBox) findViewById(R.id.bandmodesim2_GSM_EGSM900),
                    INDEX_GSM_BAND, BandModeContent.GSM_EGSM900_BIT));
            mModeArray.add(new BandModeMap(
                    (CheckBox) findViewById(R.id.bandmodesim2_GSM_DCS1800),
                    INDEX_GSM_BAND, BandModeContent.GSM_DCS1800_BIT));
            mModeArray.add(new BandModeMap(
                    (CheckBox) findViewById(R.id.bandmodesim2_GSM_PCS1900),
                    INDEX_GSM_BAND, BandModeContent.GSM_PCS1900_BIT));
            mModeArray.add(new BandModeMap(
                    (CheckBox) findViewById(R.id.bandmodesim2_GSM_GSM850),
                    INDEX_GSM_BAND, BandModeContent.GSM_GSM850_BIT));
        }


        mIsThisAlive = true;

        mBtnSet.setOnClickListener(this);
        mBtnReset.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TelephonyManager.getDefault().getPhoneCount() > 1) {
            Log.v("@M_" + LOG_TAG, "Gemini");
            mPhone = PhoneFactory.getPhone(mSimType);
        } else {
            Log.v("@M_" + LOG_TAG, "Single");
            mPhone = PhoneFactory.getDefaultPhone();
        }

            querySupportMode();
            queryCurrentMode();
        
    }

    private void setMode(AsyncResult aSyncResult, int msg) {
        final String[] result = (String[]) aSyncResult.result;

        for (final String value : result) {
            Log.v("@M_" + LOG_TAG, "--.>" + value);
            final String splitString = value.substring(BandModeContent.SAME_COMMAND
                    .length());
            final String[] getDigitalVal = splitString.split(",");

            if (getDigitalVal != null && getDigitalVal.length > 1) {
                long[] values = new long[INDEX_BAND_MAX];
                for (int i = 0; i < values.length; i++) {
                    if (getDigitalVal.length <= i || getDigitalVal[i] == null) {
                        values[i] = 0;
                        continue;
                    }
                    try {
                        values[i] = Long.valueOf(getDigitalVal[i].trim());
                    } catch (NumberFormatException e) {
                        values[i] = 0;
                    }
                }
                if (msg == BandModeContent.EVENT_QUERY_SUPPORTED) {
                    setSupportedMode(values);
                } else {
                    setCurrentMode(values);
                    saveDefaultValueIfNeed(values);
                }
            }
        }
    }

    /**
     * Query Modem supported band modes.
     */
    private void querySupportMode() {
        final String[] modeString = {BandModeContent.QUERY_SUPPORT_COMMAND,
                BandModeContent.SAME_COMMAND};
        Log.v("@M_" + LOG_TAG, "AT String:" + modeString[0]);
        sendATCommand(modeString, BandModeContent.EVENT_QUERY_SUPPORTED);
    }

    /**
     * Query Modem is being used band modes.
     */
    private void queryCurrentMode() {
        final String[] modeString = {BandModeContent.QUERY_CURRENT_COMMAND,
                BandModeContent.SAME_COMMAND};
        Log.v("@M_" + LOG_TAG, "AT String:" + modeString[0]);
        sendATCommand(modeString, BandModeContent.EVENT_QUERY_CURRENT);
    }

    /**
     * Query Modem is being used band modes.
     */
    private void sendATCommand(String[] atCommand, int msg) {
        if (mPhone != null) {
            mPhone.invokeOemRilRequestStrings(atCommand, mResponseHander.obtainMessage(msg));
        }
    }

    /**
     * Set the selected modes.
     *
     * @param values
     *            the integers of mode values
     * @return false means set failed or success
     */
    private void setBandMode(final long[] values) {
        if (values[0] > BandModeContent.GSM_MAX_VALUE
                || values[1] > BandModeContent.UMTS_MAX_VALUE
                || values[2] > BandModeContent.LTE_MAX_VALUE
                || values[3] > BandModeContent.LTE_MAX_VALUE) {
            return;
        }
        // null select is not allowed.
        if (values[0] == 0) {
            values[0] = BandModeContent.GSM_MAX_VALUE;
        }
        if (values[1] == 0) {
            values[1] = BandModeContent.UMTS_MAX_VALUE;
        }
        if (values[2] == 0 && values[3] == 0) {
            values[2] = BandModeContent.LTE_MAX_VALUE;
            values[3] = BandModeContent.LTE_MAX_VALUE;
        }

        String[] modeString = {
                BandModeContent.SET_COMMAND + values[0] + "," + values[1], "" };
        if (FeatureSupport.isSupported(FeatureSupport.FK_LTE_SUPPORT)) {
            modeString[0] += "," + values[2] + "," + values[3];
        }
        Log.v("@M_" + LOG_TAG, "AT String:" + modeString[0]);
        sendATCommand(modeString, BandModeContent.EVENT_SET);
        setCurrentMode(values);
        mSettingLte = true;
    }

    /**
     * Get the selected mode values.
     *
     * @return values from the selected boxes
     */
    private long[] getValFromBox() {
        long[] values = new long[INDEX_BAND_MAX];
        for (final BandModeMap m : mModeArray) {
            if (m.mChkBox.isChecked()) {
                values[m.mIndex] |= 1L << m.mBit;
            }
        }
        return values;
    }

    /**
     * @param values
     *            the integer values from the modem
     */
    private void setCurrentMode(final long[] values) {
        for (final BandModeMap m : mModeArray) {
            if ((values[m.mIndex] & (1L << m.mBit)) == 0) {
                m.mChkBox.setChecked(false);
            } else {
                if (m.mChkBox.isEnabled()) {
                    m.mChkBox.setChecked(true);
                }
            }
        }
    }

    /**
     * @param values
     *            the integer values from the modem
     */
    private void setSupportedMode(final long[] values) {
        for (final BandModeMap m : mModeArray) {
            if ((values[m.mIndex] & (1L << m.mBit)) == 0) {
                m.mChkBox.setEnabled(false);
            } else {
                m.mChkBox.setEnabled(true);
            }
        }
    }

    /**
     * Get default mode values.
     *
     * @return saved default values
     */
    private long[] getDefaultValue() {
        SharedPreferences pref = getSharedPreferences(PREF_FILE + mSimType, MODE_PRIVATE);
        long[] values = new long[INDEX_BAND_MAX];
        for (int i = 0; i < INDEX_BAND_MAX; i++) {
            values[i] = pref.getLong(PREF_KEYS[i], 0);
        }
        return values;
    }

    /**
     * Save default mode values to preference if not exist.
     *
     * @param values
     *            the integer values from the modem
     */
    private void saveDefaultValueIfNeed(long[] values) {
        SharedPreferences pref = getSharedPreferences(PREF_FILE + mSimType, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        for (int i = 0; i < INDEX_BAND_MAX; i++) {
            if (!pref.contains(PREF_KEYS[i])) {
                editor.putLong(PREF_KEYS[i], values[i]);
            }
        }
        editor.commit();
    }

    /**
     * Button on click listener.
     *
     * @param arg0
     *            the view of the button event
     */
    public void onClick(final View arg0) {
        if (arg0.getId() == mBtnSet.getId()) {

                setBandMode(getValFromBox());
            
        } else if (arg0.getId() == mBtnReset.getId()) {
            showDialog(BandModeContent.EVENT_RESET);
        }
    }

    @Override
    protected Dialog onCreateDialog(final int dialogId) {
        if (BandModeContent.EVENT_QUERY_SUPPORTED == dialogId) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    BandSelect.this);
            builder.setTitle(R.string.query_result);
            builder.setMessage(R.string.query_support_message);
            builder.setPositiveButton(R.string.query_btn_text, null);
            builder.create().show();
        } else if (BandModeContent.EVENT_QUERY_CURRENT == dialogId) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    BandSelect.this);
            builder.setTitle(R.string.query_result);
            builder.setMessage(R.string.query_current_message);
            builder.setPositiveButton(R.string.query_btn_text, null);
            builder.create().show();
        } else if (BandModeContent.EVENT_RESET == dialogId) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    BandSelect.this);
            builder.setTitle(R.string.reset_title);
            builder.setMessage(R.string.reset_message);
            builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
  
                            setBandMode(getDefaultValue());
                       
                        dialog.dismiss();
                    }
                }
            );
            builder.setNegativeButton(android.R.string.cancel, null);
            builder.create().show();
        } else if (BandModeContent.EVENT_SET_OK == dialogId) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    BandSelect.this);
            builder.setTitle(R.string.set_success_title);
            builder.setMessage(R.string.set_success_message);
            builder.setPositiveButton(R.string.set_ok_text, null);
            builder.create().show();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    BandSelect.this);
            builder.setTitle(R.string.set_fail_title);
            builder.setMessage(R.string.set_fail_message);
            builder.setPositiveButton(R.string.set_fail_text, null);
            builder.create().show();
        }
        return super.onCreateDialog(dialogId);
    }

    @Override
    public void onDestroy() {
        mIsThisAlive = false;
        super.onDestroy();
    }

    private void initCdmaArray() {
        findViewById(R.id.TableLayout03).setVisibility(View.VISIBLE);
        ArrayList<CheckBox> checkBox = addCheckboxToTable(R.id.TableLayout03,
                R.array.band_mode_cdma);
        for (int i = 0; i < checkBox.size(); i++) {
            mCdmaModeArray.add(new BandModeMap(checkBox.get(i), INDEX_CDMA_BAND, i));
        }
    }

    private void queryCurrentCdmaMode() {
        final String[] modeString = {BandModeContent.QUERY_CURRENT_COMMAND_CDMA,
                BandModeContent.SAME_COMMAND_CDMA};
        Log.v("@M_" + LOG_TAG, "AT String:" + modeString[0]);
        sendATCommandCdma(modeString, BandModeContent.EVENT_QUERY_CURRENT_CDMA);
    }

    private void setModeCdma(AsyncResult aSyncResult, int msg) {
        final String[] result = (String[]) aSyncResult.result;
        for (final String string : result) {
            Log.v("@M_" + LOG_TAG, "--.>" + string);
            String splitString = string.substring(BandModeContent.SAME_COMMAND_CDMA.length());
            if (msg == BandModeContent.EVENT_QUERY_CURRENT_CDMA) {
                long value = 0;
                try {
                    value = Long.valueOf(splitString.trim());
                } catch (NumberFormatException e) {
                    value = 0;
                }
                if (msg == BandModeContent.EVENT_QUERY_SUPPORTED) {
                    setSupportedModeCdma(value);
                } else {
                    setCurrentModeCdma(value);
                    saveDefaultValueIfNeedCdma(value);
                }
            }
        }
    }

    private void sendATCommandCdma(String[] atCommand, int msg) {
        mCdmaPhone.invokeOemRilRequestStrings(atCommand, mResponseHander.obtainMessage(msg));
    }

    private void setBandModeCdma(final long value) {
        String[] modeString = {BandModeContent.SET_COMMAND_CDMA + value, "" };
        Log.v("@M_" + LOG_TAG, "AT String:" + modeString[0]);
        sendATCommandCdma(modeString, BandModeContent.EVENT_SET_CDMA);
        setCurrentModeCdma(value);
        mSettingCdma = true;
    }

    private long getValFromBoxCdma() {
        long value = 0;
        for (final BandModeMap m : mCdmaModeArray) {
            if (m.mChkBox.isChecked()) {
                value |= 1L << m.mBit;
            }
        }
        return value;
    }

    private void setCurrentModeCdma(final long value) {
        for (final BandModeMap m : mCdmaModeArray) {
            if ((value & (1L << m.mBit)) == 0) {
                m.mChkBox.setChecked(false);
            } else {
                if (m.mChkBox.isEnabled()) {
                    m.mChkBox.setChecked(true);
                }
            }
        }
    }

    private void setSupportedModeCdma(final long value) {
        for (final BandModeMap m : mCdmaModeArray) {
            Log.v("@M_" + LOG_TAG, "a " + value + m.mBit);
            if ((value & (1L << m.mBit)) == 0) {
                m.mChkBox.setEnabled(false);
            } else {
                m.mChkBox.setEnabled(true);
            }
        }
    }

    private long getDefaultValueCdma() {
        SharedPreferences pref = getSharedPreferences(PREF_FILE + mSimType, MODE_PRIVATE);
        return pref.getLong(PREF_KEYS[INDEX_CDMA_BAND], 0);
    }

    private void saveDefaultValueIfNeedCdma(long value) {
        SharedPreferences pref = getSharedPreferences(PREF_FILE + mSimType, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (!pref.contains(PREF_KEYS[INDEX_CDMA_BAND])) {
            editor.putLong(PREF_KEYS[INDEX_CDMA_BAND], value);
        }
        editor.commit();
    }
}
