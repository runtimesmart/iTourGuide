LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE   := JNI_ImageBlur
LOCAL_SRC_FILES := com_itg_jni_ImageBlur.cpp
LOCAL_LDLIBS    := -ljnigraphics -llog
include $(BUILD_SHARED_LIBRARY)
