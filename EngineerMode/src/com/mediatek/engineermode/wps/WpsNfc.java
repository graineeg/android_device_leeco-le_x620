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
public class WpsNfc extends ListActivity {

    private static final String TAG = "EM/WpsNfc";

    private ArrayList<String> mModuleList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wps_nfc);


        mModuleList = new ArrayList<String>();
        mModuleList.add(getString(R.string.wps_role));
        mModuleList.add(getString(R.string.wps_write_tag));

        ArrayAdapter<String> moduleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mModuleList);
        setListAdapter(moduleAdapter);

    }



    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (mModuleList.get(position).equals(
                getString(R.string.wps_role))) {
            Log.v("@M_" + TAG, "-->onListItemClick wps_role");
            startActivity(new Intent(WpsNfc.this, WpsNfcRole.class));
        } else if (mModuleList.get(position).equals(
                getString(R.string.wps_write_tag))) {
            Log.v("@M_" + TAG, "-->onListItemClick wps_write_tag");
            startActivity(new Intent(WpsNfc.this, WpsNfcTag.class));
        }
    }



}
