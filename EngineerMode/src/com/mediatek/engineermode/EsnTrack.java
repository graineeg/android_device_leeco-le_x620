package com.mediatek.engineermode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemProperties;
//L1.MP3 Patchback only
import android.preference.EditTextPreference;
//L1.MP3 Patchback only

import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

public class EsnTrack extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "EM/EsnTrack";

    private static final String ESN_TRACK_SYS_PROP = "persist.sys.esn_track_switch";
    private static final String KEY_ESN_TRACK_SWITCH = "esn_track_switch";
    // L1.MP3 Patchback only
    private static final String KEY_ESN_TRACK_ADDRESS = "EsnServerAdderess";
    public static final String MY_ESN_ADDRESS = "MyEsnAddrFile";
    public static final String KEY_ADDRESS_ESN = "address";
    private EditTextPreference mEditTextPref;
    private static final String cdmaAddressIntent =
                        "com.mediatek.engineermode.EsnTrack.ADDRESS_CHANGE";
    // L1.MP3 Patchback only

    private static final String TURN_ON = "On";
    private static final String TURN_OFF = "Off";
    private ListPreference mListPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.esn_track);
        mListPreference = (ListPreference) findPreference(KEY_ESN_TRACK_SWITCH);
        mListPreference.setOnPreferenceChangeListener(this);
        // L1.MP3 Patchback only
        mEditTextPref = (EditTextPreference) findPreference(KEY_ESN_TRACK_ADDRESS);
        mEditTextPref.setOnPreferenceChangeListener(this);
        // L1.MP3 Patchback only

        final String savedEsnValue = getEsnTrackProperty();
        final String summary = savedEsnValue.equals("1") ? TURN_ON : TURN_OFF;
        mListPreference.setSummary(summary);
        mListPreference.setValue(savedEsnValue.equals("1") ? "1" : "0");

        // L1.MP3 Patchback only
        SharedPreferences sharedpreferences = this.getSharedPreferences(
                MY_ESN_ADDRESS, this.MODE_PRIVATE);
        String sendAddress = sharedpreferences.getString(KEY_ADDRESS_ESN, "");
        Log.d(TAG, "onCreate sendAddress: " + sendAddress);
        if (sendAddress != null && sendAddress.length() > 0) {
            mEditTextPref.setSummary(sendAddress);
        } else {
            mEditTextPref.setSummary("Enter address");
        }
        mEditTextPref.setText(sendAddress);
        // L1.MP3 Patchback only

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.d(TAG, "onPreferenceChange newValue " + (String) newValue);
        if (preference.getKey().equals(KEY_ESN_TRACK_SWITCH)) {
            Log.d(TAG, "onPreferenceChange newValue " + (String) newValue);
            setEsnTrackProperty((String) newValue);
            String property = getEsnTrackProperty();
            final boolean isEnabled = property.equals("1");
            mListPreference.setValue(isEnabled ? "1" : "0");
            final String summary = isEnabled ? TURN_ON : TURN_OFF;
            mListPreference.setSummary(summary);
        }

        // L1.MP3 Patchback only
        if (preference.getKey().equals(KEY_ESN_TRACK_ADDRESS)) {
            Log.d(TAG, "onPreferenceChange address newValue :"
                    + (String) newValue.toString());
            SharedPreferences sharedpreferences = this.getSharedPreferences(
                    MY_ESN_ADDRESS, this.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(KEY_ADDRESS_ESN, (String) newValue.toString());
            editor.commit();
            String writtenaddress = sharedpreferences.getString(
                    KEY_ADDRESS_ESN, "");
            if (writtenaddress != null && writtenaddress.length() > 0) {
                mEditTextPref.setSummary(writtenaddress);
            } else {
                mEditTextPref.setSummary("Enter address");
            }
            mEditTextPref.setText(writtenaddress);
            this.sendBroadcast(new Intent(cdmaAddressIntent).putExtra(
                    "Address", writtenaddress));
        }
        // L1.MP3 Patchback only
        return false;
    }

    private String getEsnTrackProperty() {
        String value = "0";
        value = SystemProperties.get("persist.sys.esn_track_switch");
        Log.d(TAG, "getEsnTrackProperty esn :" + value);
        return value;
    }

    private void setEsnTrackProperty(String esn) {
        Log.d(TAG, "setEsnTrackProperty esn :" + esn);
        if (esn.equals("1")) {
            SystemProperties.set("persist.sys.esn_track_switch", "1");
            String value = SystemProperties.get("persist.sys.esn_track_switch");
            Log.d(TAG, "setEsnTrackProperty set on esn :" + value);
        } else {
            SystemProperties.set("persist.sys.esn_track_switch", "0");
            String value = SystemProperties.get("persist.sys.esn_track_switch");
            Log.d(TAG, "setEsnTrackProperty set off esn :" + value);
        }

    }
}
