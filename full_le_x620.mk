$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)

# Inherit from the common Open Source product configuration
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)

# Inherit from hardware-specific part of the product configuration
$(call inherit-product, device/leeco/le_x620/device.mk)

# Release name
PRODUCT_RELEASE_NAME := le_x620

EXTENDED_FONT_FOOTPRINT := true

PRODUCT_DEVICE := le_x620
PRODUCT_NAME := full_le_x620
PRODUCT_BRAND := LeEco
PRODUCT_MODEL := Le 2 Pro
PRODUCT_MANUFACTURER := LeMobile
PRODUCT_RESTRICT_VENDOR_FILES := false

# Boot animation
TARGET_SCREEN_HEIGHT      := 1920
TARGET_SCREEN_WIDTH       := 1080
TARGET_BOOTANIMATION_NAME := 1080

PRODUCT_DEFAULT_LANGUAGE := en
PRODUCT_DEFAULT_REGION   := US
