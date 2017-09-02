LOCAL_PATH := $(call my-dir)
ifneq ($(filter le_x620, $(TARGET_DEVICE)),)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := \
	GraphicBufferExtra.cpp \
	GraphicBufferExtra_hal.cpp

LOCAL_C_INCLUDES := \
	$(LOCAL_PATH)/include

LOCAL_SHARED_LIBRARIES := \
	libhardware \
	libcutils \
	libutils

LOCAL_MODULE := libgralloc_extra

include $(BUILD_SHARED_LIBRARY)
endif
