//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.crazycdn.gvryssdk;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.vr.sdk.widgets.common.VrWidgetRenderer.GLThreadScheduler;
import com.google.vr.sdk.widgets.common.VrWidgetView;
import com.google.vr.sdk.widgets.video.VrVideoEventListener;

import java.io.IOException;

public class VrVideoView extends VrWidgetView {
    private static final String TAG = VrVideoView.class.getSimpleName();
    private static final boolean DEBUG = false;
    private VrVideoPlayer videoPlayer;
    private VrVideoRenderer renderer;

    public VrVideoView(Context var1, AttributeSet var2) {
        super(var1, var2);
    }

    public VrVideoView(Context var1) {
        super(var1);
    }

    protected VrVideoRenderer createRenderer(Context context, GLThreadScheduler var2, float var3, float var4) {
        this.videoPlayer = new VrVideoPlayer(context, new VrVideoPlayer.VideoTexturesListener() {
            @Override
            public void onVideoTexturesReady() {
                VrVideoView.this.renderer.setSphericalMetadata(VrVideoView.this.videoPlayer.getMetadata());
            }
        });
        this.renderer = new VrVideoRenderer(this.videoPlayer, this.getContext(), var2, var3, var4);
        return this.renderer;
    }

    public void pauseRendering() {
        super.pauseRendering();
        this.pauseVideo();
    }

    public void loadVideo(Uri var1, VrVideoOptions var2) throws IOException {
        this.videoPlayer.openUri(var1, var2);
    }

    public void loadVideoFromAsset(String var1, VrVideoOptions var2) throws IOException {
        this.videoPlayer.openAsset(var1, var2);
    }

    public void setMediaDataSourceFactory(DataSource.Factory var1) {
        this.videoPlayer.setMediaDataSourceFactory(var1);
    }

    public void setEventListener(VrVideoEventListener var1) {
        super.setEventListener(var1);
        this.videoPlayer.setEventListener(var1);
    }

    public void playVideo() {
        VrVideoPlayer var1 = this.videoPlayer;
        synchronized(this.videoPlayer) {
            this.videoPlayer.getExoPlayer().setPlayWhenReady(true);
        }
    }

    public void pauseVideo() {
        VrVideoPlayer var1 = this.videoPlayer;
        synchronized(this.videoPlayer) {
            this.videoPlayer.getExoPlayer().setPlayWhenReady(false);
        }
    }

    public void seekTo(long var1) {
        VrVideoPlayer var3 = this.videoPlayer;
        synchronized(this.videoPlayer) {
            this.videoPlayer.getExoPlayer().seekTo(var1);
        }
    }

    public long getDuration() {
        VrVideoPlayer var3 = this.videoPlayer;
        synchronized(this.videoPlayer) {
            long var1 = this.videoPlayer.getExoPlayer().getDuration();
            return var1;
        }
    }

    public long getCurrentPosition() {
        VrVideoPlayer var3 = this.videoPlayer;
        synchronized(this.videoPlayer) {
            long var1 = this.videoPlayer.getExoPlayer().getCurrentPosition();
            return var1;
        }
    }

    public void setVolume(float var1) {
        this.videoPlayer.setVolume(var1);
    }
}
