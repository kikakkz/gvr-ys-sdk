package com.crazycdn.gvryssdk;

import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * Created by yunshang on 18-1-20.
 */

public class ProjectionMeshDecoderV2 {
    private static final int TYPE_YTMP = Util.getIntegerCodeForString("ytmp");
    private static final int TYPE_MSHP = Util.getIntegerCodeForString("mshp");
    private static final int TYPE_RAW = Util.getIntegerCodeForString("raw ");
    private static final int TYPE_DFL8 = Util.getIntegerCodeForString("dfl8");
    private static final int TYPE_MESH = Util.getIntegerCodeForString("mesh");
    private static final int TYPE_PROJ = Util.getIntegerCodeForString("proj");
    private static final int MAX_MESH_COUNT = 2;
    private static final int MAX_COORDINATE_COUNT = 10000;
    private static final int MAX_VERTEX_COUNT = 32000;
    private static final int MAX_TRIANGLE_INDICES = 128000;
    private static final int INITIAL_DECOMPRESS_SIZE = 100000;
    private List<ProjectionScene> sceneList = new ArrayList();
    private boolean error;
    private ProjectionScene scene;
    private int meshCount;
    private boolean ytmpParsed;

    public ProjectionMeshDecoderV2() {
    }

    public ProjectionScene decode(byte[] var1) {
        if(var1 == null) {
            return null;
        } else {
            this.error = false;
            this.ytmpParsed = false;
            this.scene = null;
            ParsableByteArray var2 = new ParsableByteArray(var1);
            return this.isProj(var2)?this.parseProj(var2):this.parseYtmp(var2, var2.capacity());
        }
    }

    private boolean isProj(ParsableByteArray var1) {
        int var2 = var1.getPosition();
        var1.setPosition(0);
        var1.skipBytes(4);
        int var3 = var1.readInt();
        var1.setPosition(var2);
        return var3 == TYPE_PROJ;
    }

    private ProjectionScene parseProj(ParsableByteArray var1) {
        var1.skipBytes(8);

        int var3;
        for(int var2 = var1.getPosition(); var2 < var1.capacity() && !this.error; var2 += var3) {
            var1.setPosition(var2);
            var3 = var1.readInt();
            if(var3 == 0) {
                return null;
            }

            int var4 = var1.readInt();
            if(var4 == TYPE_YTMP || var4 == TYPE_MSHP) {
                if(this.ytmpParsed) {
                    return null;
                }

                ProjectionScene var5 = this.parseYtmp(var1, var3 + var2);
                if(this.error) {
                    return null;
                }

                if(var5 != null) {
                    return var5;
                }
            }
        }

        return null;
    }

    private boolean readFromCache(int var1) {
        for(int var2 = 0; var2 < this.sceneList.size(); ++var2) {
            if(((ProjectionScene)this.sceneList.get(var2)).getCrc() == var1) {
                this.scene = (ProjectionScene)this.sceneList.get(var2);
                return true;
            }
        }

        return false;
    }

    private ProjectionScene parseYtmp(ParsableByteArray var1, int var2) {
        int var3 = var1.readUnsignedByte();
        var1.skipBytes(3);
        if(var3 == 0) {
            int var4 = var1.readInt();
            if(this.readFromCache(var4)) {
                return this.scene;
            }

            int var5 = var1.readInt();
            if(var5 == TYPE_RAW) {
                this.parseRawYtmpData(var1, var2, var4);
                this.ytmpParsed = true;
            } else if(var5 == TYPE_DFL8) {
                int[] var6 = new int[1];
                byte[] var7 = inflate(var1.data, var1.getPosition(), var2 - var1.getPosition(), var6);
                if(var7 == null) {
                    return null;
                }

                ParsableByteArray var8 = new ParsableByteArray(var7, var6[0]);
                this.parseRawYtmpData(var8, var6[0], var4);
                this.ytmpParsed = true;
            } else {
                this.error = true;
            }

            if(this.error) {
                return null;
            }

            if(this.scene.getLeftMesh() != null) {
                this.sceneList.add(this.scene);
                return this.scene;
            }
        }

        return null;
    }

    private void parseRawYtmpData(ParsableByteArray var1, int var2, int var3) {
        this.meshCount = 0;
        int var4 = var1.getPosition();

        int var5;
        for(this.scene = new ProjectionScene(var3); var4 < var2 && !this.error; var4 += var5) {
            var1.setPosition(var4);
            var5 = var1.readInt();
            if(var5 == 0) {
                this.error = true;
                return;
            }

            int var6 = var1.readInt();
            if(var6 == TYPE_MESH) {
                if(this.meshCount >= 2) {
                    this.error = true;
                    return;
                }

                this.parseMesh(var1);
                ++this.meshCount;
            }
        }

    }

    private void parseMesh(ParsableByteArray var1) {
        int var2 = var1.readInt();
        if(var2 > 10000) {
            this.error = true;
        } else {
            float[] var3 = new float[var2];

            int var4;
            for(var4 = 0; var4 < var2; ++var4) {
                var3[var4] = var1.readFloat();
            }

            var4 = var1.readInt();
            if(var4 > 32000) {
                this.error = true;
            } else {
                double var5 = Math.log(2.0D);
                int var7 = (int)Math.ceil(Math.log(2.0D * (double)var2) / var5);
                int var8 = 0;
                int var9 = 0;
                int var10 = 0;
                int var11 = 0;
                int var12 = 0;
                ParsableBitArray var13 = new ParsableBitArray(var1.data);
                var13.setPosition(var1.getPosition() * 8);
                float[] var14 = new float[var4 * 3];
                float[] var15 = new float[var4 * 2];

                int var16;
                int var17;
                int var18;
                for(var16 = 0; var16 < var4; ++var16) {
                    var8 += decodeZigZag(var13.readBits(var7));
                    var9 += decodeZigZag(var13.readBits(var7));
                    var10 += decodeZigZag(var13.readBits(var7));
                    var11 += decodeZigZag(var13.readBits(var7));
                    var12 += decodeZigZag(var13.readBits(var7));
                    if(isVertexInvalid(var8, var9, var10, var11, var12, var2)) {
                        this.error = true;
                        return;
                    }

                    var17 = var16 * 3;
                    var14[var17 + 0] = var3[var8];
                    var14[var17 + 1] = var3[var9];
                    var14[var17 + 2] = var3[var10];
                    var18 = var16 * 2;
                    var15[var18 + 0] = var3[var11];
                    var15[var18 + 1] = var3[var12];
                }

                var13.setPosition(var13.bitsLeft() + 7 & -8);
                var13.readBits(32);
                var13.readBits(8);
                var16 = var13.readBits(8);
                var17 = var13.readBits(32);
                if(var17 > 128000) {
                    this.error = true;
                } else {
                    var18 = (int)Math.ceil(Math.log(2.0D * (double)var4) / var5);
                    int[] var19 = new int[var17];
                    int var20 = 0;

                    for(int var21 = 0; var21 < var17; ++var21) {
                        var20 += decodeZigZag(var13.readBits(var18));
                        if(var20 >= var4) {
                            this.error = true;
                            return;
                        }

                        var19[var21] = var20;
                    }

                    byte var24 = 4;
                    switch(var16) {
                        case 1:
                            var24 = 5;
                            break;
                        case 2:
                            var24 = 6;
                    }

                    ProjectionScene.Mesh var22 = new ProjectionScene.Mesh();
                    ProjectionScene.SubMesh var23 = new ProjectionScene.SubMesh(var14, var15, var19, var24);
                    var22.addSubMesh(var23);
                    if(this.meshCount == 0) {
                        this.scene.setLeftMesh(var22);
                    } else if(this.meshCount == 1) {
                        this.scene.setRightMesh(var22);
                    }

                }
            }
        }
    }

    private static int decodeZigZag(int var0) {
        return var0 >> 1 ^ -(var0 & 1);
    }

    private static boolean isVertexInvalid(int var0, int var1, int var2, int var3, int var4, int var5) {
        return Math.max(Math.max(var0, var1), Math.max(var2, var3)) >= var5 || var4 >= var5;
    }

    static byte[] inflate(byte[] var0, int var1, int var2, int[] var3) {
        Inflater var4 = new Inflater(true);
        var4.setInput(var0, var1, var2);
        int var5 = 100000;
        byte[] var6 = new byte[var5];
        int var7 = 0;
        boolean var8 = false;

        try {
            do {
                var7 += var4.inflate(var6, var7, var5 - var7);
                var8 = !var4.needsInput();
                if(var8) {
                    var6 = resizeBuffer(var6);
                    var5 = var6.length;
                }
            } while(var8);
        } catch (DataFormatException var10) {
            return null;
        }

        var3[0] = var7;
        return var6;
    }

    private static byte[] resizeBuffer(byte[] var0) {
        byte[] var1 = new byte[var0.length * 2];
        System.arraycopy(var0, 0, var1, 0, var0.length);
        return var1;
    }
}
