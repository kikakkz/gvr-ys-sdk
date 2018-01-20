package com.crazycdn.gvryssdk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunshang on 18-1-20.
 */

public class ProjectionScene {
    private int crc;
    private ProjectionScene.Mesh[] meshes;
    private int stereoMode;

    public ProjectionScene(int var1) {
        this.crc = var1;
        this.meshes = new ProjectionScene.Mesh[2];
    }

    public ProjectionScene.Mesh getLeftMesh() {
        return this.meshes[0];
    }

    public void setLeftMesh(ProjectionScene.Mesh var1) {
        this.meshes[0] = var1;
    }

    public ProjectionScene.Mesh getRightMesh() {
        return this.meshes[1];
    }

    public void setRightMesh(ProjectionScene.Mesh var1) {
        this.meshes[1] = var1;
    }

    public int getCrc() {
        return this.crc;
    }

    public int getStereoMode() {
        return this.stereoMode;
    }

    public void setStereoMode(int var1) {
        this.stereoMode = var1;
    }

    public static class Mesh {
        private List<ProjectionScene.SubMesh> subMeshes = new ArrayList();

        public Mesh() {
        }

        public void addSubMesh(ProjectionScene.SubMesh var1) {
            this.subMeshes.add(var1);
        }

        public int getSubMeshCount() {
            return this.subMeshes.size();
        }

        public ProjectionScene.SubMesh getSubMesh(int var1) {
            return (ProjectionScene.SubMesh)this.subMeshes.get(var1);
        }
    }

    public static class SubMesh {
        private final int mode;
        private final float[] vertexBuffer;
        private final float[] textureCoordBuffer;
        private final int[] vertexIndices;

        public SubMesh(float[] var1, float[] var2, int[] var3, int var4) {
            this.vertexBuffer = var1;
            this.textureCoordBuffer = var2;
            this.vertexIndices = var3;
            this.mode = var4;
        }

        public int getMode() {
            return this.mode;
        }

        public float[] getVertexBuffer() {
            return this.vertexBuffer;
        }

        public float[] getTextureCoordBuffer() {
            return this.textureCoordBuffer;
        }

        public int[] getVertexIndices() {
            return this.vertexIndices;
        }

        public float[] getVertices() {
            return this.expandBuffer(this.vertexBuffer, 3);
        }

        public float[] getTextureCoords() {
            return this.expandBuffer(this.textureCoordBuffer, 2);
        }

        private float[] expandBuffer(float[] var1, int var2) {
            int var3 = this.vertexIndices.length;
            float[] var4 = new float[var3 * var2];

            for(int var5 = 0; var5 < var3; ++var5) {
                int var6 = var5 * var2;
                int var7 = this.vertexIndices[var5] * var2;

                for(int var8 = 0; var8 < var2; ++var8) {
                    var4[var6 + var8] = var1[var7 + var8];
                }
            }

            return var4;
        }
    }
}
