# call the proprietary setup
$(call inherit-product, vendor/leeco/le_x620/le_x620-vendor.mk)

# The gps config appropriate for this device
$(call inherit-product, device/common/gps/gps_as_supl.mk)

LOCAL_PATH := device/leeco/le_x620

DEVICE_PACKAGE_OVERLAYS += $(LOCAL_PATH)/overlay

# Screen density
PRODUCT_AAPT_CONFIG := normal
PRODUCT_AAPT_PREF_CONFIG := xxhdpi
PRODUCT_CHARACTERISTICS := nosdcard

# Audio policy & codec
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/audio_policy.conf:system/etc/audio_policy.conf \
    $(LOCAL_PATH)/configs/audio_device.xml:system/etc/audio_device.xml \
    $(LOCAL_PATH)/configs/media_codecs.xml:system/etc/media_codecs.xml \
    $(LOCAL_PATH)/configs/media_codecs_performance.xml:system/etc/media_codecs_performance.xml \
    $(LOCAL_PATH)/configs/media_profiles.xml:system/etc/media_profiles.xml \
    frameworks/av/media/libstagefright/data/media_codecs_google_audio.xml:system/etc/media_codecs_google_audio.xml \
    frameworks/av/media/libstagefright/data/media_codecs_google_telephony.xml:system/etc/media_codecs_google_telephony.xml \
    frameworks/av/media/libstagefright/data/media_codecs_google_video_le.xml:system/etc/media_codecs_google_video_le.xml \
    frameworks/av/services/audiopolicy/config/a2dp_audio_policy_configuration.xml:/system/etc/a2dp_audio_policy_configuration.xml \
    frameworks/av/services/audiopolicy/config/audio_policy_volumes.xml:/system/etc/audio_policy_volumes.xml \
    frameworks/av/services/audiopolicy/config/default_volume_tables.xml:/system/etc/default_volume_tables.xml \
    frameworks/av/services/audiopolicy/config/r_submix_audio_policy_configuration.xml:/system/etc/r_submix_audio_policy_configuration.xml \
    frameworks/av/services/audiopolicy/config/usb_audio_policy_configuration.xml:/system/etc/usb_audio_policy_configuration.xml

# MTK Helpers
PRODUCT_PACKAGES += \
    libccci_util \
    libgralloc_extra \
    libgui_ext \
    libion \
    lights.mt6797 \
    libui_ext \
    libmtk_symbols

# GPS
PRODUCT_PACKAGES += \
    gps.mt6797 \
    libcurl \
    YGPS

# Extra packages
PRODUCT_PACKAGES += \
    audio.a2dp.default \
    audio_policy.default \
    audio.r_submix.default \
    audio.usb.default \
    libaudio-resampler \
    libemoji \
    com.android.future.usb.accessory \
    libnl_2 \
    libtinyalsa \
    libtinycompress \
    libtinymix \
    libtinyxml

PRODUCT_TAGS += dalvik.gc.type-precise

# Ramdisk
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/rootdir/factory_init.project.rc:root/factory_init.project.rc \
    $(LOCAL_PATH)/rootdir/factory_init.rc:root/factory_init.rc \
    $(LOCAL_PATH)/rootdir/fstab.mt6797:root/fstab.mt6797 \
    $(LOCAL_PATH)/rootdir/init.modem.rc:root/init.modem.rc \
    $(LOCAL_PATH)/rootdir/init.mt6797.rc:root/init.mt6797.rc \
    $(LOCAL_PATH)/rootdir/init.project.rc:root/init.project.rc \
    $(LOCAL_PATH)/rootdir/init.mt6797.usb.rc:root/init.mt6797.usb.rc \
    $(LOCAL_PATH)/rootdir/init.recovery.mt6797.rc:root/init.recovery.mt6797.rc \
    $(LOCAL_PATH)/rootdir/meta_init.modem.rc:root/meta_init.modem.rc \
    $(LOCAL_PATH)/rootdir/meta_init.project.rc:root/meta_init.project.rc \
    $(LOCAL_PATH)/rootdir/meta_init.rc:root/meta_init.rc \
    $(LOCAL_PATH)/rootdir/ueventd.mt6797.rc:root/ueventd.mt6797.rc \
    $(LOCAL_PATH)/rootdir/init.mal.rc:root/init.mal.rc \
    $(LOCAL_PATH)/rootdir/init.trustonic.rc:root/init.trustonic.rc \
    $(LOCAL_PATH)/rootdir/init.volte.rc:root/init.volte.rc
	
# RIL
PRODUCT_PROPERTY_OVERRIDES += \
    ro.kernel.android.checkjni=0 \
    ro.telephony.ril_class=MediaTekRIL \
    ro.telephony.ril.config=fakeiccid \
    ro.com.android.mobiledata=false
	
SIM_COUNT := 2
PRODUCT_PROPERTY_OVERRIDES += ro.telephony.sim.count=$(SIM_COUNT)

# WiFi
PRODUCT_PACKAGES += \
    lib_driver_cmd_mt66xx \
    hostapd \
    libwpa_client \
    wpa_supplicant

# Default.prop
ADDITIONAL_DEFAULT_PROPERTIES += \
    ro.adb.secure=0 \
    ro.secure=0 \
    security.perf_harden=1 \
    ro.allow.mock.location=0 \
    ro.debuggable=1 \
    persist.service.acm.enable=0 \
    ro.mount.fs=EXT4 \
    camera.disable_zsl_mode=1 \
    persist.sys.usb.config=adb

# build.prop
PRODUCT_PROPERTY_OVERRIDES += \
    media.stagefright.legacyencoder=true \
    media.stagefright.less-secure=true

# extra log controls prop
PRODUCT_PROPERTY_OVERRIDES += \
    persist.ril.log=0 \
    ro.disable.xlog=0

# Recovery allowed devices
TARGET_OTA_ASSERT_DEVICE := le_x6,le_x620,Le_X620

# BLOCK_BASED_OTA
BLOCK_BASED_OTA := false

# Kernel
ifeq ($(TARGET_PREBUILT_KERNEL),)
	LOCAL_KERNEL := $(LOCAL_PATH)/prebuilt/kernel
else
	LOCAL_KERNEL := $(TARGET_PREBUILT_KERNEL)
endif

PRODUCT_COPY_FILES += \
   $(LOCAL_KERNEL):kernel

# Permissions
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.audio.low_latency.xml:system/etc/permissions/android.hardware.audio.low_latency.xml \
    frameworks/native/data/etc/android.hardware.bluetooth_le.xml:system/etc/permissions/android.hardware.bluetooth_le.xml \
    frameworks/native/data/etc/android.hardware.bluetooth.xml:system/etc/permissions/android.hardware.bluetooth.xml \
    frameworks/native/data/etc/android.hardware.faketouch.xml:system/etc/permissions/android.hardware.faketouch.xml \
    frameworks/native/data/etc/android.hardware.fingerprint.xml:system/etc/permissions/android.hardware.fingerprint.xml \
    frameworks/native/data/etc/android.hardware.location.gps.xml:system/etc/permissions/android.hardware.location.gps.xml \
    frameworks/native/data/etc/android.hardware.sensor.accelerometer.xml:system/etc/permissions/android.hardware.sensor.accelerometer.xml \
    frameworks/native/data/etc/android.hardware.sensor.compass.xml:system/etc/permissions/android.hardware.sensor.compass.xml \
    frameworks/native/data/etc/android.hardware.sensor.gyroscope.xml:system/etc/permissions/android.hardware.sensor.gyroscope.xml \
    frameworks/native/data/etc/android.hardware.sensor.light.xml:system/etc/permissions/android.hardware.sensor.light.xml \
    frameworks/native/data/etc/android.hardware.sensor.proximity.xml:system/etc/permissions/android.hardware.sensor.proximity.xml \
    frameworks/native/data/etc/android.hardware.sensor.stepcounter.xml:system/etc/permissions/android.hardware.sensor.stepcounter.xml \
    frameworks/native/data/etc/android.hardware.sensor.stepdetector.xml:system/etc/permissions/android.hardware.sensor.stepdetector.xml \
    frameworks/native/data/etc/android.hardware.telephony.gsm.xml:system/etc/permissions/android.hardware.telephony.gsm.xml \
    frameworks/native/data/etc/android.hardware.touchscreen.multitouch.distinct.xml:system/etc/permissions/android.hardware.touchscreen.multitouch.distinct.xml \
    frameworks/native/data/etc/android.hardware.touchscreen.multitouch.jazzhand.xml:system/etc/permissions/android.hardware.touchscreen.multitouch.jazzhand.xml \
    frameworks/native/data/etc/android.hardware.touchscreen.multitouch.xml:system/etc/permissions/android.hardware.touchscreen.multitouch.xml \
    frameworks/native/data/etc/android.hardware.touchscreen.xml:system/etc/permissions/android.hardware.touchscreen.xml \
    frameworks/native/data/etc/android.hardware.usb.accessory.xml:system/etc/permissions/android.hardware.usb.accessory.xml \
    frameworks/native/data/etc/android.hardware.usb.host.xml:system/etc/permissions/android.hardware.usb.host.xml \
    frameworks/native/data/etc/android.hardware.wifi.direct.xml:system/etc/permissions/android.hardware.wifi.direct.xml \
    frameworks/native/data/etc/android.hardware.wifi.xml:system/etc/permissions/android.hardware.wifi.xml \
    frameworks/native/data/etc/handheld_core_hardware.xml:system/etc/permissions/handheld_core_hardware.xml

PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/permissions/android.hardware.camera.xml:system/etc/permissions/android.hardware.camera.xml \
    $(LOCAL_PATH)/configs/permissions/android.hardware.consumerir.xml:system/etc/permissions/android.hardware.consumerir.xml

# Doze
PRODUCT_PACKAGES += \
    LeDoze

# Snap
PRODUCT_PACKAGES += \
    Snap

# Power
PRODUCT_PACKAGES += \
    power.mt6797

# ANT+
PRODUCT_PACKAGES += \
    AntHalService \
    com.dsi.ant.antradio_library \
    libantradio

PRODUCT_COPY_FILES += \
    external/ant-wireless/antradio-library/com.dsi.ant.antradio_library.xml:system/etc/permissions/com.dsi.ant.antradio_library.xml

# For android_filesystem_config.h
PRODUCT_PACKAGES += \
    fs_config_files

# Thermal
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/thermal/.ht120.mtc:system/etc/.tp/.ht120.mtc \
    $(LOCAL_PATH)/configs/thermal/thermal.conf:system/etc/.tp/thermal.conf \
    $(LOCAL_PATH)/configs/thermal/thermal.off.conf:system/etc/.tp/thermal.off.conf \
    $(LOCAL_PATH)/configs/thermal/.thermal_policy_00:system/etc/.tp/.thermal_policy_00 \
    $(LOCAL_PATH)/configs/thermal/.thermal_policy_01:system/etc/.tp/.thermal_policy_01 \
    $(LOCAL_PATH)/configs/thermal/.thermal_policy_02:system/etc/.tp/.thermal_policy_02 \
    $(LOCAL_PATH)/configs/thermal/.thermal_policy_03:system/etc/.tp/.thermal_policy_03 \
    $(LOCAL_PATH)/configs/thermal/.thermal_policy_game_01:system/etc/.tp/.thermal_policy_game_01

# Dalvik/HWUI
$(call inherit-product, frameworks/native/build/phone-xxxhdpi-4096-dalvik-heap.mk)

# Call hwui memory config
$(call inherit-product-if-exists, frameworks/native/build/phone-xxxhdpi-4096-hwui-memory.mk)
