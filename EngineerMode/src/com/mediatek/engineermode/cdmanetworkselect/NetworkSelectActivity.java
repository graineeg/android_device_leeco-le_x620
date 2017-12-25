package com.mediatek.engineermode.cdmanetworkselect;

import android.app.Activity;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CheckBox;

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.mediatek.engineermode.FeatureSupport;

import com.mediatek.engineermode.R;
/**
 *
 * For setting network mode.
 * @author mtk54043
 *
 */
public class NetworkSelectActivity extends Activity implements OnCheckedChangeListener {
    private static final String TAG = "EM/NetworkMode";
    private static final int EVENT_QUERY_NETWORKMODE_DONE = 101;
    private static final int EVENT_SET_NETWORKMODE_DONE = 102;
    private static final int EVENT_QUERY_EHRPD_ENABLE_DONE = 103;
    private static final int EVENT_SET_EHRPD_ENABLE_DONE = 104;

    private static final int HYBRID_INDEX = 0;
    private static final int CDMA_1X_ONLY_INDEX = 1;
    private static final int EVDO_ONLY_INDEX = 2;
    private static final int HYBRID = 4;
    private static final int CDMA_1X_ONLY  = 5;
    private static final int EVDO_ONLY = 6;

    private boolean mFirstEnter = true;
    private int mCurrentSettingsNetworkMode;

    private Phone mPhone = null;

    private Spinner mPreferredNetworkSpinner = null;
    private CheckBox mDisableeHRPDCheckBox = null;

    private OnItemSelectedListener mPreferredNetworkHandler = new OnItemSelectedListener() {
        public void onItemSelected(AdapterView parent, View v, int pos, long id) {
            Message msg = mHandler.obtainMessage(EVENT_SET_NETWORKMODE_DONE);

            int settingsNetworkMode = Settings.Global.getInt(getContentResolver(),
                    Settings.Global.PREFERRED_NETWORK_MODE, Phone.PREFERRED_NT_MODE);
            mCurrentSettingsNetworkMode = settingsNetworkMode;
            int selectNetworkMode = Phone.PREFERRED_NT_MODE;
            switch (pos) {
            case HYBRID_INDEX: // 4
                selectNetworkMode = HYBRID;
                break;
            case CDMA_1X_ONLY_INDEX: // 5
                selectNetworkMode = CDMA_1X_ONLY;
                break;
            case EVDO_ONLY_INDEX: // 6
                selectNetworkMode = EVDO_ONLY;
                break;
            default:
                break;
            }
            if (mFirstEnter == true) {
                mFirstEnter = false;
            } else {
                Log.d("@M_" + TAG, "selectNetworkMode " + selectNetworkMode);
                if (mPhone != null) {
                    mPhone.setPreferredNetworkType(selectNetworkMode, msg);
                }
            }
        }

        public void onNothingSelected(AdapterView parent) {

        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            AsyncResult ar;
            switch (msg.what) {
            case EVENT_QUERY_NETWORKMODE_DONE:
                Log.d("@M_" + TAG, "Get response EVENT_QUERY_NETWORKMODE_DONE");
                ar = (AsyncResult) msg.obj;
                if (ar.exception == null) {
                    int type = ((int[]) ar.result)[0];
                    Log.d("@M_" + TAG, "Get Preferred Type " + type);
                    switch (type) {
                    case HYBRID:
                        mPreferredNetworkSpinner.setSelection(HYBRID_INDEX, true);
                        break;
                    case CDMA_1X_ONLY:
                        mPreferredNetworkSpinner.setSelection(CDMA_1X_ONLY_INDEX, true);
                        break;
                    case EVDO_ONLY:
                        mPreferredNetworkSpinner.setSelection(EVDO_ONLY_INDEX, true);
                        break;
                    default:
                        break;
                    }
                } else {
                    Toast.makeText(NetworkSelectActivity.this, R.string.query_preferred_fail,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case EVENT_SET_NETWORKMODE_DONE:
                ar = (AsyncResult) msg.obj;
                if ((ar.exception != null) && (mPhone != null)) {
                    mPhone.getPreferredNetworkType(obtainMessage(EVENT_QUERY_NETWORKMODE_DONE));
                }
                break;
            case EVENT_QUERY_EHRPD_ENABLE_DONE:
                ar = (AsyncResult) msg.obj;
                if (ar.exception == null) {
                    String data[] = (String[]) ar.result;
                    if ((data.length > 0) && (data[0] != null)) {
                        Log.d("@M_" + TAG, "data[0]:" + data[0]);
                        mDisableeHRPDCheckBox.setChecked(data[0].equals("+EHRPD:0"));
                    }
                } else {
                    Toast.makeText(NetworkSelectActivity.this, R.string.query_eHRPD_state_fail,
                        Toast.LENGTH_SHORT).show();
                }
                break;
            case EVENT_SET_EHRPD_ENABLE_DONE:
                ar = (AsyncResult) msg.obj;
                if (ar.exception != null) {
                    Toast.makeText(NetworkSelectActivity.this, R.string.set_eHRPD_state_fail,
                        Toast.LENGTH_SHORT).show();
                    queryeHRPDStatus();
                }
                break;
            default:
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.networkmode_switching);
        if (TelephonyManager.getDefault().getPhoneCount() > 1) {
            mPhone = PhoneFactory.getPhone(PhoneConstants.SIM_ID_1);
        } else {
            mPhone = PhoneFactory.getDefaultPhone();
        }


        mPreferredNetworkSpinner = (Spinner) findViewById(R.id.networkModeSwitching);
        mDisableeHRPDCheckBox = (CheckBox) findViewById(R.id.disable_eHRPD);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.mCdmaNetworkLabels));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPreferredNetworkSpinner.setAdapter(adapter);

        mPreferredNetworkSpinner.setOnItemSelectedListener(mPreferredNetworkHandler);
        mDisableeHRPDCheckBox.setOnCheckedChangeListener(this);
        mFirstEnter = true;
    }

    @Override
    protected void onResume() {
        mCurrentSettingsNetworkMode = android.provider.Settings.Global.getInt(getContentResolver(),
                android.provider.Settings.Global.PREFERRED_NETWORK_MODE, Phone.PREFERRED_NT_MODE);
        if (mPhone != null) {
            mPhone.getPreferredNetworkType(mHandler.obtainMessage(EVENT_QUERY_NETWORKMODE_DONE));
        }
        Log.d("@M_" + TAG, "Query EVENT_QUERY_NETWORKMODE_DONE");
        queryeHRPDStatus();
        super.onResume();
    }

    private void queryeHRPDStatus(){
        String atCommand = "AT+eHRPD?";
        sendAtCommand(new String[] {atCommand, "+EHRPD:"}, EVENT_QUERY_EHRPD_ENABLE_DONE);
    }

    private void seteHRPDStatus(int state){
        String atCommand = null;

        switch(state) {
            case 0:
                atCommand = "AT+eHRPD=0";
            break;
            case 1:
                atCommand = "AT+eHRPD=1";
            break;
        }
        sendAtCommand(new String[] {atCommand, ""}, EVENT_SET_EHRPD_ENABLE_DONE);
    }

    private void sendAtCommand(String[] command, int msg) {
        Log.d("@M_" + TAG, "sendAtCommand() " + command[0]);
        if (mPhone != null) {
            mPhone.invokeOemRilRequestStrings(command, mHandler.obtainMessage(msg));
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton view, boolean isChecked) {
        Log.d("@M_" + TAG, "m4GDataOnlyCheckBox check is" + isChecked);
        if (!mFirstEnter) {
            mDisableeHRPDCheckBox.setChecked(isChecked);
            seteHRPDStatus(isChecked ? 0 : 1);
        }
    }
}
