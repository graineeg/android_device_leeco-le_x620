# Configs
PRODUCT_COPY_FILES += \
    device/leeco/le_x620/configs/etc/apns-conf.xml:system/etc/apns-conf.xml \
    device/leeco/le_x620/configs/etc/ecc_list.xml:system/etc/ecc_list.xml \
    device/leeco/le_x620/configs/etc/spn-conf.xml:system/etc/spn-conf.xml

# Messaging
PRODUCT_PACKAGES += \
    messaging \
    Stk