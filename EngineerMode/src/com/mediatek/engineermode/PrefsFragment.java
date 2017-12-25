/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein is
 * confidential and proprietary to MediaTek Inc. and/or its licensors. Without
 * the prior written permission of MediaTek inc. and/or its licensors, any
 * reproduction, modification, use or disclosure of MediaTek Software, and
 * information contained herein, in whole or in part, shall be strictly
 * prohibited.
 *
 * MediaTek Inc. (C) 2010. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER
 * ON AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL
 * WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NONINFRINGEMENT. NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH
 * RESPECT TO THE SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY,
 * INCORPORATED IN, OR SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES
 * TO LOOK ONLY TO SUCH THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO.
 * RECEIVER EXPRESSLY ACKNOWLEDGES THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO
 * OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES CONTAINED IN MEDIATEK
 * SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK SOFTWARE
 * RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S
 * ENTIRE AND CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE
 * RELEASED HEREUNDER WILL BE, AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE
 * MEDIATEK SOFTWARE AT ISSUE, OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE
 * CHARGE PAID BY RECEIVER TO MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 *
 * The following software/firmware and/or related documentation ("MediaTek
 * Software") have been modified by MediaTek Inc. All revisions are subject to
 * any receiver's applicable license agreements with MediaTek Inc.
 */

package com.mediatek.engineermode;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.provider.Settings;
import android.widget.Toast;

import com.mediatek.engineermode.cip.CipUtil;
import com.mediatek.engineermode.ptp.PtpList;
import java.io.File;

public class PrefsFragment extends PreferenceFragment {
    private static final String TAG = "EM/PrefsFragment";
    private static int[] FRAGMENT_RES = {R.xml.telephony,
            R.xml.connectivity, R.xml.hardware_testing, R.xml.location,
            R.xml.log_and_debugging, R.xml.others};

    private static int[] FRAGMENT_RES_WIFIONLY = {
            R.xml.connectivity, R.xml.hardware_testing, R.xml.location,
            R.xml.log_and_debugging, R.xml.others};
    private static final String INNER_LOAD_INDICATOR_FILE =
            "/system/etc/system_update/address.xml";
    private static final int MTK_NFC_CHIP_TYPE_MSR3110 = 0x01;
    private static final int MTK_NFC_CHIP_TYPE_6605 = 0x02;
    private static final String FILE_DHRY_0 = "/sys/bus/platform/drivers/slt_cpu0_dhry/slt_cpu0_dhry";
    private static final String[] KEY_REMOVE_ARRAY = {"dcm", "deep_idle", "cpu_dvfs", "pll_cg",
                    "mcdi_setting", "sleep_mode", "de_sense", "display", "battery_log", "sensor",
                    "io", "touchscreen", "memory"};
    private int mXmlResId;
    private boolean mIsInit = false;

    /**
     * Default empty constructor
     */
    public PrefsFragment() {

    }

    /**
     * Set this fragment resource
     *
     * @param resIndex
     *            Resource ID
     */
    public void setResource(int resIndex) {
        mXmlResId = resIndex;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isWifiOnly()){
            FRAGMENT_RES = FRAGMENT_RES_WIFIONLY;
        }

    }
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen screen, Preference pref) {
        if (pref == null || FeatureSupport.isSupportedEmSrv()) {
            return super.onPreferenceTreeClick(screen, pref);
        }
        String id = pref.getKey();
        for (String c : KEY_REMOVE_ARRAY) {
            if (id.equals(c)) {
                Toast.makeText(getActivity(), R.string.notice_wo_emsvr,
                    Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return super.onPreferenceTreeClick(screen, pref);
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    private void removePreference(PreferenceScreen prefScreen, String prefId) {
        Preference pref = (Preference) findPreference(prefId);
        if (pref != null) {
            prefScreen.removePreference(pref);
        }
    }

    private void removeUnsupportedItems() {
        PreferenceScreen screen = getPreferenceScreen();

        if (!FeatureSupport.isSupported(FeatureSupport.FK_DT_SUPPORT)) {
            removePreference(screen, "dualtalk_network_info");
            removePreference(screen, "dualtalk_bandmode");
        }

        // Duplicate with Network Selecting, remove them
        removePreference(screen, "digital_standard");


        if (!FeatureSupport.isSupported(FeatureSupport.FK_MTK_C2K_SUPPORT)) {
            if (!FeatureSupport.isSupported(FeatureSupport.FK_DT_SUPPORT)) {
                removePreference(screen, "dualtalk_network_select");
            } else {
                removePreference(screen, "network_select");
            }
        }

        // if
        // (NfcAdapter.getDefaultAdapter(getActivity().getApplicationContext())
        // == null) {
        // removePreference(screen, "nfc");
        // }
        // it's ok
        int versionCode = Settings.Global.getInt(getActivity().
                getContentResolver(), "nfc_controller_code", -1);
        if (FRAGMENT_RES[mXmlResId] == R.xml.connectivity) {
            switch (versionCode) {
                case MTK_NFC_CHIP_TYPE_MSR3110 :
                    Log.i("@M_" + TAG, "MSR3110 nfc chip, call nfc");
                    removePreference(screen, "hqanfc");
                    break;
                case MTK_NFC_CHIP_TYPE_6605 :
                    Log.i("@M_" + TAG, "6605 nfc chip, call hqanfc");
                    removePreference(screen, "nfc");
                    break;
                default:
                    Log.i("@M_" + TAG, "no nfc chip support");
                    removePreference(screen, "hqanfc");
                    removePreference(screen, "nfc");
                    removePreference(screen, "nfc_dta");
                    break;
            }
        }

        if (SystemProperties.get("ro.mtk_wifiwpsp2p_nfc_support").equals("1") == false) {
            removePreference(screen, "wps_nfc_list");
        }
        /*if (! .MTK_LOG2SERVER_APP) {
            removePreference(screen, "log2server");
        }*/

        if (FeatureSupport.isSupported(FeatureSupport.FK_EVDO_DT_SUPPORT)
                || !FeatureSupport.isSupported(FeatureSupport.FK_DT_SUPPORT)
                || !FeatureSupport.isSupported(FeatureSupport.FK_MTK_C2K_SUPPORT)) {
            removePreference(screen, "ext_md_logger");
        }

        if (!FeatureSupport.isSupported(FeatureSupport.FK_SMSREG_APP)) {
            removePreference(screen, "device_manager");
        }

        if (FeatureSupport.isSupported(FeatureSupport.FK_BSP_PACKAGE)) {
            removePreference(screen, "auto_answer");
        }

        if (ChipSupport.isFeatureSupported(ChipSupport.MTK_FM_SUPPORT)) {
            if (!ChipSupport.isFeatureSupported(ChipSupport.MTK_FM_TX_SUPPORT)) {
                removePreference(screen, "fm_transmitter");
            }
        } else {
            removePreference(screen, "fm_receiver");
            removePreference(screen, "fm_transmitter");
        }

        // AGPS is not ready if MTK_AGPS_APP isn't defined
        if (!ChipSupport.isFeatureSupported(ChipSupport.MTK_AGPS_APP)
                || !ChipSupport.isFeatureSupported(ChipSupport.MTK_GPS_SUPPORT)) {
            removePreference(screen, "location_basedservice");
        }

        removePreference(screen, "fused_location_provider");

        if (!ChipSupport.isFeatureSupported(ChipSupport.MTK_AGPS_APP)
                || !ChipSupport.isFeatureSupported(ChipSupport.MTK_GPS_SUPPORT)
                || !isVoiceCapable() || isWifiOnly()) {
            removePreference(screen, "auto_dialer");
        }

        if (!ChipSupport.isFeatureSupported(ChipSupport.MTK_GPS_SUPPORT)) {
            removePreference(screen, "ygps");
            removePreference(screen, "cw_test");
        }

        // MATV is not ready if HAVE_MATV_FEATURE isn't defined
        if (!ChipSupport.isFeatureSupported(ChipSupport.HAVE_MATV_FEATURE)) {
            removePreference(screen, "matv");
        }

        // BT is not ready if MTK_BT_SUPPORT isn't defined
        if (!ChipSupport.isFeatureSupported(ChipSupport.MTK_BT_SUPPORT)) {
            removePreference(screen, "bluetooth");
        }

        removePreference(screen, "audio");

        // wifi is not ready if MTK_WLAN_SUPPORT isn't defined
        if (!ChipSupport.isFeatureSupported(ChipSupport.MTK_WLAN_SUPPORT)) {
            removePreference(screen, "wifi");
        }

        if (!isVoiceCapable() || isWifiOnly()) {
            removePreference(screen, "auto_answer");
            removePreference(screen, "repeat_call_test");
        }

        if (isWifiOnly()) {
            removePreference(screen, "amr_wb");
            removePreference(screen, "antenna");
            removePreference(screen, "bandmode");
            removePreference(screen, "cfu");
            removePreference(screen, "fast_dormancy");
            removePreference(screen, "gprs");
            removePreference(screen, "hspa_info");
            removePreference(screen, "mobile_data_prefer");
            removePreference(screen, "modem_test");
            removePreference(screen, "modem_warning");
            removePreference(screen, "network_info");
            removePreference(screen, "dualtalk_network_info");
            removePreference(screen, "network_select");
            removePreference(screen, "cdma_network_select");
            removePreference(screen, "dualtalk_network_select");
            removePreference(screen, "rat_mode");
            removePreference(screen, "rf_desense_test");
            removePreference(screen, "sbp");
            removePreference(screen, "swla");
            removePreference(screen, "simme_lock1");
            removePreference(screen, "simme_lock2");
            removePreference(screen, "modem_switch");
            removePreference(screen, "lte_config");
            removePreference(screen, "lte_network_mode");
            removePreference(screen, "lte_network_info");
            removePreference(screen, "lte_tool");
            removePreference(screen, "md_em_filter");
            removePreference(screen, "ehrpd_bg_data");
            removePreference(screen, "sim_switch");
        }

        // if it single sim, then the flow is the same as before
        if (TelephonyManager.getDefault().getSimCount() > 1) {
            /**
             * if it is Gemini, then the flow is : it start a TabActivity, then
             * the TabActivity will start sim1 or sim2 simLock activity Intent
             * to launch SIM lock TabActivity
             */
            // intent.setComponent(new
            // ComponentName("com.android.simmelock","com.android.simmelock.TabLockList"));
            removePreference(screen, "simme_lock1");
        } else {
            // Intent to launch SIM lock settings
            // intent.setComponent(new
            // ComponentName("com.android.simmelock","com.android.simmelock.LockList"));
            removePreference(screen, "simme_lock2");
        }
        Log.i("@M_" + TAG, "ChipSupport.getChip(): " + ChipSupport.getChip());
        if (ChipSupport.MTK_6589_SUPPORT > ChipSupport.getChip()) {
            removePreference(screen, "de_sense");
            removePreference(screen, "camera89");
        } else {
            removePreference(screen, "camera");
        }

        if (!FeatureSupport.isSupported(FeatureSupport.FK_FD_SUPPORT)) {
            removePreference(screen, "fast_dormancy");
        }

        File innerLoadIndicator = new File(INNER_LOAD_INDICATOR_FILE);
        if (!innerLoadIndicator.exists()) {
            removePreference(screen, "system_update");
        }
        if (!ChipSupport.isChipInSet(ChipSupport.CHIP_657X_SERIES_NEW)) {
            removePreference(screen, "deep_idle");
            removePreference(screen, "sleep_mode");
            removePreference(screen, "dcm");
            removePreference(screen, "pll_cg");
            removePreference(screen, "cpu_dvfs");
            removePreference(screen, "mcdi_setting");
        }

        if (!FeatureSupport.isPackageExisted(this.getActivity(), FeatureSupport.PK_CDS_EM)) {
            removePreference(screen, "cds_information");
        }

        Preference pref = (Preference) findPreference("cmas");
        if (pref != null && !isActivityAvailable(pref.getIntent())) {
            removePreference(screen, "cmas");
        }

        if (!FeatureSupport.isSupported(FeatureSupport.FK_EVDO_DT_SUPPORT)) {
            removePreference(screen, "saber");
        }

        String mOptr = SystemProperties.get("ro.operator.optr");
        String mOpSeg = SystemProperties.get("ro.operator.seg");
        if (!"OP01".equals(mOptr)) {
            removePreference(screen, "ConfigureCheck2_Send_Test");
            removePreference(screen, "ConfigureCheck2_Self_Test");
        }

        if (!"OP09".equals(mOptr) &&
            SystemProperties.get("ro.ct6m_support").equals("1") == false) {
            removePreference(screen, "CT_ConfigureCheck");
        }

        String usbCheckerState = SystemProperties.get("ro.mtk_usb_cba_support", "0");
        Log.i("@M_" + TAG, "ro.mtk_usb_cba_support is " + usbCheckerState);

        if (!usbCheckerState.equals("1")) {
            removePreference(screen, "usb_checker_enabler");
        }

        if (!SystemProperties.get("ro.mtk_cmcc_ft_precheck_support").equals("1")) {
            removePreference(screen, "cmcc_ftprecheck");
        }

        if (!SystemProperties.get("ro.mtk_modem_monitor_support").equals("1")) {
            removePreference(screen, "mdml_sample");
        }

        if (FeatureSupport.isSupported(FeatureSupport.FK_MTK_C2K_SUPPORT)) { // For C2K
            // keep all network menu: network_select, cdma_network_select, network_selection
            //removePreference(screen, "swla");
            removePreference(screen, "dualtalk_network_select");
            // hide IR menu temporarily. API not ready yet.
            removePreference(screen, "network_selection");
        } else if (("OP09".equals(mOptr)) && ("SEGDEFAULT".equals(mOpSeg))) { // For CT
            //removePreference(screen, "swla");
            removePreference(screen, "network_select");
            removePreference(screen, "dualtalk_network_select");
        } else {
            removePreference(screen, "cdma_network_select");
            removePreference(screen, "network_selection");
        }

        if (!(("OP09".equals(mOptr)) && ("SEGDEFAULT".equals(mOpSeg)))) {
            removePreference(screen, "test_card");
        }

        if (!FeatureSupport.isSupported(FeatureSupport.FK_DEVREG_APP)) {
            removePreference(screen, "device_register");
            Log.i("@M_" + TAG, "Not show entry for DEVREG.");
        }

         removePreference(screen, "wfd_settings");

        if (!FeatureSupport.isSupported(FeatureSupport.FK_LTE_DC_SUPPORT)) {
            removePreference(screen, "lte_config");
        }
        if (!FeatureSupport.isSupported(FeatureSupport.FK_LTE_SUPPORT)) {
            removePreference(screen, "lte_network_info");
        }

        if (FeatureSupport.isSupported(FeatureSupport.FK_LTE_DC_SUPPORT)) {
            removePreference(screen, "network_select");
        } else {
            removePreference(screen, "lte_network_mode");
        }

        if (!CipUtil.isCipSupported()) {
            removePreference(screen, "cip");
        }

        if (UserHandle.MU_ENABLED && UserManager.supportsMultipleUsers()
                && UserManager.get(getActivity()).getUserHandle() != UserHandle.USER_OWNER) {
            // Remove all items used phone instance
            removePreference(screen, "amr_wb");
            removePreference(screen, "antenna");
            removePreference(screen, "bandmode");
            removePreference(screen, "te_dev_tool");
            removePreference(screen, "cds_information");
            removePreference(screen, "cfu");
            removePreference(screen, "fast_dormancy");
            removePreference(screen, "gprs");
            removePreference(screen, "hspa_info");
            removePreference(screen, "mobile_data_prefer");
            removePreference(screen, "modem_test");
            removePreference(screen, "modem_warning");
            removePreference(screen, "network_info");
            removePreference(screen, "dualtalk_network_info");
            removePreference(screen, "network_select");
            removePreference(screen, "cdma_network_select");
            removePreference(screen, "dualtalk_network_select");
            removePreference(screen, "rat_mode");
            removePreference(screen, "rf_desense_test");
            removePreference(screen, "sbp");
            removePreference(screen, "swla");
            removePreference(screen, "simme_lock1");
            removePreference(screen, "simme_lock2");
            removePreference(screen, "modem_switch");
            removePreference(screen, "lte_config");
            removePreference(screen, "lte_network_mode");
            removePreference(screen, "lte_network_info");
            removePreference(screen, "lte_tool");
            removePreference(screen, "world_mode");
            removePreference(screen, "md_em_filter");
            removePreference(screen, "ehrpd_bg_data");
            removePreference(screen, "misc_config");
        }



        if (!FeatureSupport.isSupported(FeatureSupport.FK_LTE_SUPPORT)
                && ChipSupport.getChip() <= ChipSupport.MTK_6595_SUPPORT) {
            removePreference(screen, "antenna");
        }

        if (!PtpList.isPtpSupported()) {
            removePreference(screen, "ptp_tech");
        }

        if (!FeatureSupport.isSupported(FeatureSupport.FK_VOLTE_SUPPORT)) {
            removePreference(screen, "ims");
        }

        if (!SystemProperties.get("ro.sim_me_lock_mode", "0").equals("0")) {
           removePreference(screen, "simme_lock1");
           removePreference(screen, "simme_lock2");
        }
        if (!new File(FILE_DHRY_0).exists()) {
            removePreference(screen, "cpu_stress_test");
        }
        if (!FeatureSupport.isSupported(FeatureSupport.FK_AAL_SUPPORT)) {
            removePreference(screen, "aal");
        }

        if (!FeatureSupport.isSupported(FeatureSupport.FK_TC1_FEATURE)) {
            removePreference(screen, "security_status");
            removePreference(screen, "antenna_diversity");
        } else {
            removePreference(screen, "antenna");
        }
        if (SystemProperties.get("ro.mtk_sensorhub_support").equals("1") == false) {
            removePreference(screen, "sensor_hub_test");
            removePreference(screen, "smart_motion");
        }

        if (ChipSupport.getChip() < ChipSupport.MTK_6735_SUPPORT) {
            removePreference(screen, "amr_wb");
        }
        if (!((FeatureSupport.isSupported(FeatureSupport.FK_SVLTE_SUPPORT)
               || FeatureSupport.isSupported(FeatureSupport.FK_SRLTE_SUPPORT))
            && FeatureSupport.isSupported(FeatureSupport.FK_C2K_IRAT_SUPPORT))) {
            removePreference(screen, "c2k_ir_settings");
        }

        if (!FeatureSupport.isSupported(FeatureSupport.FK_VILTE_SUPPORT)) {
            removePreference(screen, "vilte");
        }
        if (FeatureSupport.isSupported(FeatureSupport.FK_MTK_WEARABLE_PLATFORM)) {
            removePreference(screen, "camera89");
            removePreference(screen, "camera_desence");
            removePreference(screen, "usb");
            removePreference(screen, "dcm");
            removePreference(screen, "deep_idle");
            removePreference(screen, "mcdi_setting");
            removePreference(screen, "fm_receiver");
            removePreference(screen, "settings_font");
            removePreference(screen, "device_manager");
            removePreference(screen, "debug_utils");
            removePreference(screen, "sd_card_test");
            removePreference(screen, "user2root");
            removePreference(screen, "sleep_mode");
//            removePreference(screen, "lcm");
            removePreference(screen, "modem_debug");
        }
        if (!FeatureSupport.isSupported(FeatureSupport.FK_MTK_C2K_SUPPORT)) {
            removePreference(screen, "bypass");
        }
        if (FeatureSupport.isUserLoad()) {
            removePreference(screen, "lte_tool");
        }
    }

    private boolean isVoiceCapable() {
        TelephonyManager telephony = (TelephonyManager) getActivity()
                .getSystemService(Context.TELEPHONY_SERVICE);
        boolean bVoiceCapable = telephony != null && telephony.isVoiceCapable();
        Log.i("@M_" + TAG, "sIsVoiceCapable : " + bVoiceCapable);
        return bVoiceCapable;
    }

    private boolean isWifiOnly() {
        ConnectivityManager connManager = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean bWifiOnly = false;
        if (null != connManager) {
            bWifiOnly = !connManager
                    .isNetworkSupported(ConnectivityManager.TYPE_MOBILE);
            Log.i("@M_" + TAG, "bWifiOnly: " + bWifiOnly);
        }
        return bWifiOnly;
    }

    private boolean isActivityAvailable(Intent intent) {
        return null != getActivity().getPackageManager().resolveActivity(intent, 0);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("@M_" + TAG, "setUserVisibleHint : " + isVisibleToUser + " index of " + mXmlResId);
        if (isVisibleToUser) {
            if (!mIsInit) {
                // Load preferences from xml.
                addPreferencesFromResource(FRAGMENT_RES[mXmlResId]);
                removeUnsupportedItems();
                mIsInit = true;
            }
            PreferenceScreen screen = getPreferenceScreen();

            int count = screen.getPreferenceCount();
            for (int i = 0; i < count; i++) {
                Preference pre = screen.getPreference(i);
                if (null != pre) {
                    Intent intent = pre.getIntent();
                    pre.setEnabled(isActivityAvailable(intent));
                }
            }
        }
    }


}
