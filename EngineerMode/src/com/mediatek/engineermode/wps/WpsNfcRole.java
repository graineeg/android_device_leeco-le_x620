package com.mediatek.engineermode.wps;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mediatek.engineermode.R;

import java.util.ArrayList;


/**
 * wifi wps NFC test_bed in engineermode
 *
 * @author mtk54040
 *
 */
public class WpsNfcRole extends ListActivity {

    private static final String TAG = "EM/WpsRole";

    private ArrayList<String> mModuleList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.wps_role);
        setContentView(R.layout.wps_nfc);

        mModuleList = new ArrayList<String>();
        mModuleList.add(getString(R.string.wps_role_enrollee));
        mModuleList.add(getString(R.string.wps_role_registrar));

        ArrayAdapter<String> moduleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mModuleList);
        setListAdapter(moduleAdapter);

    }



    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (mModuleList.get(position).equals(
                getString(R.string.wps_role_enrollee))) {
            Log.v("@M_" + TAG, "-->onListItemClick wps_role_enrollee");
            startActivity(new Intent(WpsNfcRole.this, WpsNfcRoleEnrollee.class));
        } else if (mModuleList.get(position).equals(
                getString(R.string.wps_role_registrar))) {
            Log.v("@M_" + TAG, "-->onListItemClick wps_role_registrar");
            startActivity(new Intent(WpsNfcRole.this, WpsNfcRoleRegistrar.class));
        }
    }



}
