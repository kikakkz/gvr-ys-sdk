package com.crazycdn.gvryssdk;

import android.content.Context;
import android.media.MediaCodec;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

/**
 * Created by yunshang on 18-1-18.
 */

public class SphericalV2MediaCodecVideoRenderer extends MediaCodecVideoRenderer {
    private static final int MAX_DROPPED_FRAME_COUNT_TO_NOTIFY = 1;
    private static final String TAG = SphericalV2MediaCodecVideoRenderer.class.getSimpleName();
    private final SampleTimestampBuffer sampleTimestampBuffer = new SampleTimestampBuffer();
    private SphericalV2ProjectionDataListener projectionListener;
    private Format inputFormat;

    public SphericalV2MediaCodecVideoRenderer(Context context, Handler var2, @Nullable DrmSessionManager<FrameworkMediaCrypto> var3, @Nullable VideoRendererEventListener var4, long var5) {
        super(context, MediaCodecSelector.DEFAULT, var5, var3, false, var2, var4, 1);
    }

    public void setProjectionListener(SphericalV2ProjectionDataListener var1) {
        this.projectionListener = var1;
    }

    public SampleTimestampBuffer getSampleTimestampBuffer() {
        return this.sampleTimestampBuffer;
    }

    protected void onInputFormatChanged(Format format) throws ExoPlaybackException {
        super.onInputFormatChanged(format);
        this.inputFormat = format;
        if(format != null && format.stereoMode != -1 && format.projectionData != null && this.projectionListener != null) {
            this.projectionListener.onProjectionDataChanged(format.stereoMode, format.projectionData);
        }

    }

    public Format getInputFormat() {
        return this.inputFormat;
    }

    protected void onStreamChanged(Format[] var1, long var2) throws ExoPlaybackException {
        this.sampleTimestampBuffer.setPresentationTimeOffsetUs(var2);
        super.onStreamChanged(var1, var2);
    }

    protected void renderOutputBuffer(MediaCodec var1, int var2, long var3) {
        this.sampleTimestampBuffer.addPresentationTimeUsForReleaseTimeUs(var3, System.nanoTime() / 1000L);
        super.renderOutputBuffer(var1, var2, var3);
    }

    protected void renderOutputBufferV21(MediaCodec var1, int var2, long var3, long var5) {
        this.sampleTimestampBuffer.addPresentationTimeUsForReleaseTimeUs(var3, var5 / 1000L);
        super.renderOutputBufferV21(var1, var2, var3, var5);
    }
}
