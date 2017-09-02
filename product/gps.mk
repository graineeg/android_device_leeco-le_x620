# GPS
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.location.gps.xml:system/etc/permissions/android.hardware.location.gps.xml

PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/agps_profiles_conf2.xml:system/etc/agps_profiles_conf2.xml

# GPS
PRODUCT_PACKAGES += \
    YGPS

# GPS library
PRODUCT_PACKAGES += \
    gps.mt6797 \
    libcurl \
    libepos
