/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein
 * is confidential and proprietary to MediaTek Inc. and/or its licensors.
 * Without the prior written permission of MediaTek inc. and/or its licensors,
 * any reproduction, modification, use or disclosure of MediaTek Software,
 * and information contained herein, in whole or in part, shall be strictly prohibited.
 *
 * MediaTek Inc. (C) 2010. All rights reserved.
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

package com.mediatek.engineermode.video;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemProperties;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mediatek.engineermode.Elog;
import com.mediatek.engineermode.R;

/**
 *  Video main actvity.
 * @author mtk81238
 *
 */
public class VideoActivity extends Activity implements OnItemSelectedListener {

    private static final String TAG = "EM/VideoActivity";
    private static final String SP_KEY_MJC_CUSTOMER = "sys.display.mjc.customer";
    private static final String SP_KEY_MJC_DEMO = "sys.display.mjc.demo";

    private Spinner mSpCustomize = null;
    private Spinner mSpDemo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_entry);
        mSpCustomize = (Spinner) findViewById(R.id.video_customize_spn);
        mSpDemo = (Spinner) findViewById(R.id.video_demo_spn);
        fillSpinnerIntItems(mSpCustomize, new int[] {0, 1});
        fillSpinnerIntItems(mSpDemo, new int[] {0, 1, 2});
        mSpCustomize.setSelection(getIntSystemProp(SP_KEY_MJC_CUSTOMER, 0));
        mSpDemo.setSelection(getIntSystemProp(SP_KEY_MJC_DEMO, 0));
        mSpCustomize.setOnItemSelectedListener(this);
        mSpDemo.setOnItemSelectedListener(this);
    }

    private void fillSpinnerIntItems(Spinner spinner, int[] itemVals) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < itemVals.length; i++) {
            adapter.add(String.valueOf(itemVals[i]));
        }
        spinner.setAdapter(adapter);
    }

    private void setIntSystemProp(String key, int val) {
        String strVal = String.valueOf(val);
        SystemProperties.set(key, strVal);
    }

    private int getIntSystemProp(String key, int defVal) {
        String strVal = SystemProperties.get(key);
        int value = 0;
        try {
            value = Integer.parseInt(strVal);
        } catch (NumberFormatException e) {
            value = defVal;
        }
        return value;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
            long id) {
        if (parent == mSpCustomize) {
            setIntSystemProp(SP_KEY_MJC_CUSTOMER, position);
        } else if (parent == mSpDemo) {
            setIntSystemProp(SP_KEY_MJC_DEMO, position);
        } else {
            Elog.d(TAG, "Unhandled adapterView:" + parent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // just do nothing
    }
}
