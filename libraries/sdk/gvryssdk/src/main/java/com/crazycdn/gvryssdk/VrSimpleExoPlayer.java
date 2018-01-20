package com.crazycdn.gvryssdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.util.ArrayList;

/**
 * Created by yunshang on 18-1-18.
 */

public class VrSimpleExoPlayer extends SimpleExoPlayer {
    private final CameraMotionMetadataRendererV2 cameraMotionRenderer;
    private final SphericalV2MediaCodecVideoRenderer videoRenderer;

    public VrSimpleExoPlayer(Context context, DefaultBandwidthMeter bandwidthMeter) {

        super(new VrSimpleExoPlayer.VrRenderersFactory(context),
                new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter)),
                new DefaultLoadControl());
        CameraMotionMetadataRendererV2 var2 = null;
        SphericalV2MediaCodecVideoRenderer var3 = null;
        Renderer[] var4 = this.renderers;
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Renderer var7 = var4[var6];
            if(var7 instanceof CameraMotionMetadataRendererV2) {
                var2 = (CameraMotionMetadataRendererV2)var7;
            } else if(var7 instanceof SphericalV2MediaCodecVideoRenderer) {
                var3 = (SphericalV2MediaCodecVideoRenderer)var7;
            }
        }

        this.cameraMotionRenderer = var2;
        this.videoRenderer = var3;
    }

    public FrameRotationBuffer getFrameRotationBuffer() {
        return this.cameraMotionRenderer.getFrameRotationBuffer();
    }

    public void setProjectionListener(SphericalV2ProjectionDataListener var1) {
        this.videoRenderer.setProjectionListener(var1);
    }

    public long getSampleTimestampUsForReleaseTimeUs(long var1) {
        return this.videoRenderer.getSampleTimestampBuffer().getSampleTimestampUsForReleaseTimeUs(var1);
    }

    public Format getInputFormat() {
        return this.videoRenderer.getInputFormat();
    }

    private static class VrRenderersFactory extends DefaultRenderersFactory {
        public VrRenderersFactory(Context var1) {
            super(var1);
        }

        @Override
        protected void buildVideoRenderers(Context context,
                                           @Nullable DrmSessionManager<FrameworkMediaCrypto> drmSessionManager,
                                           long allowedVideoJoiningTimeMs, Handler eventHandler,
                                           VideoRendererEventListener eventListener, @ExtensionRendererMode int extensionRendererMode,
                                           ArrayList<Renderer> out) {
            out.add(new SphericalV2MediaCodecVideoRenderer(context, eventHandler, drmSessionManager, eventListener, allowedVideoJoiningTimeMs));
        }

        protected void buildMetadataRenderers(Context context, MetadataOutput output, Looper outputLooper,
                                              @ExtensionRendererMode int extensionRendererMode, ArrayList<Renderer> out) {
            super.buildMetadataRenderers(context, output, outputLooper, extensionRendererMode, out);
            out.add(new CameraMotionMetadataRendererV2(MotionCoordinateSystem.FLIP_XY));
        }
    }
}
