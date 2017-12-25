package com.mediatek.engineermode.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;

import com.mediatek.engineermode.Elog;
/**
 * the handler of USB tethering on boot.
 * @author mtk81238
 *
 */
public class UsbTetheringBootHandler implements IBootServiceHandler {
    private static final String TAG = "EM/BootUsbTethering";
    private static final String SP_KEY_USB_TETHERING = "persist.service.usbtethering";
    private static final String USB_TETHERING_ON = "1";
    private static final String USB_TETHERING_OFF = "0";
    private EmBootStartService mStartService = null;
    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_STATE.equals(action)) {
                context.unregisterReceiver(mUsbReceiver);
                boolean usbConnected = intent.getBooleanExtra(UsbManager.USB_CONNECTED, false);
                onGetUsbState(mStartService, usbConnected);
            }
        }
    };

    @Override
    public int handleStartRequest(EmBootStartService service) {
        mStartService = service;
        registUsbReceiver(service);
        return HANDLE_ONGOING;
    }

    private void registUsbReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UsbManager.ACTION_USB_STATE);
        context.registerReceiver(mUsbReceiver, intentFilter);
    }

    private void onGetUsbState(Context context, boolean connected) {
        if (connected) {
            Thread task = new Thread() {
                @Override
                public void run() {
                    super.run();
                    setUsbTethering(mStartService);
                    mStartService.stopStartedService(UsbTetheringBootHandler.this);
                    mStartService = null;
                }
            };
            task.setPriority(Thread.MAX_PRIORITY);
            task.start();
        }
    }

    private void setUsbTethering(Context context) {
        ConnectivityManager connMgr
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager telMgr
                = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String value = SystemProperties.get(SP_KEY_USB_TETHERING);
        if (value != null) {
            value = value.trim();
        }
        if (USB_TETHERING_ON.equals(value)) {
            telMgr.setDataEnabled(false);
            connMgr.setUsbTethering(true);
            SystemClock.sleep(12000);
            telMgr.setDataEnabled(true);
        } else if (USB_TETHERING_OFF.equals(value)) {
            connMgr.setUsbTethering(false);
        } else {
            Elog.d(TAG, "Undesirable USB_TETHERING value:" + value);
        }
    }

    static boolean isSupportBootUsbTethering() {
        String value = SystemProperties.get(SP_KEY_USB_TETHERING);
        if (USB_TETHERING_ON.equals(value) || USB_TETHERING_OFF.equals(value)) {
            return true;
        }
        return false;
    }
}
