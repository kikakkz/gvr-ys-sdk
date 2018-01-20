package com.crazycdn.gvryssdk;

import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.util.ParsableByteArray;

import java.io.IOException;

/**
 * Created by yunshang on 18-1-18.
 */

public class CameraMotionMetadataRendererV2 extends BaseRenderer {
    private static final int SAMPLE_WINDOW_DURATION_US = 100000;
    private final FormatHolder formatHolder = new FormatHolder();
    private final DecoderInputBuffer buffer = new DecoderInputBuffer(1);
    private final MotionCoordinateSystem coordinateSystem;
    private volatile FrameRotationBuffer frameRotationBuffer;

    public CameraMotionMetadataRendererV2(MotionCoordinateSystem var1) {
        super(4);
        this.coordinateSystem = var1;
    }

    public FrameRotationBuffer getFrameRotationBuffer() {
        return this.frameRotationBuffer;
    }

    protected void onDisabled() {
        this.frameRotationBuffer = null;
    }

    public void render(long var1, long var3) throws ExoPlaybackException {
        if(this.frameRotationBuffer == null) {
            this.frameRotationBuffer = new FrameRotationBuffer(this.coordinateSystem);
        }

        do {
            if(this.hasReadStreamToEnd()) {
                return;
            }

            this.buffer.clear();
            int var5 = this.readSource(this.formatHolder, this.buffer, false);
            if(var5 != -4 || this.buffer.isEndOfStream()) {
                return;
            }

            try {
                this.buffer.flip();
                float[] var6 = parseMetadata(this.buffer.data.array(), this.buffer.data.limit());
                if(var6 != null) {
                    this.frameRotationBuffer.setRotation(this.buffer.timeUs, var6);
                }
            } catch (IOException var7) {
                throw ExoPlaybackException.createForRenderer(var7, this.getIndex());
            }
        } while(this.buffer.timeUs <= var1 + 100000L);

    }

    public boolean isEnded() {
        return this.hasReadStreamToEnd();
    }

    public boolean isReady() {
        return true;
    }

    private static float[] parseMetadata(byte[] var0, int var1) throws IOException {
        ParsableByteArray var2 = new ParsableByteArray(var0, var1);
        if(var2.readLittleEndianInt() != 0) {
            return null;
        } else {
            float[] var3 = new float[]{Float.intBitsToFloat(var2.readLittleEndianInt()),
                    Float.intBitsToFloat(var2.readLittleEndianInt()),
                    Float.intBitsToFloat(var2.readLittleEndianInt())};
            return var3;
        }
    }

    @Override
    public int supportsFormat(Format format) throws ExoPlaybackException {
        return format.sampleMimeType.equals("application/x-camera-motion")?4:0;
    }
}
