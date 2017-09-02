# ANT
PRODUCT_PACKAGES += \
	libantradio \
	antradio_app \
	ANT_RAM_CODE_CONN_V1.BIN \
	AntHalService \
	com.dsi.ant.antradio_library

# ANT Permissions
PRODUCT_COPY_FILES += \
	external/ant-wireless/antradio-library/com.dsi.ant.antradio_library.xml:system/etc/permissions/com.dsi.ant.antradio_library.xml
