#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring


JNICALL
Java_com_novip_novip_Native_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    //APP授权码
    std::string hello = "1234";
    return env->NewStringUTF(hello.c_str());
}

