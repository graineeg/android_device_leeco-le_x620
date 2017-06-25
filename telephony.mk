# Configs
PRODUCT_COPY_FILES += \
    device/xiaomi/nikel/configs/etc/apns-conf.xml:system/etc/apns-conf.xml \
    device/xiaomi/nikel/configs/etc/ecc_list.xml:system/etc/ecc_list.xml \
    device/xiaomi/nikel/configs/etc/spn-conf.xml:system/etc/spn-conf.xml

# Messaging
PRODUCT_PACKAGES += \
    messaging \
    Stk