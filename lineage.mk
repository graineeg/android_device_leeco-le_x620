# Release Name
PRODUCT_RELEASE_NAME := Le 2 Pro

# Inherit some common CM stuff.
$(call inherit-product, vendor/cm/config/common_full_phone.mk)

# Inherit device configuration
$(call inherit-product, device/leeco/le_x620/full_le_x620.mk)

## Device identifier. This must come after all inclusions
PRODUCT_DEVICE := le_x620
PRODUCT_NAME := lineage_le_x620
PRODUCT_BRAND := LeEco
PRODUCT_MODEL := Le 2 Pro
PRODUCT_MANUFACTURER := LeMobile
