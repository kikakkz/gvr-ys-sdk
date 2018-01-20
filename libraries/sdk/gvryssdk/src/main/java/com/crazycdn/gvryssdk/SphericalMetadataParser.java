package com.crazycdn.gvryssdk;

import android.util.Log;

import com.crazycdn.gvryssdk.anon.SphericalMetadataOuterClass;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringBufferInputStream;

/**
 * Created by yunshang on 18-1-20.
 */

public class SphericalMetadataParser {
    private static final String TAG = SphericalMetadataParser.class.getSimpleName();
    private static final boolean DEBUG = false;
    private static final String INITIAL_HEADING = "InitialViewHeadingDegrees";
    private static final String INITIAL_PITCH = "InitialViewPitchDegrees";
    private static final String INITIAL_ROLL = "InitialViewRollDegrees";
    private static final String SPHERICAL = "Spherical";
    private static final String STITCHED = "Stitched";
    private static final String STITCHING_SOFTWARE = "StitchingSoftware";
    private static final String PROJECTION_TYPE = "ProjectionType";
    private static final String STEREO_MODE = "StereoMode";
    private static final String STEREO_MODE_MONO = "mono";
    private static final String STEREO_MODE_LEFT_RIGHT = "left-right";
    private static final String STEREO_MODE_TOP_BOTTOM = "top-bottom";

    public SphericalMetadataParser() {
    }

    public static SphericalMetadataOuterClass.SphericalMetadata parse(String var0) {
        if(var0 == null) {
            return new SphericalMetadataOuterClass.SphericalMetadata();
        } else {
            try {
                XmlPullParserFactory var1 = XmlPullParserFactory.newInstance();
                var1.setNamespaceAware(true);
                XmlPullParser var2 = var1.newPullParser();
                var2.setInput(new StringBufferInputStream(var0), (String)null);
                var2.nextTag();
                return readFeed(var2);
            } catch (Exception var3) {
                Log.e(TAG, var3.getMessage());
                return new SphericalMetadataOuterClass.SphericalMetadata();
            }
        }
    }

    private static SphericalMetadataOuterClass.SphericalMetadata readFeed(XmlPullParser var0) throws XmlPullParserException, IOException {
        SphericalMetadataOuterClass.SphericalMetadata var1 = new SphericalMetadataOuterClass.SphericalMetadata();

        while(true) {
            do {
                if(var0.next() == 1) {
                    return var1;
                }
            } while(var0.getEventType() != 2);

            int var2 = 1;
            String var3 = "";

            while(var2 > 0) {
                switch(var0.next()) {
                    case 2:
                        ++var2;
                        break;
                    case 3:
                        --var2;
                        break;
                    case 4:
                        var3 = var0.getText();
                }
            }

            if(var3 != null) {
                String var4 = var0.getName();
                byte var6 = -1;
                switch(var4.hashCode()) {
                    case -1887876031:
                        if(var4.equals("InitialViewRollDegrees")) {
                            var6 = 2;
                        }
                        break;
                    case -1614191141:
                        if(var4.equals("StereoMode")) {
                            var6 = 3;
                        }
                        break;
                    case -1257448899:
                        if(var4.equals("Spherical")) {
                            var6 = 4;
                        }
                        break;
                    case -253590984:
                        if(var4.equals("StitchingSoftware")) {
                            var6 = 6;
                        }
                        break;
                    case 80888400:
                        if(var4.equals("InitialViewPitchDegrees")) {
                            var6 = 1;
                        }
                        break;
                    case 132635209:
                        if(var4.equals("ProjectionType")) {
                            var6 = 7;
                        }
                        break;
                    case 415550222:
                        if(var4.equals("InitialViewHeadingDegrees")) {
                            var6 = 0;
                        }
                        break;
                    case 1611823408:
                        if(var4.equals("Stitched")) {
                            var6 = 5;
                        }
                }

                switch(var6) {
                    case 0:
                        var1.initialViewHeadingDegrees = Integer.parseInt(var3);
                        break;
                    case 1:
                        var1.initialViewPitchDegrees = Integer.parseInt(var3);
                        break;
                    case 2:
                        var1.initialViewRollDegrees = Integer.parseInt(var3);
                        break;
                    case 3:
                        byte var8 = -1;
                        switch(var3.hashCode()) {
                            case 3357411:
                                if(var3.equals("mono")) {
                                    var8 = 2;
                                }
                                break;
                            case 1028134102:
                                if(var3.equals("left-right")) {
                                    var8 = 1;
                                }
                                break;
                            case 1736247715:
                                if(var3.equals("top-bottom")) {
                                    var8 = 0;
                                }
                        }

                        switch(var8) {
                            case 0:
                                var1.frameLayoutMode = 2;
                                break;
                            case 1:
                                Log.e(TAG, "left-right videos are unsupported");
                            case 2:
                            default:
                                var1.frameLayoutMode = 1;
                        }
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        break;
                    default:
                        String var10000 = TAG;
                        String var10002 = String.valueOf(var4);
                        String var10001;
                        if(var10002.length() != 0) {
                            var10001 = "Unknown name: ".concat(var10002);
                        } else {
                            var10001 = new String("Unknown name: ");
                        }

                        Log.w(var10000, var10001);
                }
            }
        }
    }
}
