package com.crazycdn.gvryssdk;

import com.crazycdn.gvryssdk.anon.SphericalMetadataOuterClass;

/**
 * Created by yunshang on 18-1-20.
 */

public class SphericalV2MetadataParser {
    private static final String TAG = SphericalV2MetadataParser.class.getSimpleName();

    public SphericalV2MetadataParser() {
    }

    public static SphericalMetadataOuterClass.SphericalMetadata parse(int var0, byte[] var1) {
        ProjectionMeshDecoderV2 var2 = new ProjectionMeshDecoderV2();
        ProjectionScene var3 = var2.decode(var1);
        SphericalMetadataOuterClass.SphericalMetadata var4 = new SphericalMetadataOuterClass.SphericalMetadata();
        switch(var0) {
            case 0:
                var4.frameLayoutMode = 1;
                break;
            case 1:
                var4.frameLayoutMode = 2;
                break;
            case 2:
                var4.frameLayoutMode = 3;
                break;
            default:
                throw new IllegalArgumentException((new StringBuilder(33)).append("Unexpected stereoMode ").append(var0).toString());
        }

        if(var3 != null) {
            var4.mesh = new SphericalMetadataOuterClass.StereoMeshConfig();
            var4.mesh.leftEyeMesh = createMesh(var3.getLeftMesh());
            var4.mesh.rightEyeMesh = createMesh(var3.getRightMesh());
        }

        return var4;
    }

    private static SphericalMetadataOuterClass.StereoMeshConfig.Mesh createMesh(ProjectionScene.Mesh var0) {
        if(var0 == null) {
            return null;
        } else if(var0.getSubMeshCount() != 1) {
            throw new IllegalArgumentException("There should be only a single submesh");
        } else {
            ProjectionScene.SubMesh var1 = var0.getSubMesh(0);
            SphericalMetadataOuterClass.StereoMeshConfig.Mesh var2 = new SphericalMetadataOuterClass.StereoMeshConfig.Mesh();
            switch(var1.getMode()) {
                case 4:
                    var2.geometryType = 0;
                    break;
                case 5:
                    var2.geometryType = 1;
                    break;
                default:
                    int var8 = var1.getMode();
                    throw new IllegalArgumentException((new StringBuilder(32)).append("Unexpected mesh mode ").append(var8).toString());
            }

            float[] var3 = var1.getVertices();
            float[] var4 = var1.getTextureCoords();
            int var5 = var3.length / 3;
            var2.vertices = new SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex[var5];

            for(int var6 = 0; var6 < var5; ++var6) {
                SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex var7 = new SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex();
                var2.vertices[var6] = var7;
                var7.x = var3[3 * var6];
                var7.y = var3[3 * var6 + 1];
                var7.z = var3[3 * var6 + 2];
                var7.u = var4[2 * var6];
                var7.v = var4[2 * var6 + 1];
            }

            return var2;
        }
    }
}
