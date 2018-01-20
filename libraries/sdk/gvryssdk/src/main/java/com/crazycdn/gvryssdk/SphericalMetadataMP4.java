package com.crazycdn.gvryssdk;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by yunshang on 18-1-20.
 */

public final class SphericalMetadataMP4 {
    private static final String TAG = "SphericalMetadataMP4";
    private static final String[] METADATA_PATH = new String[]{"moov", "trak"};
    private static final String METADATA_ATOM = "uuid";
    private static final byte[] SPHERICAL_UUID = new byte[]{-1, -52, -126, 99, -8, 85, 74, -109, -120, 20, 88, 122, 2, 82, 31, -35};
    private static final String METADATA_ATOM_CHARSET = "UTF-8";
    private static final long MIN_ATOM_SIZE = 8L;
    private static final long ATOM_SIZE_64_BITS = 1L;

    public SphericalMetadataMP4() {
    }

    public static String extract(InputStream var0) {
        if(var0 == null) {
            Log.e("SphericalMetadataMP4", "Failed to extract metadata string from mp4: no stream!");
            return null;
        } else {
            try {
                return extract(var0, 0, 2147483647L);
            } catch (Exception var2) {
                Log.e("SphericalMetadataMP4", "Failed to extract metadata string from mp4.", var2);
                return null;
            }
        }
    }

    private static String extract(InputStream var0, int var1, long var2) throws IOException {
        if(var1 > METADATA_PATH.length) {
            throw new IllegalArgumentException("Search depth exceeds expectations.");
        } else {
            int var4 = 0;

            label91:
            while((long)var4 < var2 || var1 == 0) {
                byte[] var5 = readBytes(var0, 4);
                var4 += 4;
                if(var5 != null) {
                    long var6 = bytesToInt(var5, 4);
                    if(var6 < 8L && var6 != 1L) {
                        throw new IOException((new StringBuilder(39)).append("Invalid atom size: ").append(var6).toString());
                    }

                    byte[] var8 = readBytes(var0, 4);
                    var4 += 4;
                    if(var8 == null) {
                        throw new IOException("Unexpected end of stream.");
                    }

                    String var9 = bytesToString(var8, "UTF-8");
                    if(var9 != null && var9.length() == 4) {
                        long var10 = var6 - 8L;
                        if(var6 == 1L) {
                            var5 = readBytes(var0, 8);
                            var4 += 8;
                            if(var5 == null) {
                                throw new IOException("Unexpected end of stream.");
                            }

                            var6 = bytesToInt(var5, 8);
                            var10 = var6 - 16L;
                            if(var6 < 8L) {
                                throw new IOException((new StringBuilder(39)).append("Invalid atom size: ").append(var6).toString());
                            }
                        }

                        if(var1 != METADATA_PATH.length && METADATA_PATH[var1].equals(var9)) {
                            String var15 = extract(var0, var1 + 1, var6 - 8L);
                            if(var15 != null) {
                                return var15;
                            }

                            var4 = (int)((long)var4 + var10);
                            var10 = 0L;
                        } else if(var1 == METADATA_PATH.length && "uuid".equals(var9)) {
                            byte[] var12 = readBytes(var0, SPHERICAL_UUID.length);
                            var4 += SPHERICAL_UUID.length;
                            var10 -= (long)SPHERICAL_UUID.length;
                            if(var12 == null) {
                                throw new IOException("Failed to parse UUID.");
                            }

                            if(Arrays.equals(var12, SPHERICAL_UUID)) {
                                byte[] var13 = readBytes(var0, (int)var10);
                                String var14 = bytesToString(var13, "UTF-8");
                                if(var14 == null) {
                                    throw new IOException("Error retrieving metadata xml.");
                                }

                                String var10002 = String.valueOf(var14);
                                String var10001;
                                if(var10002.length() != 0) {
                                    var10001 = "Located spherical metadata:\n".concat(var10002);
                                } else {
                                    var10001 = new String("Located spherical metadata:\n");
                                }

                                Log.i("SphericalMetadataMP4", var10001);
                                return var14;
                            }
                        }

                        while(true) {
                            if(var10 <= 0L) {
                                continue label91;
                            }

                            long var16 = var0.skip(var10);
                            if(var16 > 0L) {
                                var10 -= var16;
                                var4 = (int)((long)var4 + var16);
                            }
                        }
                    }

                    throw new IOException("Invalid atom name.");
                }

                if(var1 != 0) {
                    throw new IOException((new StringBuilder(69)).append("Unexpected end of stream.").append(var4).append(" ").append(var2).append(" ").append(var1).toString());
                }
                break;
            }

            return null;
        }
    }

    private static byte[] readBytes(InputStream var0, int var1) throws IOException {
        int var2 = 0;

        byte[] var3;
        int var4;
        for(var3 = new byte[var1]; var2 < var1; var2 += var4) {
            var4 = var0.read(var3, var2, var1 - var2);
            if(var4 == -1) {
                return null;
            }
        }

        return var3;
    }

    private static long bytesToInt(byte[] var0, int var1) {
        if(var0 != null && var0.length == var1 && var1 <= 8) {
            int var2 = 0;

            for(int var3 = 0; var3 < var1; ++var3) {
                var2 |= (var0[var3] & 255) << 8 * (var1 - 1 - var3);
            }

            return (long)var2;
        } else {
            throw new IllegalArgumentException("Invalid byte array.");
        }
    }

    private static String bytesToString(byte[] var0, String var1) {
        if(var0 == null) {
            throw new IllegalArgumentException("Null byte array.");
        } else {
            try {
                return new String(var0, var1);
            } catch (IOException var3) {
                throw new IllegalArgumentException("Invalid charset.");
            }
        }
    }
}
