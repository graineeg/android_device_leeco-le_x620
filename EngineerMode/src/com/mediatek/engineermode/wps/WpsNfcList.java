package com.mediatek.engineermode.wps;

import android.app.ListActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import com.mediatek.engineermode.Elog;
import com.mediatek.engineermode.R;
import java.util.ArrayList;

/**
 * wifi wps NFC test_bed in engineermode
 *
 * @author mtk54040
 *
 */
public class WpsNfcList extends ListActivity implements OnClickListener {

    private static final String TAG = "EM/WpsList";
    private static final String WPS_WIFI_ENABLE = "wps_em_wifi_enable";
    private static final String WPS_P2P_ENABLE = "wps_em_p2p_enable";
    private static final int DIALOG_QUERY_WFA = 0;
    private static final int DIALOG_SET_WARNING = 1;
    private static final String ACTION_SET_HANDOVER_MODE = "com.mediatek.nfc.handover.ACTION_SET_HANDOVER_MODE";
    private static final String EXTRA_MODE_VALUE = "com.mediatek.nfc..handover.EXTRA_MODE_VALUE";

    private static final String ACTION_QUERY_HANDOVER_MODE = "com.mediatek.nfc.handover.ACTION_QUERY_HANDOVER_MODE";
    private static final String ACTION_QUERY_HANDOVER_MODE_RESULT = "com.mediatek.nfc.handover.ACTION_QUERY_HANDOVER_MODE_RESULT";

    private CheckBox mWifiEnableChk;
    private CheckBox mP2pEnableChk;
    private CheckBox mWfaEnableChk;
    private ArrayList<String> mModuleList = null;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Elog.v(TAG, "mReceiver onReceive: " + action);
            if (action.equals(ACTION_QUERY_HANDOVER_MODE_RESULT)) {
                int result = intent.getExtras().getInt(EXTRA_MODE_VALUE);
                Elog.v(TAG, "result is: " + result);
                if (result > 0) {
                    mWfaEnableChk.setChecked(true);
                } else {
                    mWfaEnableChk.setChecked(false);
                }
                removeDialog(DIALOG_QUERY_WFA);
            } else {
                Elog.v(TAG, "mReceiver Other response");
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wps_nfc_em);

        mWifiEnableChk = (CheckBox) findViewById(R.id.checkBox1);
        mP2pEnableChk = (CheckBox) findViewById(R.id.checkBox2);
        mWfaEnableChk = (CheckBox) findViewById(R.id.checkBox3);

        mWifiEnableChk.setOnClickListener(this);
        mP2pEnableChk.setOnClickListener(this);
        mWfaEnableChk.setOnClickListener(this);

        mModuleList = new ArrayList<String>();
        mModuleList.add(getString(R.string.wps_nfc));

        ArrayAdapter<String> moduleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mModuleList);
        setListAdapter(moduleAdapter);
        int wifiFlag = 0;
        int p2pFlag = 0;

        wifiFlag = Settings.System.getInt(getContentResolver(),
                WPS_WIFI_ENABLE, -1);
        p2pFlag = Settings.System.getInt(getContentResolver(), WPS_P2P_ENABLE,
                -1);

        Elog.v(TAG, "-->wifiFlag = " + wifiFlag);
        Elog.v(TAG, "-->p2pFlag = " + p2pFlag);

        if (wifiFlag > 0) {
            mWifiEnableChk.setChecked(true);
        }
        if (p2pFlag > 0) {
            mP2pEnableChk.setChecked(true);
        }
        // register query result receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_QUERY_HANDOVER_MODE_RESULT);
        registerReceiver(mReceiver, filter);

        // send query intent
        Intent intentQueryRequest = new Intent(ACTION_QUERY_HANDOVER_MODE);
        sendBroadcast(intentQueryRequest);
        showDialog(DIALOG_QUERY_WFA);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (mModuleList.get(position).equals(getString(R.string.wps_nfc))) {
            Elog.v(TAG, "-->onListItemClick wps_nfc");

            startActivity(new Intent(WpsNfcList.this, WpsNfc.class));
        }
    }
    @Override
    protected void onDestroy() {
        Elog.v(TAG, "onDestroy");
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
    public void onClick(View v) {
        Elog.v(TAG, "-->onClick checkbox");

        if (v.equals(mWifiEnableChk)) {
            if (mWifiEnableChk.isChecked()) {
                Settings.System.putInt(getContentResolver(),
                        WPS_WIFI_ENABLE, 1);
            } else {
                Settings.System.putInt(getContentResolver(),
                        WPS_WIFI_ENABLE, 0);
            }

        } else if (v.equals(mP2pEnableChk)) {

            if (mP2pEnableChk.isChecked()) {
                Settings.System
                        .putInt(getContentResolver(), WPS_P2P_ENABLE, 1);
            } else {
                Settings.System.putInt(getContentResolver(), WPS_P2P_ENABLE, 0);
            }
        } else if (v.equals(mWfaEnableChk)) {
            int result;
            if (mWfaEnableChk.isChecked()) {
                result = 1;
            } else {
                result = 0;
            }
            Intent intentSetRequest = new Intent(ACTION_SET_HANDOVER_MODE);
            intentSetRequest.putExtra(EXTRA_MODE_VALUE, result);
            sendBroadcast(intentSetRequest);
            Elog.v(TAG, "send result :" + result);
            showDialog(DIALOG_SET_WARNING);
        }
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = null;
        switch (id) {
            case DIALOG_QUERY_WFA:
                ProgressDialog innerDialog = new ProgressDialog(this);
                innerDialog.setTitle(R.string.wps_dialog_init);
                innerDialog
                        .setMessage(getString(R.string.wps_dialog_init_message));
                innerDialog.setCancelable(false);
                innerDialog.setIndeterminate(true);
                dialog = innerDialog;
                break;
            case DIALOG_SET_WARNING:
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.wps_dialog_warn);
                builder.setCancelable(false);
                builder.setMessage(getString(R.string.wps_dialog_warn_message));
                builder.setPositiveButton(R.string.wps_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                dialog = builder.create();
                break;
            default:
                break;
        }
        return dialog;
    }
}
