//
// Created by yunshang on 18-1-18.
//

#include <jni.h>

#define CLASSNAME               com_crazycdn_gvryssdk_VrVideoRenderer
#define JNINAME3(CLASS3, FUNC3) Java_##CLASS3##_##FUNC3
#define JNINAME2(CLASS2, FUNC2) JNINAME3(CLASS2, FUNC2)
#define JNINAME(FUNC)           JNINAME2(CLASSNAME, FUNC)

extern jlong Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeCreate(JNIEnv *, jobject, jobject, jobject, jfloat);
JNIEXPORT jlong JNICALL JNINAME(nativeCreate)(JNIEnv *env, jobject object, jobject javaLoader, jobject javaContext, jfloat val)
{
    return Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeCreate(env, object, javaLoader, javaContext, val);
}

extern void Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeResize (JNIEnv *, jobject, jlong, jint, jint, jfloat, jfloat, jint);
JNIEXPORT void JNICALL JNINAME(nativeResize)(JNIEnv *env, jobject object, jlong var1, jint var3, jint var4, jfloat var5, jfloat var6, jint var7)
{
    Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeResize(env, object, var1, var3, var4, var5, var6, var7);
}

extern void Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeRenderFrame(JNIEnv *, jobject, jlong);
JNIEXPORT void JNICALL JNINAME(nativeRenderFrame)(JNIEnv *env, jobject object, jlong var1)
{
    Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeRenderFrame(env, object, var1);
}

extern void Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeSetStereoMode(JNIEnv *, jobject, jlong, jboolean);
JNIEXPORT void JNICALL JNINAME(nativeSetStereoMode)(JNIEnv *env, jobject object, jlong var1, jboolean var3)
{
    Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeSetStereoMode(env, object, var1, var3);
}

extern void Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeSetPureTouchTracking(JNIEnv *, jobject, jlong, jboolean);
JNIEXPORT void JNICALL JNINAME(nativeSetPureTouchTracking)(JNIEnv *env, jobject object, jlong var1, jboolean var3)
{
    Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeSetPureTouchTracking(env, object, var1, var3);
}

extern void Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeDestroy(JNIEnv *, jobject, jlong);
JNIEXPORT void JNICALL JNINAME(nativeDestroy)(JNIEnv *env, jobject object, jlong var1)
{
    Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeDestroy(env, object, var1);
}

extern void Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeOnPause(JNIEnv *, jobject, jlong);
JNIEXPORT void JNICALL JNINAME(nativeOnPause)(JNIEnv *env, jobject object, jlong var1)
{
    Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeOnPause(env, object, var1);
}

extern void Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeOnResume(JNIEnv *, jobject, jlong);
JNIEXPORT void JNICALL JNINAME(nativeOnResume)(JNIEnv *env, jobject object, jlong var1)
{
    Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeOnResume(env, object, var1);
}

extern void Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeOnPanningEvent(JNIEnv *, jobject, jlong, jfloat, jfloat);
JNIEXPORT void JNICALL JNINAME(nativeOnPanningEvent)(JNIEnv *env, jobject object, jlong var1, jfloat var3, jfloat var4)
{
    Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeOnPanningEvent(env, object, var1, var3, var4);
}

extern void Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeGetHeadRotation(JNIEnv *, jobject, jlong, jfloatArray);
JNIEXPORT void JNICALL JNINAME(nativeGetHeadRotation)(JNIEnv *env, jobject object, jlong var1, jfloatArray var3)
{
    Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeGetHeadRotation(env, object, var1, var3);
}

extern void Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeSetSphericalMetadata(JNIEnv *, jobject, jlong, jbyteArray);
JNIEXPORT void JNICALL JNINAME(nativeSetSphericalMetadata)(JNIEnv *env, jobject object, jlong var1, jbyteArray var3)
{
    Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeSetSphericalMetadata(env, object, var1, var3);
}

extern jlong Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeSetVideoTexture(JNIEnv *, jobject, jlong, jintArray);
JNIEXPORT jlong JNICALL JNINAME(nativeSetVideoTexture)(JNIEnv *env, jobject object, jlong var1, jintArray var3)
{
    return Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeSetVideoTexture(env, object, var1, var3);
}

extern void Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeSetCameraRotation(JNIEnv *, jobject, jlong, jfloatArray);
JNIEXPORT void JNICALL JNINAME(nativeSetCameraRotation)(JNIEnv *env, jobject object, jlong var1, jfloatArray var3)
{
    Java_com_google_vr_sdk_widgets_video_VrVideoRenderer_nativeSetCameraRotation(env, object, var1, var3);
}
