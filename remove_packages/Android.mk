LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := RemovePackagesGiuliac
LOCAL_MODULE_CLASS := APPS
LOCAL_MODULE_TAGS := optional

LOCAL_OVERRIDES_PACKAGES += \
    CalendarGoogle \
    Chrome64 \
    Gmail2 \
    Photos \
    Maps \
    Drive \
    talkback \
    YouTube \
    Accord \
    FossifyGallery \
    com.google.android.as

LOCAL_UNINSTALLABLE_MODULE := true
LOCAL_CERTIFICATE := PRESIGNED
LOCAL_SRC_FILES := /dev/null
include $(BUILD_PREBUILT)
