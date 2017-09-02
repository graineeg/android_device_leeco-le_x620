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

package com.mediatek.ygps;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


/**
 * Service to support background running.
 *
 */
public class YgpsService extends Service {

    private static final String TAG = "EM/YGPS_Service";
    protected static final String SERVICE_START_ACTION = "com.mediatek.ygps.YgpsService";
    private static final int NTF_ID_YGPS_FORGROUND = 10;
    private boolean mForeground = false;
    private Binder mBinder = new LocalYgpsBinder();

    /**
     * request service as foreground service.
     * @param requester activity class
     */
    public void requestForeground(Class<? extends Activity> requester) {
        startForeground(NTF_ID_YGPS_FORGROUND, buildNotification(requester));
        mForeground = true;
    }

    private Notification buildNotification(Class<? extends Activity> clazz) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert).
        setContentTitle("YGPS run in background").
        setContentText("Tap here enter YGPS");
        Intent intent = new Intent(this, clazz);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pi);
        return builder.build();
    }

    /**
     * dismiss foreground notification.
     */
    public void dismissForeground() {
        stopForeground(true);
        mForeground = false;
    }

    @Override
    public void onCreate() {
        Log.v("@M_" + TAG, "YGPSService onCreate");
        // sSelf = this;
        // mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    /**
     * tell whether service run as foreground service.
     * @return true if service was run as foreground
     */
    public boolean isForeground() {
        return mForeground;
    }

    @Override
    public void onDestroy() {
        Log.v("@M_" + TAG, "YGPSService onDestroy");
        if (mForeground) {
            dismissForeground();
        }
        // mNM.cancel(R.string.mobilelog_service_start);
        // mNM.cancelAll();
        // sSelf = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("@M_" + TAG, "onStartCommand " + intent + " flags " + flags);
        return START_STICKY;
    }

    /**
     * ygps local binder.
     * @author: mtk
     */
    class LocalYgpsBinder extends Binder {
        public YgpsService getServiceInstance() {
            return YgpsService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
