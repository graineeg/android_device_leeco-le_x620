package com.mediatek.engineermode;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemProperties;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * AAL Switch main activity.
 * @author mtk
 */
public class AalSetting extends Activity
        implements android.view.View.OnClickListener {
    private static final String TAG = "AalSettingTAG";
    private static final String SP_KEY_AAL_STATE = "persist.sys.aal.function";
    private static final String AAL_STATE_DISABLED = "0";
    private static final String AAL_STATE_ENABLED = "6";
    private static final String AAL_STATE_NOEXIST = "NOEXIST";

    private ToggleButton mTbtnSwitch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aal_setting);
        mTbtnSwitch = (ToggleButton) findViewById(R.id.aal_state_tbtn);
        String state = getAalState();
        if (AAL_STATE_ENABLED.equals(state)
                || AAL_STATE_NOEXIST.equals(state)) {
            mTbtnSwitch.setText(getString(R.string.aal_on));
            mTbtnSwitch.setChecked(true);
        }
        mTbtnSwitch.setOnClickListener(this);
    }

    private String getAalState() {
        String value = SystemProperties.get(SP_KEY_AAL_STATE);
        String state = value;
        if (value == null || value.length() == 0) {
            return AAL_STATE_NOEXIST;
        }
        state = state.trim();
        return state;
    }

    private int switchAal(boolean enabled) {
        Elog.d(TAG, "switchAal:" + enabled);
        String value = AAL_STATE_DISABLED;
        if (enabled) {
            value = AAL_STATE_ENABLED;
        }
        SystemProperties.set(SP_KEY_AAL_STATE, value);
        if (value.equals(getAalState())) {
            return 0;
        }
        Elog.d(TAG, "fail to set aal system properties");
        return -1;
    }

    @Override
    public void onClick(View view) {
        if (view == mTbtnSwitch) {
            switchAal(mTbtnSwitch.isChecked());
            Toast.makeText(this, R.string.aal_switch_tip, Toast.LENGTH_SHORT).show();
        }
    }
}
