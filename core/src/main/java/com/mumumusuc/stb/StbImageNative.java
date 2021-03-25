package com.mumumusuc.stb;

import java.nio.Buffer;

public class StbImageNative {
    private StbImageNative() {
    }

    /*JNI
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
     */

    public static native void nInit();/*
        jclass clz_StbIOCallbacks = env->FindClass("com/mumumusuc/stb/StbIOCallbacks");
        assert(clz_StbIOCallbacks && "cannot find class `StbIOCallbacks`");

        g_callback_mid.read = env->GetMethodID(clz_StbIOCallbacks,"read","(Ljava/nio/ByteBuffer;)I");
        assert(g_callback_mid.read && "cannot find callback method `read`");

        g_callback_mid.skip = env->GetMethodID(clz_StbIOCallbacks,"skip","(I)V");
        assert(g_callback_mid.skip && "cannot find callback method `skip`");

        g_callback_mid.eof = env->GetMethodID(clz_StbIOCallbacks,"eof","()I");
        assert(g_callback_mid.eof && "cannot find callback method `eof`");
    */

    public static native int nInfoFromMemory(Buffer buffer, int len, int[] x, int[] y, int[] channels);/*
        return stbi_info_from_memory(buffer, len, &x[0], &y[0], &channels[0]);
    */

    public static native int nInfoFromCallbacks(StbIOCallbacks cb, int[] x, int[] y, int[] channels);/*
        stbi_io_callbacks clbk = IO_CALLBACKS_INSTANCE;
        env_holder holder = {env, cb};
        return stbi_info_from_callbacks(&clbk, &holder, &x[0], &y[0], &channels[0]);
    */

    public static native int nIs16BitFromMemory(Buffer buffer, int len);/*
        return stbi_is_16_bit_from_memory(buffer, len);
    */

    public static native int nIs16BitFromCallbacks(StbIOCallbacks cb);/*
        stbi_io_callbacks clbk = IO_CALLBACKS_INSTANCE;
        env_holder holder = {env, cb};
        return stbi_is_16_bit_from_callbacks(&clbk, &holder);
    */

    //#define STBI_NO_STDIO
    //public static native int nInfo();
    //public static native int nIs16Bit();


    public static native Buffer nLoadFromMemory(Buffer buffer, int len, int[] x, int[] y, int[] channels, int desiredChannels);/*
        stbi_uc* data = stbi_load_from_memory((stbi_uc*)buffer, len, &x[0], &y[0], &channels[0], desiredChannels);
        size_t size = x[0] * y[0] * channels[0] * 8 / 8;
        return env->NewDirectByteBuffer(data, size);
    */

    public static native Buffer nLoadFromCallback(StbIOCallbacks cb, int[] x, int[] y, int[] channels, int desiredChannels);/*
        stbi_io_callbacks clbk = IO_CALLBACKS_INSTANCE;
        env_holder holder = {env, cb};
        stbi_uc* data = stbi_load_from_callbacks(&clbk, &holder, &x[0], &y[0], &channels[0], desiredChannels);
        size_t size = x[0] * y[0] * channels[0] * 8 / 8;
        return env->NewDirectByteBuffer(data, size);
    */

    // configs
    public static native void nSetUnpremultiplyOnLoad(int flag_true_if_should_unpremultiply);/*
        stbi_set_unpremultiply_on_load(flag_true_if_should_unpremultiply);
    */

    public static native void nConvertIphonePngToRgb(int flag_true_if_should_convert);/*
        stbi_convert_iphone_png_to_rgb(flag_true_if_should_convert);
    */

    public static native void nSetFlipVerticallyOnLoad(int flag_true_if_should_flip);/*
        stbi_set_flip_vertically_on_load(flag_true_if_should_flip);
    */

    public static native String nFailureReason();/*
        return env->NewStringUTF(stbi_failure_reason());
    */
}
