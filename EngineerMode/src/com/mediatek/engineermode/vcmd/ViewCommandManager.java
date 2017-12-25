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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.mediatek.engineermode.Elog;

import android.app.Activity;



public class ViewCommandManager {
    private static final String TAG = "ViewCommandManager";
    private Activity mHost;
    private HashMap<Integer, ViewCommand> mViewCmdSet;
    public ViewCommandManager(Activity activity) {
        mHost = activity;
        mViewCmdSet = new HashMap<Integer, ViewCommand>();
    }

    public void initViewCmdFromList(List<ViewCommand> list) {
        for (ViewCommand vc : list) {
            if (vc == null) {
                continue;
            }
            mViewCmdSet.put(vc.rid, vc);
        }
    }

    public void initViewCmdFromArray(ViewCommand[] viewCmds) {
        if (viewCmds == null) {
            return;
        }
        for (int i = 0; i < viewCmds.length; i++) {
            if (viewCmds[i] == null) {
                continue;
            }

            ViewCommand vc = viewCmds[i];
            mViewCmdSet.put(vc.rid, vc);
        }
    }

    public boolean handleViewAction(int rid) {
        ViewCommand vc = mViewCmdSet.get(rid);
        if (vc == null) {
            Elog.d(TAG, "Invalid rid:" + rid);
            return false;
        }
        if (vc.adapter == null) {
            return false;
        }
        return vc.adapter.doViewAction(mHost, vc);
    }

    public boolean handleAllViewAction() {
        Collection<ViewCommand> vcs = mViewCmdSet.values();
        boolean flag = true;
        for (ViewCommand vc : vcs) {
            if (vc.adapter == null) {
                flag = false;
                continue;
            }
            if (!vc.adapter.doViewAction(mHost, vc)) {
                flag = false;
            }
        }
        return flag;
    }

    public boolean setViewByVal(int rid) {
        ViewCommand vc = mViewCmdSet.get(rid);
        if (vc == null) {
            Elog.d(TAG, "Invalid rid:" + rid);
            return false;
        }
        if (vc.adapter == null) {
            return false;
        }
        return vc.adapter.setViewByCmdVal(mHost, vc);
    }

    public boolean setAllViewByVal() {
        Collection<ViewCommand> vcs = mViewCmdSet.values();
        boolean flag = true;
        for (ViewCommand vc : vcs) {
            if (vc.adapter == null) {
                Elog.d(TAG, "adapter is null; vc.cmdPath:" + vc.cmdPath);
                flag = false;
                continue;
            }
            if (!vc.adapter.setViewByCmdVal(mHost, vc)) {
                Elog.d(TAG, "SetViewByCmdVal fail; vc.cmdPath:" + vc.cmdPath);
                flag = false;
            }
        }
        return flag;
    }

    public void clearRes() {
        mViewCmdSet.clear();
    }

    public  Collection<ViewCommand> getViewCommandSet() {
        return mViewCmdSet.values();
    }

}
