package com.mediatek.engineermode;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.SystemProperties;
import android.util.Log;


import java.io.File;
import java.io.IOException;

public class UartUsbSwitch extends Activity {

    private static final String TAG = "EM/UartUsbSwitch";
    private static final String FILE_PORT_MODE = "/sys/devices/platform/mt_usb/portmode";
    private static final String FILE_PORT_MODE_NEW = "/sys/devices/platform/musb-mtu3d/musb-hdrc/portmode";
    private static final String FILE_PORT_MODE_2 = "/sys/bus/platform/devices/musb-hdrc/portmode";
    private static final String USB_CONNECT_STATE = "/sys/class/android_usb/android0/state";
    private static final String USB_CONNECT = "CONNECT";
    private static final String USB_CONFIGURED = "CONFIGURED";
    private static final String SUCCESS = " success";
    private static final String FAIL = " fail";
    private static final String MODE_USB = "0";
    private static final String MODE_UART = "1";
    private static final String KEY_USB_PORT = "mediatek.usb.port.mode";
    private static final String VAL_USB = "usb";
    private static final String VAL_UART = "uart";
    private static final int MSG_CHECK_RESULT = 11;
    private static final int DIALOG_USB_WARNING = 0;
    private static final int DIALOG_USB_CONNECT_WARNING = 1;
    private TextView mTvCurrent;
    private RadioGroup mRgMode;
    private WorkerHandler mWorkerHandler = null;
    private HandlerThread mWorkerThread = null;
    private String mModeVal;
    private RadioButton mRbUsb;
    private RadioButton mRbUart;
    private String mPortFile = FILE_PORT_MODE;
    private final RadioGroup.OnCheckedChangeListener mCheckListener = new RadioGroup.OnCheckedChangeListener() {

        public void onCheckedChanged(RadioGroup group, int checkedId) {
            Boolean bModeUsb = null;
            switch (checkedId) {
            case R.id.uart_usb_switch_mode_usb:
                bModeUsb = true;
                break;
            case R.id.uart_usb_switch_mode_uart:
                bModeUsb = false;
                break;
            case -1:
            default:
                break;
            }
            doSwitch(bModeUsb);
            Log.d("@M_" + TAG, "OnCheckedChangeListener.onCheckedChanged() checkId:" + checkedId + " bModeUsb:" + bModeUsb);
        }

    };

    private void showDialog(String title, String msg) {
        AlertDialog dialog = new AlertDialog.Builder(this).setCancelable(
                true).setTitle(title).setMessage(msg).
                setPositiveButton(android.R.string.ok, null).create();
        dialog.show();
    }

    private class WorkerHandler extends Handler {
        WorkerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_CHECK_RESULT:
                final boolean result = waitForState(mModeVal, 2000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateStatus(VAL_USB.equals(mModeVal));
                        enableUsbUartSwitch(true);
                        showDialog(null, getString(R.string.uart_usb_switch_set)
                              + (result ? SUCCESS : FAIL));

                    }
                });

                break;
            default:
                Log.w("@M_" + TAG, "mWorkerHandler Unknown msg: " + msg.what);
                break;
            }
            super.handleMessage(msg);
        }
    }
    private void enableUsbUartSwitch(boolean enabled) {
        mRbUsb.setEnabled(enabled);
        mRbUart.setEnabled(enabled);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ChipSupport.MTK_6595_SUPPORT == ChipSupport.getChip() ||
                ChipSupport.MTK_6795_SUPPORT == ChipSupport.getChip()) {
            mPortFile = FILE_PORT_MODE_2;
        }
        if (!new File(mPortFile).exists()) {
            Toast.makeText(this, R.string.uart_usb_switch_notsupport,
                    Toast.LENGTH_SHORT).show();
            Log.w("@M_" + TAG, "Port mode file not exist");
            finish();
            return;
        }
        setContentView(R.layout.uart_usb_switch);
        mTvCurrent = (TextView) findViewById(R.id.uart_usb_switch_current_mode);
        mRgMode = (RadioGroup) findViewById(R.id.uart_usb_switch_mode);
        mRbUsb = (RadioButton) findViewById(R.id.uart_usb_switch_mode_usb);
        mRbUart = (RadioButton) findViewById(R.id.uart_usb_switch_mode_uart);
        mWorkerThread = new HandlerThread(TAG);
        mWorkerThread.start();
        mWorkerHandler = new WorkerHandler(mWorkerThread.getLooper());
        showDialog(DIALOG_USB_WARNING);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = null;
        if (id == DIALOG_USB_WARNING) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.uart_usb_switch_dialog_title);
            builder.setCancelable(false);
            builder.setMessage(getString(R.string.uart_usb_switch_warning));
            builder.setPositiveButton(R.string.ok, null);
            dialog = builder.create();
        } else if (id == DIALOG_USB_CONNECT_WARNING) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.uart_usb_switch_dialog_title_error);
            builder.setCancelable(false);
            builder.setMessage(getString(R.string.uart_usb_switch_dialog_usb_error));
            builder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            dialog = builder.create();
        }
        return dialog;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String current = getUsbMode();
        Log.v("@M_" + TAG, "Current: " + current);
        if (null == current) {
            Toast.makeText(this, R.string.uart_usb_switch_geterror,
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Boolean mode = null;
        if (current.contains(MODE_USB)) {
            mode = true;
        } else if (current.contains(MODE_UART)) {
            mode = false;
        }
        updateStatus(mode);
        mRgMode.setOnCheckedChangeListener(mCheckListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
    };

    @Override
    protected void onDestroy() {
        if (mWorkerHandler != null) {
            mWorkerHandler.removeMessages(MSG_CHECK_RESULT);
        }
        if (mWorkerThread != null) {
            mWorkerThread.quit();
        }
        super.onDestroy();
    };

    private void doSwitch(Boolean bModeUsb) {
        if (null != bModeUsb) {
            if (bModeUsb.booleanValue()) {
                mModeVal = VAL_USB;
            } else {
                mModeVal = VAL_UART;

                if (isUsbConnected()) {
                    showDialog(DIALOG_USB_CONNECT_WARNING);
                    return;
                }
            }
            enableUsbUartSwitch(false);
            setUsbMode(mModeVal);
            mWorkerHandler.sendEmptyMessage(MSG_CHECK_RESULT);
        }
    }

    private void updateStatus(Boolean bModeUsb) {
        if (null == bModeUsb) {
            mTvCurrent.setText(R.string.uart_usb_switch_unknown);
            mRgMode.check(-1);
        } else if (bModeUsb.booleanValue()) {
            mTvCurrent.setText(R.string.uart_usb_switch_usb);
            mRgMode.check(R.id.uart_usb_switch_mode_usb);
        } else {
            mTvCurrent.setText(R.string.uart_usb_switch_uart);
            mRgMode.check(R.id.uart_usb_switch_mode_uart);
        }
    }
    private boolean isUsbConnected() {
        String result = null;
        boolean isConnected = false;
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("cat ");
        strBuilder.append(USB_CONNECT_STATE);
        Log.v("@M_" + TAG, "isUsbConnected cmd: " + strBuilder.toString());
        try {
            if (ShellExe.RESULT_SUCCESS == ShellExe.execCommand(strBuilder
                    .toString(), true)) {
                result = ShellExe.getOutput();
                if (result.equals(USB_CONFIGURED) || result.equals(USB_CONNECT)) {
                    isConnected = true;
                }
            }
        } catch (IOException e) {
            Log.w("@M_" + TAG, "get current dramc IOException: " + e.getMessage());
        }
        return isConnected;
    }
    private String getUsbMode() {
        String result = null;
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("cat ");
        strBuilder.append(mPortFile);
        Log.v("@M_" + TAG, "get current dramc cmd: " + strBuilder.toString());
        try {
            if (ShellExe.RESULT_SUCCESS == ShellExe.execCommand(strBuilder
                    .toString(), true)) {
                result = ShellExe.getOutput();
            }
        } catch (IOException e) {
            Log.w("@M_" + TAG, "get current dramc IOException: " + e.getMessage());
        }
        return result;
    }

    private void setUsbMode(String value) {
        Log.v("@M_" + TAG, "setUsbMode(), value: " + value);
        SystemProperties.set(KEY_USB_PORT, value);
    }

    private boolean waitForState(String modeVal, int milliSec) {
        int count = milliSec / 50;
        for (int i = 0; i < count; i++) {
            String relValue = SystemProperties.get(KEY_USB_PORT);
            Log.d("@M_" + TAG, "Check value of usb port mode:" + relValue);
            if (modeVal.equals(relValue)) {
                return true;
            }
            SystemClock.sleep(50);
        }
        return false;
    }
}
