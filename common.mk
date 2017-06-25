# Common overlays
DEVICE_PACKAGE_OVERLAYS += device/leeco/le_x620/overlay

# Display
PRODUCT_PACKAGES += \
    libion

# GPS
PRODUCT_COPY_FILES += \
    device/leeco/le_x620/configs/etc/agps_profiles_conf2.xml:system/etc/agps_profiles_conf2.xml

# Gello
# PRODUCT_PACKAGES += \
#     Gello

# Snap
PRODUCT_PACKAGES += \
    Snap

# FMRadio
PRODUCT_PACKAGES += \
    libfmjni \
    FMRadio

# Filesystem management tools
PRODUCT_PACKAGES += \
    e2fsck \
    fsck.f2fs \
    mkfs.f2fs \
    make_ext4fs

# exFAT
PRODUCT_PACKAGES += \
    mount.exfat \
    fsck.exfat \
    mkfs.exfat

# NTFS
PRODUCT_PACKAGES += \
    fsck.ntfs \
    mkfs.ntfs \
    mount.ntfs

# USB
PRODUCT_PACKAGES += \
    com.android.future.usb.accessory

# WallpaperPicker
PRODUCT_PACKAGES += \
    WallpaperPicker

# Sensor Calibration
PRODUCT_PACKAGES += \
    libem_sensor_jni

# Date
PRODUCT_BUILD_PROP_OVERRIDES += BUILD_UTC_DATE=0

# include other configs
include device/leeco/le_x620/permissions.mk
include device/leeco/le_x620/media.mk
include device/leeco/le_x620/wifi.mk
include device/leeco/le_x620/telephony.mk