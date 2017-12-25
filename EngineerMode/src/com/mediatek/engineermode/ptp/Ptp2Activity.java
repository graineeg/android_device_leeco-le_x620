package com.mediatek.engineermode.ptp;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.mediatek.engineermode.vcmd.AbstrViewCmdActivity;
import com.mediatek.engineermode.vcmd.CheckBoxCmdAdapter;
import com.mediatek.engineermode.vcmd.TextViewCmdAdapter;
import com.mediatek.engineermode.vcmd.ViewCommand;
import com.mediatek.engineermode.R;

public class Ptp2Activity extends AbstrViewCmdActivity {

    private static final String TAG = "EM_PTP2";
    private static final String PATH_PTP2_SPARK = "/proc/ptp2/ptp2_spark_enable";
    private static final String PATH_PTP2_FBB = "/proc/ptp2/ptp2_fbb_enable";
    private static final String PATH_PTP2_STATUS = "/proc/ptp2/ptp2_dump";
    @Override
    protected List<ViewCommand> initViewCmds() {
        List<ViewCommand> list = new ArrayList<ViewCommand>();
        CheckBoxCmdAdapter cbAdapter = new CheckBoxCmdAdapter();
        list.add(new ViewCommand(R.id.ptp2_enable_spark_cb, PATH_PTP2_SPARK, cbAdapter));
        list.add(new ViewCommand(R.id.ptp2_enable_fbb_cb, PATH_PTP2_FBB, cbAdapter));
        list.add(new ViewCommand(R.id.ptp2_status_content_tv, PATH_PTP2_STATUS, new TextViewCmdAdapter()));
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ptp_ptp2);
        registListeners();
    }

}
