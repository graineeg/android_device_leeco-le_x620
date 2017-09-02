#include <ui/GraphicBufferMapper.h>
#include <ui/PixelFormat.h>
#include <ui/Rect.h>
#include <log/log.h>
#include <dlfcn.h>

/*
#include <cutils/process_name.h>
#include <cutils/properties.h>
#include <utils/String8.h>
#include <utils/KeyedVector.h>
#include <utils/Singleton.h>
*/

#define LOG_TAG "DECKER_SHIM"

extern "C" {
    void _ZN7android19GraphicBufferMapper9lockYCbCrEPK13native_handlejRKNS_4RectEP13android_ycbcr(buffer_handle_t, uint32_t, const android::Rect&, android_ycbcr*);

    void _ZN7android19GraphicBufferMapper9lockYCbCrEPK13native_handleiRKNS_4RectEP13android_ycbcr(buffer_handle_t handle, int usage, const android::Rect& bounds, android_ycbcr *ycbcr) {
        _ZN7android19GraphicBufferMapper9lockYCbCrEPK13native_handlejRKNS_4RectEP13android_ycbcr(handle, static_cast<uint32_t>(usage), bounds, ycbcr);
    }

    void _ZN7android19GraphicBufferMapper4lockEPK13native_handlejRKNS_4RectEPPv(buffer_handle_t, uint32_t, const android::Rect&, void**);

    void _ZN7android19GraphicBufferMapper4lockEPK13native_handleiRKNS_4RectEPPv(buffer_handle_t handle, int usage, const android::Rect& bounds, void** vaddr) {
        _ZN7android19GraphicBufferMapper4lockEPK13native_handlejRKNS_4RectEPPv(handle, static_cast<uint32_t>(usage), bounds, vaddr);
    }

    /* void _ZN7android13GraphicBufferC1EjjijNSt3__112basic_stringIcNS1_11char_traitsIcEENS1_9allocatorIcEEEE(
            uint32_t inWidth, uint32_t inHeight, android::PixelFormat inFormat,
            uint32_t inUsage, std::string requestorName); */

//#ifdef FRAMEWORK_NATIVE_NOT_SUPPORT_OLD_BUFFER_REQ
    void _ZN7android13GraphicBufferC1Ejjij(void *instance, uint32_t inWidth, uint32_t inHeight, android::PixelFormat inFormat, uint32_t inUsage) {
                                                        // uint32_t inWidth, uint32_t inHeight, PixelFormat inFormat, uint32_t inUsage
        static void (*func)(void *instance, uint32_t, uint32_t, android::PixelFormat, uint32_t, std::string) = NULL;
	static void (*func2)(void *instance) = NULL;
        std::string my_requestorName("<Unknown>");

	ALOGI("_ZN7android13GraphicBufferC1Ejjij: begin ...\n");
	ALOGI("_ZN7android13GraphicBufferC1Ejjij(instance = %08X, inWidth = %d, inHeight = %d, inFormat = %d, inUsage = %08X)\n", instance, inWidth, inHeight, (uint32_t)inFormat, inUsage);
	func  = (void (*)(void *instance, uint32_t, uint32_t, android::PixelFormat, uint32_t, std::string))dlsym(RTLD_NEXT, "_ZN7android13GraphicBufferC1EjjijNSt3__112basic_stringIcNS1_11char_traitsIcEENS1_9allocatorIcEEEE");	
	func2 = (void (*)(void *instance))dlsym(RTLD_NEXT, "_ZN7android13GraphicBufferC1Ev");	
	
	ALOGI("_ZN7android13GraphicBufferC1EjjijNSt3__112basic_stringIcNS1_11char_traitsIcEENS1_9allocatorIcEEEE [0x%08X]\n",func);
	ALOGI("_ZN7android13GraphicBufferC1Ev [0x%08X]\n",func2);

	func(instance, inWidth, inHeight, inFormat, inUsage, my_requestorName);
	// func2(instance);
        /* _ZN7android13GraphicBufferC1EjjijNSt3__112basic_stringIcNS1_11char_traitsIcEENS1_9allocatorIcEEEE(
            inWidth, inHeight, inFormat, inUsage, "<Unknown>"); */
	ALOGI("_ZN7android13GraphicBufferC1Ejjij: end ...\n");

	/*
	android::String8 mProcessName;
	
        int pid = getpid();
        FILE *fp = fopen(String8::format("/proc/%d/cmdline", pid), "r");
        if (NULL != fp) {
            const size_t size = 64;
            char proc_name[size];
            fgets(proc_name, size, fp);
            fclose(fp);
            mProcessName = proc_name;
        } else {
            mProcessName = "unknownProcess";
        }
       
        ALOGI("Process name: %s\n",proc_name);
        */
    }
//#endif

    void _ZN7android5Fence4waitEi(int);

    void _ZN7android5Fence4waitEj(unsigned int timeout) {
        _ZN7android5Fence4waitEi(static_cast<int>(timeout));
    }
}
