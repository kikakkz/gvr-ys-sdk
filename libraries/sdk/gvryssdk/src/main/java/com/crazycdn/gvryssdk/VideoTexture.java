package com.crazycdn.gvryssdk;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by yunshang on 18-1-20.
 */

public class VideoTexture {
    private static final String TAG = VideoTexture.class.getSimpleName();
    private static final boolean DEBUG = false;
    private SurfaceTexture surfaceTexture;
    private final int[] textureHandle = new int[1];
    private boolean needUpdateTexture = false;
    private VideoTexture.OnNewFrameListener newFrameListener;

    public VideoTexture() {
    }

    public void init() {
        if(this.surfaceTexture != null) {
            Log.w(TAG, "Texture is already initialized");
        } else {
            GLES20.glGenTextures(1, this.textureHandle, 0);
            this.surfaceTexture = new SurfaceTexture(this.textureHandle[0]);
            this.surfaceTexture.setOnFrameAvailableListener(new VideoTexture.UpdateSurfaceListener());
            GLES20.glBindTexture('赥', this.textureHandle[0]);
        }
    }

    public void setOnNewFrameListener(VideoTexture.OnNewFrameListener var1) {
        this.newFrameListener = var1;
    }

    public synchronized void updateTexture() {
        if(this.needUpdateTexture) {
            if(this.surfaceTexture != null) {
                this.surfaceTexture.updateTexImage();
            }

            this.needUpdateTexture = false;
        }

    }

    public SurfaceTexture getSurfaceTexture() {
        return this.surfaceTexture;
    }

    public boolean getIsTextureSet() {
        return this.surfaceTexture != null;
    }

    public int getTextureId() {
        return this.textureHandle[0];
    }

    public long getTimestamp() {
        return this.surfaceTexture.getTimestamp();
    }

    public synchronized void release() {
        if(this.surfaceTexture != null) {
            this.surfaceTexture.release();
            GLES20.glDeleteTextures(1, this.textureHandle, 0);
            this.surfaceTexture = null;
        }

    }

    private class UpdateSurfaceListener implements SurfaceTexture.OnFrameAvailableListener {
        private UpdateSurfaceListener() {
        }

        public void onFrameAvailable(SurfaceTexture var1) {
            VideoTexture var2 = VideoTexture.this;
            synchronized(VideoTexture.this) {
                VideoTexture.this.needUpdateTexture = true;
            }

            if(VideoTexture.this.newFrameListener != null) {
                VideoTexture.this.newFrameListener.onNewFrame();
            }

        }
    }

    public interface OnNewFrameListener {
        void onNewFrame();
    }
}
