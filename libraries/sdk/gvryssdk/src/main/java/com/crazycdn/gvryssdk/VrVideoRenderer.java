package com.crazycdn.gvryssdk;

import android.content.Context;
import android.os.SystemClock;

import com.crazycdn.gvryssdk.anon.SphericalMetadataOuterClass;
import com.google.vr.sdk.widgets.common.VrWidgetRenderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by yunshang on 18-1-18.
 */

public class VrVideoRenderer extends VrWidgetRenderer {
    private static final String TAG = VrVideoRenderer.class.getSimpleName();
    private static final boolean DEBUG = false;
    private VrVideoPlayer player;
    private SphericalMetadataOuterClass.SphericalMetadata metadata;
    private float frameRate;
    private int elapsedFramesSinceMeasurement;
    private long lastFrameTimeMs;
    private volatile VrVideoRenderer.LoadVideoRequest lastVideoRequest;

    public VrVideoRenderer(VrVideoPlayer var1, Context var2, VrWidgetRenderer.GLThreadScheduler var3, float var4, float var5) {
        super(var2, var3, var4, var5);
        this.player = var1;
        System.loadLibrary("ysrenderer");
    }

    public void setSphericalMetadata(SphericalMetadataOuterClass.SphericalMetadata var1) {
        this.lastVideoRequest = new VrVideoRenderer.LoadVideoRequest(var1);
        this.postApiRequestToGlThread(this.lastVideoRequest);
    }

    public void onDrawFrame(GL10 var1) {
        if(this.player.prepareFrame()) {
            if(this.player.getCameraRotationMatrix() != null) {
                this.nativeSetCameraRotation(this.getNativeRenderer(), this.player.getCameraRotationMatrix());
            }

            super.onDrawFrame(var1);
        }

        if(this.lastFrameTimeMs == 0L) {
            this.lastFrameTimeMs = SystemClock.elapsedRealtime();
        }

        ++this.elapsedFramesSinceMeasurement;
    }

    public void shutdown() {
        this.player.shutdown();
        super.shutdown();
    }

    protected void onViewDetach() {
        this.player.onViewDetach();
    }

    public void onSurfaceCreated(GL10 var1, EGLConfig var2) {
        super.onSurfaceCreated(var1, var2);
        if(this.lastVideoRequest != null) {
            this.executeApiRequestOnGlThread(this.lastVideoRequest);
        }

    }

    float getCurrentFramerate() {
        return this.frameRate;
    }

    protected native long nativeCreate(ClassLoader var1, Context var2, float var3);

    protected native void nativeResize(long var1, int var3, int var4, float var5, float var6, int var7);

    protected native void nativeRenderFrame(long var1);

    protected native void nativeSetStereoMode(long var1, boolean var3);

    protected native void nativeSetPureTouchTracking(long var1, boolean var3);

    protected native void nativeDestroy(long var1);

    protected native void nativeOnPause(long var1);

    protected native void nativeOnResume(long var1);

    protected native void nativeOnPanningEvent(long var1, float var3, float var4);

    protected native void nativeGetHeadRotation(long var1, float[] var3);

    private native void nativeSetSphericalMetadata(long var1, byte[] var3);

    private native long nativeSetVideoTexture(long var1, int[] var3);

    private native void nativeSetCameraRotation(long var1, float[] var3);

    private class LoadVideoRequest implements VrWidgetRenderer.ApiRequest {
        private final SphericalMetadataOuterClass.SphericalMetadata metadata;

        public LoadVideoRequest(SphericalMetadataOuterClass.SphericalMetadata var2) {
            this.metadata = var2;
        }

        public void execute() {
            VrVideoRenderer.this.nativeSetSphericalMetadata(VrVideoRenderer.this.getNativeRenderer(), SphericalMetadataOuterClass.SphericalMetadata.toByteArray(this.metadata));
            VrVideoRenderer.this.nativeSetVideoTexture(VrVideoRenderer.this.getNativeRenderer(), VrVideoRenderer.this.player.bindTexture());
        }
    }
}
