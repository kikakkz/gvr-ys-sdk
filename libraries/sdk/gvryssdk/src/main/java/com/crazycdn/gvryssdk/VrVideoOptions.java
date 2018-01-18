package com.crazycdn.gvryssdk;

import android.util.Log;

import com.google.android.exoplayer2.C;

/**
 * Created by yunshang on 18-1-18.
 */

public class VrVideoOptions {
    private static final String TAG = "VrVideoOptions";
    private static final int FORMAT_START_MARKER = 0;
    private static final int FORMAT_END_MARKER = 4;
    public @C.ContentType int inputFormat = C.TYPE_OTHER;
    private static final int TYPE_START_MARKER = 0;
    public static final int TYPE_MONO = 1;
    public static final int TYPE_STEREO_OVER_UNDER = 2;
    private static final int TYPE_END_MARKER = 3;
    public int inputType = 1;

    public VrVideoOptions() {
    }

    public void validate() {
        String var10000;
        int var1;
        if(this.inputFormat <= 0 || this.inputFormat >= 4) {
            var1 = this.inputFormat;
            Log.e(TAG, (new StringBuilder(40)).append("Invalid Options.inputFormat: ").append(var1).toString());
            this.inputFormat = 1;
        }

        if(this.inputType <= 0 || this.inputType >= 3) {
            var1 = this.inputType;
            Log.e(TAG, (new StringBuilder(38)).append("Invalid Options.inputType: ").append(var1).toString());
            this.inputType = 1;
        }

    }
}
