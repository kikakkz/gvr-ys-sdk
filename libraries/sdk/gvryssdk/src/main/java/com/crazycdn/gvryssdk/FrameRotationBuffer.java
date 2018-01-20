package com.crazycdn.gvryssdk;

import android.opengl.Matrix;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Created by yunshang on 18-1-20.
 */

public class FrameRotationBuffer {
    private static final String TAG = "FrameRotationBuffer";
    private final float[] deviceTransform = new float[16];
    private final float[] recenterTransform = new float[16];
    private final float[] outputTransform = new float[16];
    private final float[] tempTransform = new float[16];
    private final NavigableMap<Long, Vector<Float>> rotations = new TreeMap();
    private final MotionCoordinateSystem coordinateSystem;

    public FrameRotationBuffer(MotionCoordinateSystem var1) {
        this.coordinateSystem = var1;
        Matrix.setIdentityM(this.outputTransform, 0);
        Matrix.setIdentityM(this.recenterTransform, 0);
    }

    public synchronized void setRotation(long var1, float[] var3) {
        Vector var4 = new Vector();
        this.transformRotationForOutputCoordinateSystem(var3, var4);
        if(this.rotations.size() == 0) {
            this.computeRecenterTransform(var4);
        }

        this.rotations.put(Long.valueOf(var1), var4);
    }

    public synchronized float[] getTransform(long var1) {
        Map.Entry var3 = this.rotations.floorEntry(Long.valueOf(var1));
        if(var3 == null) {
            return this.outputTransform;
        } else {
            this.rotations.headMap((Long)var3.getKey()).clear();
            angleAxisToMatrix((Vector)var3.getValue(), this.deviceTransform);
            Matrix.multiplyMM(this.outputTransform, 0, this.recenterTransform, 0, this.deviceTransform, 0);
            return this.outputTransform;
        }
    }

    private void transformRotationForOutputCoordinateSystem(float[] var1, Vector<Float> var2) {
        switch(this.coordinateSystem) {
            case FLIP_XY:
                var2.add(Float.valueOf(-var1[0]));
                var2.add(Float.valueOf(-var1[1]));
                var2.add(Float.valueOf(var1[2]));
                break;
            case FLIP_YZ:
                var2.add(Float.valueOf(var1[0]));
                var2.add(Float.valueOf(-var1[1]));
                var2.add(Float.valueOf(-var1[2]));
        }

    }

    private void computeRecenterTransform(Vector<Float> var1) {
        angleAxisToMatrix(var1, this.tempTransform);
        Matrix.setIdentityM(this.recenterTransform, 0);
        float var2 = (float)Math.sqrt((double)(this.tempTransform[10] * this.tempTransform[10] + this.tempTransform[8] * this.tempTransform[8]));
        this.recenterTransform[0] = this.tempTransform[10] / var2;
        this.recenterTransform[2] = this.tempTransform[8] / var2;
        this.recenterTransform[8] = -this.tempTransform[8] / var2;
        this.recenterTransform[10] = this.tempTransform[10] / var2;
    }

    private static void angleAxisToMatrix(Vector<Float> var0, float[] var1) {
        float var2 = ((Float)var0.get(0)).floatValue();
        float var3 = ((Float)var0.get(1)).floatValue();
        float var4 = ((Float)var0.get(2)).floatValue();
        float var5 = Matrix.length(var2, var3, var4);
        if(var5 != 0.0F) {
            float var6 = (float)Math.toDegrees((double)var5);
            Matrix.setRotateM(var1, 0, var6, var2 / var5, var3 / var5, var4 / var5);
        } else {
            Matrix.setIdentityM(var1, 0);
        }

    }
}