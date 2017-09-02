LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := ccci_lib.c

LOCAL_STATIC_LIBRARIES := libcutils liblog
	
LOCAL_SHARED_LIBRARIES := libc

LOCAL_MODULE := libccci_util
LOCAL_MULTILIB := both

LOCAL_MODULE_TAGS := optional
LOCAL_PRELINK_MODULE:=false

include $(BUILD_SHARED_LIBRARY)
