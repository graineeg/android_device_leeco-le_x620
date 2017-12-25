package com.mediatek.engineermode;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.SystemProperties;

public class FeatureSupport {

    private static final String SUPPORTED = "1";

    public static final String FK_MATV_FEATURE = "ro.have_matv_feature";
    public static final String FK_FD_SUPPORT = "ro.mtk_fd_support";
    public static final String FK_LOG2SERVER_APP = "ro.mtk_log2server_app";
    public static final String FK_SMSREG_APP = "ro.mtk_smsreg_app";
    public static final String FK_BSP_PACKAGE = "ro.mtk_bsp_package";
    public static final String FK_DT_SUPPORT = "ro.mtk_dt_support";
    public static final String FK_EVDO_DT_SUPPORT = "ro.evdo_dt_support";
    public static final String FK_MTK_C2K_SUPPORT = "ro.mtk_c2k_support";
    public static final String FK_MTK_SVLTE_SUPPORT = "ro.mtk_svlte_support";
    public static final String FK_WFD_SUPPORT = "ro.mtk_wfd_support";
    public static final String FK_DEVREG_APP = "ro.mtk_devreg_app";
    public static final String FK_LTE_DC_SUPPORT = "ro.mtk_lte_dc_support";
    public static final String FK_LTE_SUPPORT = "ro.mtk_lte_support";
    public static final String FK_VOLTE_SUPPORT = "ro.mtk_volte_support";
    public static final String FK_TC1_FEATURE = "ro.mtk_tc1_feature";
    public static final String FK_WORLD_PHONE = "ro.mtk_world_phone";
    public static final String FK_WCDMA_PREFERRED = "ro.mtk_rat_wcdma_preferred";
    public static final String FK_VOW_SUPPORT = "ro.mtk_vow_support";
    public static final String FK_VOICE_UNLOCK_SUPPORT = "ro.mtk_voice_unlock_support";
    public static final String FK_AAL_SUPPORT = "ro.mtk_aal_support";
    public static final String FK_CT4GREG_APP = "ro.mtk_ct4greg_app";
    public static final String FK_C2K_SUPPORT = "ro.mtk_c2k_support";
    public static final String FK_SVLTE_SUPPORT = "ro.mtk_svlte_support";
    public static final String FK_SRLTE_SUPPORT = "ro.mtk_srlte_support";
    public static final String FK_C2K_IRAT_SUPPORT = "ro.c2k.irat.support";
    public static final String FK_C2K_MD_IRAT_SUPPORT = "ro.c2k.md.irat.support";
    public static final String FK_VILTE_SUPPORT = "ro.mtk_vilte_support";
    public static final String FK_MTK_WEARABLE_PLATFORM = "ro.mtk_wearable_platform";

    public static final String PK_CDS_EM = "com.mediatek.connectivity";
    public static final String ENG_LOAD = "eng";
    public static final String USER_LOAD = "user";

    public static boolean isSupported(String featureKey) {
        return SUPPORTED.equals(SystemProperties.get(featureKey));
    }
    /**
     * Get em svr support or not.
     *
     * @return true or false.
     */
    public static boolean isSupportedEmSrv() {
        boolean isSupported = true;
        if (SUPPORTED.equals(SystemProperties.get("ro.mtk_gmo_ram_optimize"))) {
            if (!(ENG_LOAD.equals(SystemProperties.get("ro.build.type")))) {
                isSupported = false;
            }
        }
        return isSupported;
    }

    public static boolean isPackageExisted(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(0);
        for (ApplicationInfo ai : packages) {
            if (ai.packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isUserLoad() {
        return USER_LOAD.equals(SystemProperties.get("ro.build.type"));
    }
}
