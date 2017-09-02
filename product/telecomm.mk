# Telecomm
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/ecc_list.xml:system/etc/ecc_list.xml \
    $(LOCAL_PATH)/configs/spn-conf.xml:system/etc/spn-conf.xml

# Data properties
PRODUCT_PROPERTY_OVERRIDES += \
    ro.com.android.mobiledata=false
