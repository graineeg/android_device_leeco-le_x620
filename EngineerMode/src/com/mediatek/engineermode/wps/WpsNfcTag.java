package com.mediatek.engineermode.wps;

import android.app.ListActivity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.CheckBox;
import com.mediatek.engineermode.R;

import java.util.ArrayList;

/**
 * wifi wps NFC test_bed in engineermode
 * 
 * @author mtk54040
 * 
 */
public class WpsNfcTag extends ListActivity implements OnClickListener{

    private static final String TAG = "EM/WpsNfcTag";
    private static final String WPS_NFC_USE_PK = "wps_nfc_pubkey";
    
    private ArrayList<String> mModuleList = null;
    private WifiManager mWifiManager;
    private CheckBox mUsePublicKey;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.wps_write_tag);
        setContentView(R.layout.wps_nfc_role_enrollee);

        mModuleList = new ArrayList<String>();
        mModuleList.add(getString(R.string.wps_pin));
        mModuleList.add(getString(R.string.wps_credential));

        ArrayAdapter<String> moduleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mModuleList);
        setListAdapter(moduleAdapter);

        mUsePublicKey = (CheckBox) findViewById(R.id.checkBox);
        mUsePublicKey.setOnClickListener(this);
        Settings.System
                        .putInt(getContentResolver(), WPS_NFC_USE_PK, 0); // default always 0
                        
        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // Send broadcast

        if (mModuleList.get(position).equals(getString(R.string.wps_pin))) {
            Log.v("@M_" + TAG, "-->onListItemClick wps_pin");

            // Intent intent = new Intent();
            // intent.putExtra(getString(R.string.wps_write_tag),
            // getString(R.string.wps_pin));
            // intent.setAction(getString(R.string.wps_nfc));
            // sendBroadcast(intent);

            // add api function,first listener second para:
            // WifiManager.TOKEN_TYPE_NDEF
            //mWifiManager.writePinToNfc(WifiManager.TOKEN_TYPE_NDEF, null);
            /*
             * // assume that we have already get config from WiFiManager. byte
             * networkIndex = 0x01; String ssid = "HomeWLAN"; Short
             * authenticationType = 0x20; // = {0x00, 0x20}; Short
             * encryptionType = 0x08;// {0x00, 0x08}; String networkKey =
             * "MyPreSharedKey";// {0x4D, 0x79, 0x50, 0x72, // 0x65, 0x53, 0x68,
             * 0x61, // 0x72, 0x65, 0x64, 0x4B, // 0x65, 0x79}; byte[]
             * macAddress = { (byte) 0x11, (byte) 0x22, (byte) 0x33, (byte)
             * 0xAA, (byte) 0xBB, (byte) 0xCC }; byte[] vendorExtension = {
             * 0x10, 0x49, 0x00, 0x06, 0x00, 0x37, 0x2A, 0x00, 0x01, 0x20 };
             * 
             * WpsCredential mWpsCredential = new WpsCredential();
             * mWpsCredential.setNetworkIndex(networkIndex);
             * mWpsCredential.setSSID(ssid);
             * mWpsCredential.setAuthType(authenticationType);
             * mWpsCredential.setEncrypType(encryptionType);
             * mWpsCredential.setMacAddress(macAddress);
             * mWpsCredential.setNetworkKey(networkKey);
             * mWpsCredential.setVendorExtension(vendorExtension);
             * 
             * // sendBroadcast: W_CONFIGURATION_ACTION, config Intent
             * intentHandoverSelector = new Intent(
             * INfcWpsTestBed.MTK_WPS_NFC_TESTBED_HS_ACTION);
             * intentHandoverSelector
             * .putExtra(INfcWpsTestBed.MTK_WPS_NFC_TESTBED_EXTRA_CREDENTIAL,
             * mWpsCredential); sendBroadcast(intentHandoverSelector);
             */
            Toast.makeText(getApplicationContext(),
                    R.string.wps_send_broadcast, Toast.LENGTH_SHORT).show();
        } else if (mModuleList.get(position).equals(
                getString(R.string.wps_credential))) {
            Log.v("@M_" + TAG, "-->onListItemClick wps_credential");

            // Intent intent = new Intent();
            // intent.putExtra(getString(R.string.wps_write_tag),
            // getString(R.string.wps_credential));
            // intent.setAction(getString(R.string.wps_nfc));
            // sendBroadcast(intent);

            // add api function
            //mWifiManager.writeCredToNfc(WifiManager.TOKEN_TYPE_NDEF, null);
            /*
             * // sendBroadcast: W_CONFIGURATION_ACTION, config Intent
             * intentHandoverRequest = new Intent(
             * INfcWpsTestBed.MTK_WPS_NFC_TESTBED_HR_ACTION);
             * //intentRWCT.putExtra
             * (INfcWpsTestBed.MTK_WPS_NFC_TESTBED_EXTRA_CONFIGURATION,
             * configurationToken); sendBroadcast(intentHandoverRequest);
             */
            Toast.makeText(getApplicationContext(),
                    R.string.wps_send_broadcast, Toast.LENGTH_SHORT).show();
        }
    }
    public void onClick(View v) {
        Log.v("@M_" + TAG, "-->onClick checkbox");

        if (v.equals(mUsePublicKey)) {
            if (mUsePublicKey.isChecked()) {
                Settings.System.putInt(getContentResolver(),
                        WPS_NFC_USE_PK, 1);
            } else {
                Settings.System.putInt(getContentResolver(),
                        WPS_NFC_USE_PK, 0);
            }

        } 
    }
}
