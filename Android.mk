LOCAL_PATH := $(call my-dir)

ifeq ($(TARGET_DEVICE),le_x620)

include $(call all-makefiles-under,$(LOCAL_PATH))

endif
