package com.mediatek.engineermode.wps;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mediatek.engineermode.R;

import java.util.ArrayList;

/**
 * wifi wps NFC test_bed in engineermode
 * 
 * @author mtk54040
 * 
 */
public class WpsNfcRoleRegistrar extends ListActivity {

    private static final String TAG = "EM/WpsRole";
    private static final String MTK_WPS_NFC_TESTBED_ER_R_PASSWORD_ACTION = "mtk.wps.nfc.testbed.externalRegistrar.r.password";
    private ArrayList<String> mModuleList = null;
    private WifiManager mWifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.wps_role);
        setContentView(R.layout.wps_nfc);

        mModuleList = new ArrayList<String>();
        mModuleList.add(getString(R.string.wps_get_pin));
        mModuleList.add(getString(R.string.wps_write_credential));
        mModuleList.add(getString(R.string.wps_get_ap_pin));
         
        ArrayAdapter<String> moduleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mModuleList);
        setListAdapter(moduleAdapter);
        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // Send broadcast

        if (mModuleList.get(position).equals(getString(R.string.wps_get_pin))) {
            Log.v("@M_" + TAG, "-->onListItemClick wps_get_pin");
            // Intent intent = new Intent();
            // intent.putExtra(getString(R.string.wps_role_registrar),
            // getString(R.string.wps_get_pin));
            // intent.setAction(getString(R.string.wps_nfc));
            // sendBroadcast(intent);

            //mWifiManager.getPinFromNfc(WifiManager.TOKEN_TYPE_NDEF, null);

            Toast.makeText(getApplicationContext(),
                    R.string.wps_send_broadcast, Toast.LENGTH_SHORT).show();
        } else if (mModuleList.get(position).equals(
                getString(R.string.wps_write_credential))) {
            Log.v("@M_" + TAG, "-->onListItemClick wps_write_credential");
            // Intent intent = new Intent();
            // intent.putExtra(getString(R.string.wps_role_registrar),
            // getString(R.string.wps_share_credential));
            // intent.setAction(getString(R.string.wps_nfc));
            // sendBroadcast(intent);

            // add api function
            //mWifiManager.getWpsCredAndConnect(WifiManager.TOKEN_TYPE_NDEF, null);
            /*
             * // sendBroadcast: W_CONFIGURATION_ACTION, config
             * 
             * // assume that we have already get config from WiFiManager.
             * byte[] networkIndex = {0x01}; byte[] ssid = {0x48, 0x6F, 0x6D,
             * 0x65, 0x57, 0x4C, 0x41, 0x4E}; byte[] authenticationType = {0x00,
             * 0x20}; byte[] encryptionType = {0x00, 0x08}; byte[] networkKey =
             * {0x4D, 0x79, 0x50, 0x72, 0x65, 0x53, 0x68, 0x61, 0x72, 0x65,
             * 0x64, 0x4B, 0x65, 0x79}; byte[] macAddress = {(byte) 0xFF, (byte)
             * 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}; byte[]
             * vendorExtension = {0x10, 0x49, 0x00, 0x06, 0x00, 0x37, 0x2A,
             * 0x02, 0x01, 0x01, 0x10, 0x49, 0x00, 0x06, 0x00, 0x37, 0x2A, 0x00,
             * 0x01, 0x20};
             * 
             * ConfigurationToken configurationToken = new ConfigurationToken();
             * configurationToken.setNetworkIndex(networkIndex);
             * configurationToken.setSSID(ssid);
             * configurationToken.setAuthType(authenticationType);
             * configurationToken.setEncrypType(encryptionType);
             * configurationToken.setMacAddress(macAddress);
             * configurationToken.setNetworkKey(networkKey);
             * configurationToken.setVendorExtension(vendorExtension);
             * 
             * // sendBroadcast: W_CONFIGURATION_ACTION, config Intent
             * intentRWCT = new Intent(
             * INfcWpsTestBed.MTK_WPS_NFC_TESTBED_W_CONFIGURATION_ACTION);
             * intentRWCT
             * .putExtra(INfcWpsTestBed.MTK_WPS_NFC_TESTBED_EXTRA_CONFIGURATION,
             * configurationToken); sendBroadcast(intentRWCT);
             */
            Toast.makeText(getApplicationContext(),
                    R.string.wps_send_broadcast, Toast.LENGTH_SHORT).show();
        }else if(mModuleList.get(position).equals(
                getString(R.string.wps_get_ap_pin))) {
             Log.v("@M_" + TAG, "-->onListItemClick wps_get_ap_pin");
             Intent intentHandoverRequest = new Intent(MTK_WPS_NFC_TESTBED_ER_R_PASSWORD_ACTION);
             sendBroadcast(intentHandoverRequest);
             Toast.makeText(getApplicationContext(),
                    R.string.wps_send_broadcast, Toast.LENGTH_SHORT).show();
        }
    }

}
