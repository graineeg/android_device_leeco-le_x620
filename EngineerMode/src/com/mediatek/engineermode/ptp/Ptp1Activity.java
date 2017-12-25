package com.mediatek.engineermode.ptp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.mediatek.engineermode.vcmd.AbstrViewCmdActivity;
import com.mediatek.engineermode.vcmd.CheckBoxCmdAdapter;
import com.mediatek.engineermode.vcmd.ViewCommand;
import com.mediatek.engineermode.R;

public class Ptp1Activity extends AbstrViewCmdActivity {
    private static final String TAG = "EM_PTP1";
    private static final String PATH_PTP1 = "/proc/ptp/ptp_debug";
    private static final String PATH_PTP1_BIG_CORE = "/proc/ptp/PTP_DET_BIG/ptp_debug";
    private static final String PATH_PTP1_LITTLE_CORE = "/proc/ptp/PTP_DET_LITTLE/ptp_debug";
    private static final String PATH_PTP1_GPU = "/proc/ptp/PTP_DET_GPU/ptp_debug";
    private static final String PATH_PTP1_VCORE_AO = "/proc/ptp/PTP_DET_VCORE_AO/ptp_debug";
    private static final String PATH_PTP1_VCORE_PDN = "/proc/ptp/PTP_DET_VCORE_PDN/ptp_debug";

    private int mPtpSupport = 0;
    private boolean mIsBigLittle;

    @Override
    protected List<ViewCommand> initViewCmds() {
        List<ViewCommand> list = new ArrayList<ViewCommand>();
        CheckBoxCmdAdapter cbAdapter = new CheckBoxCmdAdapter();
        if (mIsBigLittle) {
            list.add(new ViewCommand(R.id.ptp1_enable_big_core_cb, PATH_PTP1_BIG_CORE, cbAdapter));
            list.add(new ViewCommand(R.id.ptp1_enable_little_core_cb, PATH_PTP1_LITTLE_CORE, cbAdapter));
            list.add(new ViewCommand(R.id.ptp1_enable_gpu_cb, PATH_PTP1_GPU, cbAdapter));
            list.add(new ViewCommand(R.id.ptp1_enable_vcore_ao_cb, PATH_PTP1_VCORE_AO, cbAdapter));
            list.add(new ViewCommand(R.id.ptp1_enable_vcore_pdn_cb, PATH_PTP1_VCORE_PDN, cbAdapter));
        } else {
            list.add(new ViewCommand(R.id.ptp1_enable_ptp_cb, PATH_PTP1, cbAdapter));
        }
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPtpSupport = PtpList.getPtpSupport();
        mIsBigLittle = (mPtpSupport & PtpList.PTP_BIG_LITTLE_CORE) > 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ptp_ptp1);
        registListeners();
        if (mIsBigLittle) {
            findViewById(R.id.ptp1_setting).setVisibility(View.GONE);
        } else {
            findViewById(R.id.ptp1_setting_ext).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setPtpView(-1);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        super.onCheckedChanged(buttonView, isChecked);
        setPtpView(buttonView.getId());
    }

    private void setPtpView(int id) {
        Collection<ViewCommand> vcmds = getViewCommandManager().getViewCommandSet();
        for (ViewCommand vc : vcmds) {
            if (vc.adapter != null && vc.adapter.getViewType() == CheckBox.class) {
                CheckBox cb = (CheckBox) findViewById(vc.rid);
                if (cb != null && !cb.isChecked() && (id < 0 || id == cb.getId())) {
                    cb.setEnabled(false);
                }
            }
        }
    }

}
