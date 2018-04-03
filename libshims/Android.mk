include $(CLEAR_VARS)

LOCAL_WHOLE_STATIC_LIBRARIES := \
    cpufeatures

LOCAL_MODULE := libshim_bwfocus
LOCAL_MODULE_TAGS := optional
LOCAL_MULTILIB := 32
LOCAL_VENDOR_MODULE := true

include $(BUILD_SHARED_LIBRARY)

$(call import-module,android/cpufeatures)
