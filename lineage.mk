$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)

# Inherit from the common Open Source product configuration
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)

$(call inherit-product, $(SRC_TARGET_DIR)/product/languages_full.mk)

# Inherit from hardware-specific part of the product configuration
$(call inherit-product, device/leeco/le_x620/device.mk)

# Inherit some common CM stuff.
$(call inherit-product, vendor/cm/config/common_full_phone.mk)

# Release Name
PRODUCT_RELEASE_NAME := Le 2 Pro

## Device identifier. This must come after all inclusions
PRODUCT_DEVICE := le_x620
PRODUCT_NAME := lineage_le_x620
PRODUCT_BRAND := LeEco
PRODUCT_MODEL := Le 2 Pro
PRODUCT_MANUFACTURER := LeMobile
PRODUCT_RESTRICT_VENDOR_FILES := false

# Boot animation
TARGET_SCREEN_HEIGHT      := 1920
TARGET_SCREEN_WIDTH       := 1080
TARGET_BOOTANIMATION_NAME := 1080