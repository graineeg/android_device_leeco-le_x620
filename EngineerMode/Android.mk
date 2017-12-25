#ifneq ($(filter 6753, $(TARGET_DEVICE)),)

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_STATIC_JAVA_LIBRARIES := android-support-v4

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := EngineerMode
LOCAL_CERTIFICATE := platform

# to let app run on 32-bit space
#LOCAL_JNI_SHARED_LIBRARIES := libem_platform32_dummy

LOCAL_JAVA_LIBRARIES += bouncycastle conscrypt telephony-common ims-common

LOCAL_EMMA_COVERAGE_FILTER := @$(LOCAL_PATH)/emma_filter.txt

#LOCAL_MULTILIB := 32

LOCAL_PROGUARD_ENABLED := disabled

include $(BUILD_PACKAGE)

# Use the folloing include to make our test apk.
include $(call all-makefiles-under,$(LOCAL_PATH))
#endif
