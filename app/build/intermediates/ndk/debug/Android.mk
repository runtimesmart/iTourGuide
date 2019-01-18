LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := JNI_ImageBlur
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_LDLIBS := \
	-lm \
	-llog \
	-ljnigraphics \

LOCAL_SRC_FILES := \
	/Users/yangguang/AndroidStudioProjects/iTourGuide/app/src/main/jni/Android.mk \
	/Users/yangguang/AndroidStudioProjects/iTourGuide/app/src/main/jni/com_itg_jni_ImageBlur.cpp \
	/Users/yangguang/AndroidStudioProjects/iTourGuide/app/src/main/jni/ImageBlur.c \

LOCAL_C_INCLUDES += /Users/yangguang/AndroidStudioProjects/iTourGuide/app/src/main/jni
LOCAL_C_INCLUDES += /Users/yangguang/AndroidStudioProjects/iTourGuide/app/src/debug/jni

include $(BUILD_SHARED_LIBRARY)
