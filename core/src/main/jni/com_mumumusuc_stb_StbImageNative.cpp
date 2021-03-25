#include <com_mumumusuc_stb_StbImageNative.h>

//@line:9

        #define STB_IMAGE_IMPLEMENTATION
        #define STBI_NO_STDIO
        #include <stb_image.h>
        #include <assert.h>

        typedef struct {
            JNIEnv* env;
            jobject callback;
        } env_holder;

        struct{
            jmethodID read;
            jmethodID skip;
            jmethodID eof;
        } g_callback_mid;

        static int io_callbacks_read(void *user,char *data,int size){
            JNIEnv* env = ((env_holder*)user)->env;
            jobject callback = ((env_holder*)user)->callback;
            return env->CallIntMethod(callback, g_callback_mid.read, env->NewDirectByteBuffer(data, size));
        }

        static void io_callbacks_skip(void *user,int n){
            JNIEnv* env = ((env_holder*)user)->env;
            jobject callback = ((env_holder*)user)->callback;
            env->CallVoidMethod(callback, g_callback_mid.skip, n);
        }

        static int io_callbacks_eof(void* user){
            JNIEnv* env = ((env_holder*)user)->env;
            jobject callback = ((env_holder*)user)->callback;
            return env->CallIntMethod(callback, g_callback_mid.eof);
        }

        #define IO_CALLBACKS_INSTANCE (stbi_io_callbacks){  \
            .read = io_callbacks_read,                      \
            .skip = io_callbacks_skip,                      \
            .eof  = io_callbacks_eof,                       \
        }
     JNIEXPORT void JNICALL Java_com_mumumusuc_stb_StbImageNative_nInit(JNIEnv* env, jclass clazz) {


//@line:51

        jclass clz_StbIOCallbacks = env->FindClass("com/mumumusuc/stb/StbIOCallbacks");
        assert(clz_StbIOCallbacks && "cannot find class `StbIOCallbacks`");

        g_callback_mid.read = env->GetMethodID(clz_StbIOCallbacks,"read","(Ljava/nio/ByteBuffer;)I");
        assert(g_callback_mid.read && "cannot find callback method `read`");

        g_callback_mid.skip = env->GetMethodID(clz_StbIOCallbacks,"skip","(I)V");
        assert(g_callback_mid.skip && "cannot find callback method `skip`");

        g_callback_mid.eof = env->GetMethodID(clz_StbIOCallbacks,"eof","()I");
        assert(g_callback_mid.eof && "cannot find callback method `eof`");
    

}

static inline jint wrapped_Java_com_mumumusuc_stb_StbImageNative_nInfoFromMemory
(JNIEnv* env, jclass clazz, jobject obj_buffer, jint len, jintArray obj_x, jintArray obj_y, jintArray obj_channels, unsigned char* buffer, int* x, int* y, int* channels) {

//@line:65

        return stbi_info_from_memory(buffer, len, &x[0], &y[0], &channels[0]);
    
}

JNIEXPORT jint JNICALL Java_com_mumumusuc_stb_StbImageNative_nInfoFromMemory(JNIEnv* env, jclass clazz, jobject obj_buffer, jint len, jintArray obj_x, jintArray obj_y, jintArray obj_channels) {
	unsigned char* buffer = (unsigned char*)(obj_buffer?env->GetDirectBufferAddress(obj_buffer):0);
	int* x = (int*)env->GetIntArrayElements(obj_x, 0);
	int* y = (int*)env->GetIntArrayElements(obj_y, 0);
	int* channels = (int*)env->GetIntArrayElements(obj_channels, 0);

	jint JNI_returnValue = wrapped_Java_com_mumumusuc_stb_StbImageNative_nInfoFromMemory(env, clazz, obj_buffer, len, obj_x, obj_y, obj_channels, buffer, x, y, channels);

	env->ReleaseIntArrayElements(obj_x, (jint*)x, JNI_OK);
	env->ReleaseIntArrayElements(obj_y, (jint*)y, JNI_OK);
	env->ReleaseIntArrayElements(obj_channels, (jint*)channels, JNI_OK);

	return JNI_returnValue;
}

static inline jint wrapped_Java_com_mumumusuc_stb_StbImageNative_nInfoFromCallbacks
(JNIEnv* env, jclass clazz, jobject cb, jintArray obj_x, jintArray obj_y, jintArray obj_channels, int* x, int* y, int* channels) {

//@line:69

        stbi_io_callbacks clbk = IO_CALLBACKS_INSTANCE;
        env_holder holder = {env, cb};
        return stbi_info_from_callbacks(&clbk, &holder, &x[0], &y[0], &channels[0]);
    
}

JNIEXPORT jint JNICALL Java_com_mumumusuc_stb_StbImageNative_nInfoFromCallbacks(JNIEnv* env, jclass clazz, jobject cb, jintArray obj_x, jintArray obj_y, jintArray obj_channels) {
	int* x = (int*)env->GetIntArrayElements(obj_x, 0);
	int* y = (int*)env->GetIntArrayElements(obj_y, 0);
	int* channels = (int*)env->GetIntArrayElements(obj_channels, 0);

	jint JNI_returnValue = wrapped_Java_com_mumumusuc_stb_StbImageNative_nInfoFromCallbacks(env, clazz, cb, obj_x, obj_y, obj_channels, x, y, channels);

	env->ReleaseIntArrayElements(obj_x, (jint*)x, JNI_OK);
	env->ReleaseIntArrayElements(obj_y, (jint*)y, JNI_OK);
	env->ReleaseIntArrayElements(obj_channels, (jint*)channels, JNI_OK);

	return JNI_returnValue;
}

static inline jint wrapped_Java_com_mumumusuc_stb_StbImageNative_nIs16BitFromMemory
(JNIEnv* env, jclass clazz, jobject obj_buffer, jint len, unsigned char* buffer) {

//@line:75

        return stbi_is_16_bit_from_memory(buffer, len);
    
}

JNIEXPORT jint JNICALL Java_com_mumumusuc_stb_StbImageNative_nIs16BitFromMemory(JNIEnv* env, jclass clazz, jobject obj_buffer, jint len) {
	unsigned char* buffer = (unsigned char*)(obj_buffer?env->GetDirectBufferAddress(obj_buffer):0);

	jint JNI_returnValue = wrapped_Java_com_mumumusuc_stb_StbImageNative_nIs16BitFromMemory(env, clazz, obj_buffer, len, buffer);


	return JNI_returnValue;
}

JNIEXPORT jint JNICALL Java_com_mumumusuc_stb_StbImageNative_nIs16BitFromCallbacks(JNIEnv* env, jclass clazz, jobject cb) {


//@line:79

        stbi_io_callbacks clbk = IO_CALLBACKS_INSTANCE;
        env_holder holder = {env, cb};
        return stbi_is_16_bit_from_callbacks(&clbk, &holder);
    

}

static inline jobject wrapped_Java_com_mumumusuc_stb_StbImageNative_nLoadFromMemory
(JNIEnv* env, jclass clazz, jobject obj_buffer, jint len, jintArray obj_x, jintArray obj_y, jintArray obj_channels, jint desiredChannels, unsigned char* buffer, int* x, int* y, int* channels) {

//@line:90

        stbi_uc* data = stbi_load_from_memory((stbi_uc*)buffer, len, &x[0], &y[0], &channels[0], desiredChannels);
        size_t size = x[0] * y[0] * channels[0] * 8 / 8;
        return env->NewDirectByteBuffer(data, size);
    
}

JNIEXPORT jobject JNICALL Java_com_mumumusuc_stb_StbImageNative_nLoadFromMemory(JNIEnv* env, jclass clazz, jobject obj_buffer, jint len, jintArray obj_x, jintArray obj_y, jintArray obj_channels, jint desiredChannels) {
	unsigned char* buffer = (unsigned char*)(obj_buffer?env->GetDirectBufferAddress(obj_buffer):0);
	int* x = (int*)env->GetIntArrayElements(obj_x, 0);
	int* y = (int*)env->GetIntArrayElements(obj_y, 0);
	int* channels = (int*)env->GetIntArrayElements(obj_channels, 0);

	jobject JNI_returnValue = wrapped_Java_com_mumumusuc_stb_StbImageNative_nLoadFromMemory(env, clazz, obj_buffer, len, obj_x, obj_y, obj_channels, desiredChannels, buffer, x, y, channels);

	env->ReleaseIntArrayElements(obj_x, (jint*)x, JNI_OK);
	env->ReleaseIntArrayElements(obj_y, (jint*)y, JNI_OK);
	env->ReleaseIntArrayElements(obj_channels, (jint*)channels, JNI_OK);

	return JNI_returnValue;
}

static inline jobject wrapped_Java_com_mumumusuc_stb_StbImageNative_nLoadFromCallback
(JNIEnv* env, jclass clazz, jobject cb, jintArray obj_x, jintArray obj_y, jintArray obj_channels, jint desiredChannels, int* x, int* y, int* channels) {

//@line:96

        stbi_io_callbacks clbk = IO_CALLBACKS_INSTANCE;
        env_holder holder = {env, cb};
        stbi_uc* data = stbi_load_from_callbacks(&clbk, &holder, &x[0], &y[0], &channels[0], desiredChannels);
        size_t size = x[0] * y[0] * channels[0] * 8 / 8;
        return env->NewDirectByteBuffer(data, size);
    
}

JNIEXPORT jobject JNICALL Java_com_mumumusuc_stb_StbImageNative_nLoadFromCallback(JNIEnv* env, jclass clazz, jobject cb, jintArray obj_x, jintArray obj_y, jintArray obj_channels, jint desiredChannels) {
	int* x = (int*)env->GetIntArrayElements(obj_x, 0);
	int* y = (int*)env->GetIntArrayElements(obj_y, 0);
	int* channels = (int*)env->GetIntArrayElements(obj_channels, 0);

	jobject JNI_returnValue = wrapped_Java_com_mumumusuc_stb_StbImageNative_nLoadFromCallback(env, clazz, cb, obj_x, obj_y, obj_channels, desiredChannels, x, y, channels);

	env->ReleaseIntArrayElements(obj_x, (jint*)x, JNI_OK);
	env->ReleaseIntArrayElements(obj_y, (jint*)y, JNI_OK);
	env->ReleaseIntArrayElements(obj_channels, (jint*)channels, JNI_OK);

	return JNI_returnValue;
}

JNIEXPORT void JNICALL Java_com_mumumusuc_stb_StbImageNative_nSetUnpremultiplyOnLoad(JNIEnv* env, jclass clazz, jint flag_true_if_should_unpremultiply) {


//@line:105

        stbi_set_unpremultiply_on_load(flag_true_if_should_unpremultiply);
    

}

JNIEXPORT void JNICALL Java_com_mumumusuc_stb_StbImageNative_nConvertIphonePngToRgb(JNIEnv* env, jclass clazz, jint flag_true_if_should_convert) {


//@line:109

        stbi_convert_iphone_png_to_rgb(flag_true_if_should_convert);
    

}

JNIEXPORT void JNICALL Java_com_mumumusuc_stb_StbImageNative_nSetFlipVerticallyOnLoad(JNIEnv* env, jclass clazz, jint flag_true_if_should_flip) {


//@line:113

        stbi_set_flip_vertically_on_load(flag_true_if_should_flip);
    

}

JNIEXPORT jstring JNICALL Java_com_mumumusuc_stb_StbImageNative_nFailureReason(JNIEnv* env, jclass clazz) {


//@line:117

        return env->NewStringUTF(stbi_failure_reason());
    

}

