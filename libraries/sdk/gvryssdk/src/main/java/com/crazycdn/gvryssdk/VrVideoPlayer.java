package com.crazycdn.gvryssdk;

import android.content.Context;
import android.net.Uri;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Surface;

import com.crazycdn.gvryssdk.anon.SphericalMetadataOuterClass;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yunshang on 18-1-18.
 */

public class VrVideoPlayer implements MediaSourceEventListener {
    private static final String TAG = "VrVideoPlayer";
    private static final String EXO_USER_AGENT = "Video Player Widget";
    private static final boolean DEBUG = false;
    private final VrVideoPlayer.VideoTexturesListener videoTexturesListener;
    private final VideoTexture videoTexture = new VideoTexture();
    private final VrSimpleExoPlayer simpleExoPlayer;
    private final int[] textureIds = new int[1];
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private boolean isBuffering = false;
    private boolean isLooping;
    private Context context;
    private VrVideoEventListener eventListener;
    private DataSource.Factory mediaDataSourceFactory;
    private float volume = 1.0F;
    private float[] cameraRotationMatrix;
    private SphericalMetadataOuterClass.SphericalMetadata metadata;

    protected String userAgent = "gvr-ys-player/Android";

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    public VrVideoPlayer(Context var1, VrVideoPlayer.VideoTexturesListener var2) {
        this.context = var1;
        this.simpleExoPlayer = new VrSimpleExoPlayer(var1, BANDWIDTH_METER);
        this.videoTexturesListener = var2;
        this.simpleExoPlayer.setProjectionListener(new VrVideoPlayer.ProjectionDataListener());
        this.getExoPlayer().addListener(new VrVideoPlayer.VideoLooperListener());
        this.getExoPlayer().setPlayWhenReady(true);
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    /**
     * Returns a new DataSource factory.
     *
     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
     *     DataSource factory.
     * @return A new DataSource factory.
     */
    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        DefaultBandwidthMeter bm = useBandwidthMeter ? BANDWIDTH_METER : null;
        return new DefaultDataSourceFactory(this.context.getApplicationContext(),
                bm, buildHttpDataSourceFactory(bm));
    }

    private synchronized void loadVideoIntoPlayer(Uri var1, VrVideoOptions var2) {
        if(var2 == null) {
            var2 = new VrVideoOptions();
        } else {
            var2.validate();
        }

        if(this.mediaDataSourceFactory == null) {
            this.mediaDataSourceFactory = buildDataSourceFactory(true);
        }

        MediaSource var3 = this.buildMediaSource(var1, var2, this.mainHandler, this);
        this.simpleExoPlayer.prepare(var3);
        this.videoTexture.setOnNewFrameListener(new VrVideoPlayer.NewFrameNotifier());
        if(this.videoTexturesListener != null) {
            this.videoTexturesListener.onVideoTexturesReady();
        }

        this.applyVolumeToPlayer();
    }

    private MediaSource buildMediaSource(
            Uri uri,
            VrVideoOptions options,
            @Nullable Handler handler,
            @Nullable MediaSourceEventListener listener) {
        switch (options.inputFormat) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        buildDataSourceFactory(false))
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory),
                        buildDataSourceFactory(false))
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(uri, handler, listener);
            default: {
                throw new IllegalStateException("Unsupported type: " + options.inputFormat);
            }
        }
    }

    public void setEventListener(VrVideoEventListener var1) {
        this.eventListener = var1;
    }

    public void openAsset(String var1, VrVideoOptions var2) throws IOException {
        this.metadata = new SphericalMetadataOuterClass.SphericalMetadata();
        if(var2 != null) {
            this.metadata = createMetadataFromOptions(var2);
        } else {
            this.metadata = parseMetadataFromVideoInputStream(this.context.getAssets().open(var1));
        }

        String var10001 = String.valueOf(var1);
        String var10000;
        if(var10001.length() != 0) {
            var10000 = "file:///android_asset/".concat(var10001);
        } else {
            return;
        }

        Uri var3 = Uri.parse(var10000);
        this.loadVideoIntoPlayer(var3, var2);
    }

    public void openUri(Uri var1, VrVideoOptions var2) throws IOException {
        this.metadata = new SphericalMetadataOuterClass.SphericalMetadata();
        if(var2 != null) {
            this.metadata = createMetadataFromOptions(var2);
        } else {
            String var3 = var1.getScheme();
            if(var3 == null || !var3.startsWith("http")) {
                this.metadata = parseMetadataFromVideoInputStream(new FileInputStream(var1.getPath()));
            }
        }

        this.loadVideoIntoPlayer(var1, var2);
    }

    public SphericalMetadataOuterClass.SphericalMetadata getMetadata() {
        return this.metadata;
    }

    public byte[] getMetadataBytes() {
        return SphericalMetadataOuterClass.SphericalMetadata.toByteArray(this.metadata);
    }

    public float[] getCameraRotationMatrix() {
        return this.cameraRotationMatrix;
    }

    public synchronized int[] bindTexture() {
        if(!this.videoTexture.getIsTextureSet()) {
            this.videoTexture.init();
        }

        Surface var1 = new Surface(this.videoTexture.getSurfaceTexture());
        this.simpleExoPlayer.setVideoSurface(var1);
        this.simpleExoPlayer.seekTo(this.getExoPlayer().getCurrentPosition() + 1L);
        this.textureIds[0] = this.videoTexture.getTextureId();
        return this.textureIds;
    }

    public synchronized boolean prepareFrame() {
        boolean var1 = true;
        if(this.videoTexture.getIsTextureSet()) {
            this.videoTexture.updateTexture();
            if(this.simpleExoPlayer.getFrameRotationBuffer() != null) {
                long var2 = this.videoTexture.getTimestamp() / 1000L;
                long var4 = this.simpleExoPlayer.getSampleTimestampUsForReleaseTimeUs(var2);
                this.cameraRotationMatrix = this.simpleExoPlayer.getFrameRotationBuffer().getTransform(var4);
            } else if(this.simpleExoPlayer.getInputFormat() != null && this.simpleExoPlayer.getInputFormat().rotationDegrees != -1) {
                if(this.cameraRotationMatrix == null) {
                    this.cameraRotationMatrix = new float[16];
                }

                Matrix.setIdentityM(this.cameraRotationMatrix, 0);
                Matrix.rotateM(this.cameraRotationMatrix, 0, (float)this.simpleExoPlayer.getInputFormat().rotationDegrees, 0.0F, 0.0F, 1.0F);
            }
        } else {
            var1 = false;
        }

        return var1;
    }

    public synchronized void onViewDetach() {
        this.simpleExoPlayer.setVideoSurface((Surface)null);
        this.videoTexture.release();
    }

    public synchronized void shutdown() {
        this.getExoPlayer().stop();
        this.getExoPlayer().release();
        this.videoTexture.release();
    }

    public synchronized long getCurrentPositionMs() {
        return this.getExoPlayer().getCurrentPosition();
    }

    public VrSimpleExoPlayer getExoPlayer() {
        return this.simpleExoPlayer;
    }

    public synchronized void setVolume(float var1) {
        this.volume = var1;
        this.applyVolumeToPlayer();
    }

    void setMediaDataSourceFactory(DataSource.Factory var1) {
        this.mediaDataSourceFactory = var1;
    }

    private synchronized void applyVolumeToPlayer() {
        this.simpleExoPlayer.setVolume(this.volume);
    }

    private static SphericalMetadataOuterClass.SphericalMetadata createMetadataFromOptions(VrVideoOptions var0) {
        SphericalMetadataOuterClass.SphericalMetadata var1 = new SphericalMetadataOuterClass.SphericalMetadata();
        switch(var0.inputType) {
            case VrVideoOptions.TYPE_MONO:
                var1.frameLayoutMode = 1;
                break;
            case VrVideoOptions.TYPE_STEREO_OVER_UNDER:
                var1.frameLayoutMode = 2;
                break;
            default:
                int var2 = var0.inputType;
                throw new IllegalArgumentException((new StringBuilder(40)).append("Unexpected options.inputType ").append(var2).toString());
        }

        return var1;
    }

    private static SphericalMetadataOuterClass.SphericalMetadata parseMetadataFromVideoInputStream(InputStream var0) throws IOException {
        new SphericalMetadataOuterClass.SphericalMetadata();
        String var2 = SphericalMetadataMP4.extract(var0);
        SphericalMetadataOuterClass.SphericalMetadata var1 = SphericalMetadataParser.parse(var2);
        var0.close();
        return var1;
    }

    @Override
    public void onLoadStarted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs) {

    }

    @Override
    public void onLoadCompleted(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {

    }

    @Override
    public void onLoadCanceled(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded) {

    }

    @Override
    public void onLoadError(DataSpec dataSpec, int dataType, int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaStartTimeMs, long mediaEndTimeMs, long elapsedRealtimeMs, long loadDurationMs, long bytesLoaded, IOException error, boolean wasCanceled) {
        String var10000 = TAG;
        int var19 = this.hashCode();
        String var20 = String.valueOf(error);
        Log.e(var10000, (new StringBuilder(56 + String.valueOf(var20).length())).append(var19).append("AdaptiveMediaSourceEventListener.onLoadError ").append(var20).toString());
        if(this.eventListener != null) {
            this.eventListener.onLoadError(error.toString());
        }
    }

    public void onUpstreamDiscarded(int var1, long var2, long var4) {
    }

    @Override
    public void onDownstreamFormatChanged(int trackType, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long mediaTimeMs) {

    }

    private class NewFrameNotifier implements VideoTexture.OnNewFrameListener, Runnable {
        private final Handler mainHandler;

        private NewFrameNotifier() {
            this.mainHandler = new Handler(Looper.getMainLooper());
        }

        public void onNewFrame() {
            this.mainHandler.post(this);
        }

        public void run() {
            if(VrVideoPlayer.this.eventListener != null) {
                VrVideoPlayer.this.eventListener.onNewFrame();
            }

        }
    }

    private class ProjectionDataListener implements SphericalV2ProjectionDataListener {
        private ProjectionDataListener() {
        }

        public void onProjectionDataChanged(int var1, byte[] var2) {
            VrVideoPlayer.this.metadata = SphericalV2MetadataParser.parse(var1, var2);
            if(VrVideoPlayer.this.videoTexturesListener != null) {
                VrVideoPlayer.this.videoTexturesListener.onVideoTexturesReady();
            }

        }
    }

    private class VideoLooperListener implements Player.EventListener {
        private VideoLooperListener() {
        }

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        public void onPlayerStateChanged(boolean var1, int var2) {
            if(var2 == 2) {
                VrVideoPlayer.this.isBuffering = true;
            } else if(var2 == 3) {
                if(VrVideoPlayer.this.isBuffering && VrVideoPlayer.this.eventListener != null) {
                    VrVideoPlayer.this.isBuffering = false;
                    VrVideoPlayer.this.eventListener.onLoadSuccess();
                }
            } else if(var1 && var2 == 4) {
                if(VrVideoPlayer.this.eventListener != null) {
                    VrVideoPlayer.this.eventListener.onCompletion();
                }

                if(VrVideoPlayer.this.isLooping) {
                    VrVideoPlayer var3 = VrVideoPlayer.this;
                    synchronized(VrVideoPlayer.this) {
                        VrVideoPlayer.this.getExoPlayer().seekTo(0L);
                    }
                }
            }

        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }
    }

    interface VideoTexturesListener {
        void onVideoTexturesReady();
    }
}
