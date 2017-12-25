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
public class WpsNfcRoleEnrollee extends ListActivity{

    private static final String TAG = "EM/WpsRole";
    
    private WifiManager mWifiManager;
    private ArrayList<String> mModuleList = null;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.wps_role);
        //setContentView(R.layout.wps_nfc_role_enrollee);
        setContentView(R.layout.wps_nfc);
            
        mModuleList = new ArrayList<String>();
        mModuleList.add(getString(R.string.wps_write_pin));
        mModuleList.add(getString(R.string.wps_get_credential));

        ArrayAdapter<String> moduleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mModuleList);
        setListAdapter(moduleAdapter);
        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // Send broadcast
        // Intent intent = new Intent();

        if (mModuleList.get(position).equals(getString(R.string.wps_write_pin))) {
            Log.v("@M_" + TAG, "-->onListItemClick wps_write_pin");

            // add api function
            //mWifiManager.getWpsPinAndConnect(WifiManager.TOKEN_TYPE_NDEF, null);
            /*
             * // assume that we have already get psw from WiFiManager. int
             * pwdId = 271; byte[] pubKeyHash = { 0x02, 0x45, 0x67, 0x21, 0x23,
             * 0x60, 0x40, (byte) 0x93, (byte) 0x84, (byte) 0xAF, (byte) 0xAD,
             * 0x23, 0x24, (byte) 0x9A, 0x10, 0x3C, (byte) 0xDF, 0x3F, 0x66,
             * 0x41 }; byte[] devPwd = { 0x4C, 0x3B, 0x2B, 0x20, 0x6A, 0x21,
             * 0x2B, 0x2C, 0x56, 0x41, 0x32, 0x51, 0x77, 0x42, 0x2B, 0x20 };
             * byte[] vendorEx = {0x10, 0x49, 0x00, 0x06, 0x00, 0x37, 0x2A,
             * 0x00, 0x01, 0x20};
             * 
             * PasswordToken passwordToken = new PasswordToken();
             * passwordToken.setPwdId(pwdId);
             * passwordToken.setPublicKeyHash(pubKeyHash);
             * passwordToken.setDevPwd(devPwd);
             * passwordToken.setVendorEx(vendorEx);
             * 
             * Log.v("@M_" + TAG, " Send Intent Write PWD token ");
             * 
             * Intent intentEWPT = new Intent(
             * INfcWpsTestBed.MTK_WPS_NFC_TESTBED_W_PASSWORD_ACTION);
             * 
             * 
             * intentEWPT.putExtra(INfcWpsTestBed.MTK_WPS_NFC_TESTBED_EXTRA_PASSWORD
             * , passwordToken); //intentEWPT.putExtra("PwdToken1", (Parcelable)
             * passwordToken);
             * 
             * Log.v("@M_" + TAG, "  Extra String ::  " +
             * INfcWpsTestBed.MTK_WPS_NFC_TESTBED_EXTRA_PASSWORD);
             * sendBroadcast(intentEWPT);
             */
            // Intent intent = new Intent();
            // intent.putExtra(getString(R.string.wps_role_enrollee),
            // getString(R.string.wps_share_pin));
            // intent.setAction(getString(R.string.wps_nfc));
            // sendBroadcast(intent);

            Toast.makeText(getApplicationContext(),
                    R.string.wps_send_broadcast, Toast.LENGTH_SHORT).show();
        } else if (mModuleList.get(position).equals(
                getString(R.string.wps_get_credential))) {
            Log.v("@M_" + TAG, "-->onListItemClick wps_get_credential");
            // Intent intent = new Intent();
            // intent.putExtra(getString(R.string.wps_role_enrollee),
            // getString(R.string.wps_get_credential));
            // intent.setAction(getString(R.string.wps_nfc));
            // sendBroadcast(intent);

            //mWifiManager.getCredFromNfc(null);

            Toast.makeText(getApplicationContext(),
                    R.string.wps_send_broadcast, Toast.LENGTH_SHORT).show();
        }
    }

}
