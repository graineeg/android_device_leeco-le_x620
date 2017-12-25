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

package com.mediatek.engineermode.vcmd;

import java.io.IOException;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.mediatek.engineermode.Elog;
import com.mediatek.engineermode.ShellExe;
import com.mediatek.engineermode.vcmd.ViewCommand.Adapter;

public class TextViewCmdAdapter implements Adapter {
    private static final String TAG = "TextViewCmdAdapter";
    @Override
    public boolean doViewAction(Activity host, ViewCommand vc) {
        if (!(vc.adapter instanceof TextViewCmdAdapter)) {
            Elog.d(TAG, "Unsupported ViewCommand.Adapter:" + vc.adapter.toString());
            return false;
        }
        Elog.d(TAG, "Only Do Nothing");
        return true;
    }

    @Override
    public boolean setViewByCmdVal(Activity host, ViewCommand vc) {
        if (!(vc.adapter instanceof TextViewCmdAdapter)) {
            Elog.d(TAG, "Unsupported ViewCommand.Adapter:" + vc.adapter.toString());
            return false;
        }
        TextView tv = (TextView) host.findViewById(vc.rid);
        String cmd = "cat " + vc.cmdPath;
        Elog.d(TAG, "cmd:" + cmd);
        int ret;
        try {
            ret = ShellExe.execCommand(cmd);
        } catch (IOException e) {
            Elog.d(TAG, "IOException:" + e.getMessage());
            return false;
        }
        String val = ShellExe.getOutput();
        if (ShellExe.RESULT_SUCCESS != ret) {
            Elog.d(TAG, "Exec cmd fail:" + ret + " output:" + val);
           return false;
        }
        if (val == null) {
            Elog.d(TAG, "output was null");
            return false;
        }
        tv.setText(val);
        return true;
    }

    @Override
    public Class<? extends View> getViewType() {
        return TextView.class;
    }

}
