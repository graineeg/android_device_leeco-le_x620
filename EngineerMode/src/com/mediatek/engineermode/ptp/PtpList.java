package com.mediatek.engineermode.ptp;

import java.io.File;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mediatek.engineermode.ChipSupport;
import com.mediatek.engineermode.Elog;
import com.mediatek.engineermode.R;

public class PtpList extends ListActivity {

    private static final String TAG = "PtpList";
    private ArrayList<String> mItemList = null;
    static final int PTP_UNSUPPORTED = -1;
    static final int PTP_UNKNOWN = 0;
    static final int PTP_NORMAL = 1;
    static final int PTP_BIG_LITTLE_CORE = 2;
    static final int PTP_PTP2_SUPPORTED = 8;
    private static final String DIR_PTP = "/proc/ptp";
    private static final String DIR_PTP2 = "/proc/ptp2";
    private static final String DIR_PTP_BIG_LITTLE = "/proc/ptp/PTP_DET_BIG";
    private static int sPtpSupport = PTP_UNKNOWN;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int ptpSupport = getPtpSupport();
        if (ptpSupport == PTP_UNSUPPORTED) {
            Toast.makeText(this, "PTP was not supported", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mItemList = new ArrayList<String>();
        mItemList.add(getString(R.string.ptp_ptp1));
        if ((ptpSupport & PTP_PTP2_SUPPORTED) > 0) {
            mItemList.add(getString(R.string.ptp_ptp2));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mItemList);

        setListAdapter(adapter);

    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        String item = mItemList.get(position);
        if (item.equals(getString(R.string.ptp_ptp1))) {
            startActivity(new Intent(this, Ptp1Activity.class));
        } else if (item.equals(getString(R.string.ptp_ptp2))) {
            startActivity(new Intent(this, Ptp2Activity.class));
        } else {
            Elog.d(TAG, "Unhandle List Item:" + item);
        }
    }

    static int getPtpSupport() {
        if (sPtpSupport != PTP_UNKNOWN) {
            return sPtpSupport;
        }
        int result = PTP_UNSUPPORTED;
        if (ChipSupport.isCurrentChipEquals(ChipSupport.MTK_6592_SUPPORT) ||
                ChipSupport.isCurrentChipEquals(ChipSupport.MTK_6595_SUPPORT)) {
            if (new File(DIR_PTP).exists()) {
                result = 0;
                if (new File(DIR_PTP_BIG_LITTLE).exists()) {
                    result |= PTP_BIG_LITTLE_CORE;
                } else {
                    result |= PTP_NORMAL;
                }
                if (new File(DIR_PTP2).exists()) {
                    result |= PTP_PTP2_SUPPORTED;
                }
            }
        }
        sPtpSupport = result;
        return result;
    }

    public static boolean isPtpSupported() {
        int ptpSupport = getPtpSupport();
        if (ptpSupport == PTP_UNSUPPORTED) {
            return false;
        }
        return true;
    }
}
