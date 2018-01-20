package com.crazycdn.gvryssdk.anon;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.ExtendableMessageNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.NanoEnumValue;
import com.google.protobuf.nano.WireFormatNano;

import java.io.IOException;

/**
 * Created by yunshang on 18-1-20.
 */

public abstract class SphericalMetadataOuterClass {
    private SphericalMetadataOuterClass() {
    }

    public static final class SphericalMetadata extends ExtendableMessageNano<SphericalMetadataOuterClass.SphericalMetadata> {
        private static volatile SphericalMetadataOuterClass.SphericalMetadata[] _emptyArray;
        public int initialViewHeadingDegrees;
        public int initialViewPitchDegrees;
        public int initialViewRollDegrees;
        @NanoEnumValue(
                value = SphericalMetadataOuterClass.SphericalMetadata.FrameLayoutMode.class,
                legacy = false
        )
        public int frameLayoutMode;
        public SphericalMetadataOuterClass.StereoMeshConfig mesh;

        @NanoEnumValue(
                value = SphericalMetadataOuterClass.SphericalMetadata.FrameLayoutMode.class,
                legacy = false
        )
        public static int checkFrameLayoutModeOrThrow(int var0) {
            switch(var0) {
                case 1:
                case 2:
                case 3:
                case 4:
                    return var0;
                default:
                    throw new IllegalArgumentException((new StringBuilder(47)).append(var0).append(" is not a valid enum FrameLayoutMode").toString());
            }
        }

        @NanoEnumValue(
                value = SphericalMetadataOuterClass.SphericalMetadata.FrameLayoutMode.class,
                legacy = false
        )
        public static int[] checkFrameLayoutModeOrThrow(int[] var0) {
            int[] var1 = var0;
            int var2 = var0.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                int var4 = var1[var3];
                checkFrameLayoutModeOrThrow(var4);
            }

            return var0;
        }

        public static SphericalMetadataOuterClass.SphericalMetadata[] emptyArray() {
            if(_emptyArray == null) {
                Object var0 = InternalNano.LAZY_INIT_LOCK;
                synchronized(InternalNano.LAZY_INIT_LOCK) {
                    if(_emptyArray == null) {
                        _emptyArray = new SphericalMetadataOuterClass.SphericalMetadata[0];
                    }
                }
            }

            return _emptyArray;
        }

        public SphericalMetadata() {
            this.clear();
        }

        public SphericalMetadataOuterClass.SphericalMetadata clear() {
            this.initialViewHeadingDegrees = 0;
            this.initialViewPitchDegrees = 0;
            this.initialViewRollDegrees = 0;
            this.frameLayoutMode = 1;
            this.mesh = null;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano var1) throws IOException {
            if(this.initialViewHeadingDegrees != 0) {
                var1.writeInt32(1, this.initialViewHeadingDegrees);
            }

            if(this.initialViewPitchDegrees != 0) {
                var1.writeInt32(2, this.initialViewPitchDegrees);
            }

            if(this.initialViewRollDegrees != 0) {
                var1.writeInt32(3, this.initialViewRollDegrees);
            }

            if(this.frameLayoutMode != 1) {
                var1.writeInt32(4, this.frameLayoutMode);
            }

            if(this.mesh != null) {
                var1.writeMessage(5, this.mesh);
            }

            super.writeTo(var1);
        }

        protected int computeSerializedSize() {
            int var1 = super.computeSerializedSize();
            if(this.initialViewHeadingDegrees != 0) {
                var1 += CodedOutputByteBufferNano.computeInt32Size(1, this.initialViewHeadingDegrees);
            }

            if(this.initialViewPitchDegrees != 0) {
                var1 += CodedOutputByteBufferNano.computeInt32Size(2, this.initialViewPitchDegrees);
            }

            if(this.initialViewRollDegrees != 0) {
                var1 += CodedOutputByteBufferNano.computeInt32Size(3, this.initialViewRollDegrees);
            }

            if(this.frameLayoutMode != 1) {
                var1 += CodedOutputByteBufferNano.computeInt32Size(4, this.frameLayoutMode);
            }

            if(this.mesh != null) {
                var1 += CodedOutputByteBufferNano.computeMessageSize(5, this.mesh);
            }

            return var1;
        }

        public SphericalMetadataOuterClass.SphericalMetadata mergeFrom(CodedInputByteBufferNano var1) throws IOException {
            while(true) {
                int var2 = var1.readTag();
                switch(var2) {
                    case 0:
                        return this;
                    case 8:
                        this.initialViewHeadingDegrees = var1.readInt32();
                        break;
                    case 16:
                        this.initialViewPitchDegrees = var1.readInt32();
                        break;
                    case 24:
                        this.initialViewRollDegrees = var1.readInt32();
                        break;
                    case 32:
                        int var3 = var1.getPosition();

                        try {
                            this.frameLayoutMode = checkFrameLayoutModeOrThrow(var1.readInt32());
                        } catch (IllegalArgumentException var5) {
                            var1.rewindToPosition(var3);
                            this.storeUnknownField(var1, var2);
                        }
                        break;
                    case 42:
                        if(this.mesh == null) {
                            this.mesh = new SphericalMetadataOuterClass.StereoMeshConfig();
                        }

                        var1.readMessage(this.mesh);
                        break;
                    default:
                        if(!super.storeUnknownField(var1, var2)) {
                            return this;
                        }
                }
            }
        }

        public static SphericalMetadataOuterClass.SphericalMetadata parseFrom(byte[] var0) throws InvalidProtocolBufferNanoException {
            return (SphericalMetadataOuterClass.SphericalMetadata) MessageNano.mergeFrom(new SphericalMetadataOuterClass.SphericalMetadata(), var0);
        }

        public static SphericalMetadataOuterClass.SphericalMetadata parseFrom(CodedInputByteBufferNano var0) throws IOException {
            return (new SphericalMetadataOuterClass.SphericalMetadata()).mergeFrom(var0);
        }

        public interface FrameLayoutMode {
            @NanoEnumValue(
                    value = SphericalMetadataOuterClass.SphericalMetadata.FrameLayoutMode.class,
                    legacy = false
            )
            int STEREO_FRAME_LAYOUT_MONO = 1;
            @NanoEnumValue(
                    value = SphericalMetadataOuterClass.SphericalMetadata.FrameLayoutMode.class,
                    legacy = false
            )
            int STEREO_FRAME_LAYOUT_TOP_BOTTOM = 2;
            @NanoEnumValue(
                    value = SphericalMetadataOuterClass.SphericalMetadata.FrameLayoutMode.class,
                    legacy = false
            )
            int STEREO_FRAME_LAYOUT_LEFT_RIGHT = 3;
            @NanoEnumValue(
                    value = SphericalMetadataOuterClass.SphericalMetadata.FrameLayoutMode.class,
                    legacy = false
            )
            int STEREO_FRAME_LAYOUT_STEREO_CUSTOM = 4;
        }
    }

    public static final class StereoMeshConfig extends ExtendableMessageNano<SphericalMetadataOuterClass.StereoMeshConfig> {
        private static volatile SphericalMetadataOuterClass.StereoMeshConfig[] _emptyArray;
        public SphericalMetadataOuterClass.StereoMeshConfig.Mesh leftEyeMesh;
        public SphericalMetadataOuterClass.StereoMeshConfig.Mesh rightEyeMesh;

        public static SphericalMetadataOuterClass.StereoMeshConfig[] emptyArray() {
            if(_emptyArray == null) {
                Object var0 = InternalNano.LAZY_INIT_LOCK;
                synchronized(InternalNano.LAZY_INIT_LOCK) {
                    if(_emptyArray == null) {
                        _emptyArray = new SphericalMetadataOuterClass.StereoMeshConfig[0];
                    }
                }
            }

            return _emptyArray;
        }

        public StereoMeshConfig() {
            this.clear();
        }

        public SphericalMetadataOuterClass.StereoMeshConfig clear() {
            this.leftEyeMesh = null;
            this.rightEyeMesh = null;
            this.unknownFieldData = null;
            this.cachedSize = -1;
            return this;
        }

        public void writeTo(CodedOutputByteBufferNano var1) throws IOException {
            if(this.leftEyeMesh != null) {
                var1.writeMessage(1, this.leftEyeMesh);
            }

            if(this.rightEyeMesh != null) {
                var1.writeMessage(2, this.rightEyeMesh);
            }

            super.writeTo(var1);
        }

        protected int computeSerializedSize() {
            int var1 = super.computeSerializedSize();
            if(this.leftEyeMesh != null) {
                var1 += CodedOutputByteBufferNano.computeMessageSize(1, this.leftEyeMesh);
            }

            if(this.rightEyeMesh != null) {
                var1 += CodedOutputByteBufferNano.computeMessageSize(2, this.rightEyeMesh);
            }

            return var1;
        }

        public SphericalMetadataOuterClass.StereoMeshConfig mergeFrom(CodedInputByteBufferNano var1) throws IOException {
            while(true) {
                int var2 = var1.readTag();
                switch(var2) {
                    case 0:
                        return this;
                    case 10:
                        if(this.leftEyeMesh == null) {
                            this.leftEyeMesh = new SphericalMetadataOuterClass.StereoMeshConfig.Mesh();
                        }

                        var1.readMessage(this.leftEyeMesh);
                        break;
                    case 18:
                        if(this.rightEyeMesh == null) {
                            this.rightEyeMesh = new SphericalMetadataOuterClass.StereoMeshConfig.Mesh();
                        }

                        var1.readMessage(this.rightEyeMesh);
                        break;
                    default:
                        if(!super.storeUnknownField(var1, var2)) {
                            return this;
                        }
                }
            }
        }

        public static SphericalMetadataOuterClass.StereoMeshConfig parseFrom(byte[] var0) throws InvalidProtocolBufferNanoException {
            return (SphericalMetadataOuterClass.StereoMeshConfig)MessageNano.mergeFrom(new SphericalMetadataOuterClass.StereoMeshConfig(), var0);
        }

        public static SphericalMetadataOuterClass.StereoMeshConfig parseFrom(CodedInputByteBufferNano var0) throws IOException {
            return (new SphericalMetadataOuterClass.StereoMeshConfig()).mergeFrom(var0);
        }

        public static final class Mesh extends ExtendableMessageNano<SphericalMetadataOuterClass.StereoMeshConfig.Mesh> {
            private static volatile SphericalMetadataOuterClass.StereoMeshConfig.Mesh[] _emptyArray;
            public SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex[] vertices;
            @NanoEnumValue(
                    value = SphericalMetadataOuterClass.StereoMeshConfig.Mesh.GeometryType.class,
                    legacy = false
            )
            public int geometryType;

            @NanoEnumValue(
                    value = SphericalMetadataOuterClass.StereoMeshConfig.Mesh.GeometryType.class,
                    legacy = false
            )
            public static int checkGeometryTypeOrThrow(int var0) {
                switch(var0) {
                    case 0:
                    case 1:
                        return var0;
                    default:
                        throw new IllegalArgumentException((new StringBuilder(44)).append(var0).append(" is not a valid enum GeometryType").toString());
                }
            }

            @NanoEnumValue(
                    value = SphericalMetadataOuterClass.StereoMeshConfig.Mesh.GeometryType.class,
                    legacy = false
            )
            public static int[] checkGeometryTypeOrThrow(int[] var0) {
                int[] var1 = var0;
                int var2 = var0.length;

                for(int var3 = 0; var3 < var2; ++var3) {
                    int var4 = var1[var3];
                    checkGeometryTypeOrThrow(var4);
                }

                return var0;
            }

            public static SphericalMetadataOuterClass.StereoMeshConfig.Mesh[] emptyArray() {
                if(_emptyArray == null) {
                    Object var0 = InternalNano.LAZY_INIT_LOCK;
                    synchronized(InternalNano.LAZY_INIT_LOCK) {
                        if(_emptyArray == null) {
                            _emptyArray = new SphericalMetadataOuterClass.StereoMeshConfig.Mesh[0];
                        }
                    }
                }

                return _emptyArray;
            }

            public Mesh() {
                this.clear();
            }

            public SphericalMetadataOuterClass.StereoMeshConfig.Mesh clear() {
                this.vertices = SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex.emptyArray();
                this.geometryType = 0;
                this.unknownFieldData = null;
                this.cachedSize = -1;
                return this;
            }

            public void writeTo(CodedOutputByteBufferNano var1) throws IOException {
                if(this.vertices != null && this.vertices.length > 0) {
                    for(int var2 = 0; var2 < this.vertices.length; ++var2) {
                        SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex var3 = this.vertices[var2];
                        if(var3 != null) {
                            var1.writeMessage(1, var3);
                        }
                    }
                }

                if(this.geometryType != 0) {
                    var1.writeInt32(2, this.geometryType);
                }

                super.writeTo(var1);
            }

            protected int computeSerializedSize() {
                int var1 = super.computeSerializedSize();
                if(this.vertices != null && this.vertices.length > 0) {
                    for(int var2 = 0; var2 < this.vertices.length; ++var2) {
                        SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex var3 = this.vertices[var2];
                        if(var3 != null) {
                            var1 += CodedOutputByteBufferNano.computeMessageSize(1, var3);
                        }
                    }
                }

                if(this.geometryType != 0) {
                    var1 += CodedOutputByteBufferNano.computeInt32Size(2, this.geometryType);
                }

                return var1;
            }

            public SphericalMetadataOuterClass.StereoMeshConfig.Mesh mergeFrom(CodedInputByteBufferNano var1) throws IOException {
                while(true) {
                    int var2 = var1.readTag();
                    int var3;
                    int var4;
                    SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex[] var5;
                    switch(var2) {
                        case 0:
                            return this;
                        case 10:
                            var3 = WireFormatNano.getRepeatedFieldArrayLength(var1, 10);
                            var4 = this.vertices == null?0:this.vertices.length;
                            var5 = new SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex[var4 + var3];
                            if(var4 != 0) {
                                System.arraycopy(this.vertices, 0, var5, 0, var4);
                            }
                            break;
                        case 16:
                            var3 = var1.getPosition();

                            try {
                                this.geometryType = checkGeometryTypeOrThrow(var1.readInt32());
                            } catch (IllegalArgumentException var6) {
                                var1.rewindToPosition(var3);
                                this.storeUnknownField(var1, var2);
                            }
                            continue;
                        default:
                            if(super.storeUnknownField(var1, var2)) {
                                continue;
                            }

                            return this;
                    }

                    while(var4 < var5.length - 1) {
                        var5[var4] = new SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex();
                        var1.readMessage(var5[var4]);
                        var1.readTag();
                        ++var4;
                    }

                    var5[var4] = new SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex();
                    var1.readMessage(var5[var4]);
                    this.vertices = var5;
                }
            }

            public static SphericalMetadataOuterClass.StereoMeshConfig.Mesh parseFrom(byte[] var0) throws InvalidProtocolBufferNanoException {
                return (SphericalMetadataOuterClass.StereoMeshConfig.Mesh)MessageNano.mergeFrom(new SphericalMetadataOuterClass.StereoMeshConfig.Mesh(), var0);
            }

            public static SphericalMetadataOuterClass.StereoMeshConfig.Mesh parseFrom(CodedInputByteBufferNano var0) throws IOException {
                return (new SphericalMetadataOuterClass.StereoMeshConfig.Mesh()).mergeFrom(var0);
            }

            public static final class Vertex extends ExtendableMessageNano<SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex> {
                private static volatile SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex[] _emptyArray;
                public float x;
                public float y;
                public float z;
                public float u;
                public float v;

                public static SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex[] emptyArray() {
                    if(_emptyArray == null) {
                        Object var0 = InternalNano.LAZY_INIT_LOCK;
                        synchronized(InternalNano.LAZY_INIT_LOCK) {
                            if(_emptyArray == null) {
                                _emptyArray = new SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex[0];
                            }
                        }
                    }

                    return _emptyArray;
                }

                public Vertex() {
                    this.clear();
                }

                public SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex clear() {
                    this.x = 0.0F;
                    this.y = 0.0F;
                    this.z = 0.0F;
                    this.u = 0.0F;
                    this.v = 0.0F;
                    this.unknownFieldData = null;
                    this.cachedSize = -1;
                    return this;
                }

                public void writeTo(CodedOutputByteBufferNano var1) throws IOException {
                    if(Float.floatToIntBits(this.x) != Float.floatToIntBits(0.0F)) {
                        var1.writeFloat(1, this.x);
                    }

                    if(Float.floatToIntBits(this.y) != Float.floatToIntBits(0.0F)) {
                        var1.writeFloat(2, this.y);
                    }

                    if(Float.floatToIntBits(this.z) != Float.floatToIntBits(0.0F)) {
                        var1.writeFloat(3, this.z);
                    }

                    if(Float.floatToIntBits(this.u) != Float.floatToIntBits(0.0F)) {
                        var1.writeFloat(4, this.u);
                    }

                    if(Float.floatToIntBits(this.v) != Float.floatToIntBits(0.0F)) {
                        var1.writeFloat(5, this.v);
                    }

                    super.writeTo(var1);
                }

                protected int computeSerializedSize() {
                    int var1 = super.computeSerializedSize();
                    if(Float.floatToIntBits(this.x) != Float.floatToIntBits(0.0F)) {
                        var1 += CodedOutputByteBufferNano.computeFloatSize(1, this.x);
                    }

                    if(Float.floatToIntBits(this.y) != Float.floatToIntBits(0.0F)) {
                        var1 += CodedOutputByteBufferNano.computeFloatSize(2, this.y);
                    }

                    if(Float.floatToIntBits(this.z) != Float.floatToIntBits(0.0F)) {
                        var1 += CodedOutputByteBufferNano.computeFloatSize(3, this.z);
                    }

                    if(Float.floatToIntBits(this.u) != Float.floatToIntBits(0.0F)) {
                        var1 += CodedOutputByteBufferNano.computeFloatSize(4, this.u);
                    }

                    if(Float.floatToIntBits(this.v) != Float.floatToIntBits(0.0F)) {
                        var1 += CodedOutputByteBufferNano.computeFloatSize(5, this.v);
                    }

                    return var1;
                }

                public SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex mergeFrom(CodedInputByteBufferNano var1) throws IOException {
                    while(true) {
                        int var2 = var1.readTag();
                        switch(var2) {
                            case 0:
                                return this;
                            case 13:
                                this.x = var1.readFloat();
                                break;
                            case 21:
                                this.y = var1.readFloat();
                                break;
                            case 29:
                                this.z = var1.readFloat();
                                break;
                            case 37:
                                this.u = var1.readFloat();
                                break;
                            case 45:
                                this.v = var1.readFloat();
                                break;
                            default:
                                if(!super.storeUnknownField(var1, var2)) {
                                    return this;
                                }
                        }
                    }
                }

                public static SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex parseFrom(byte[] var0) throws InvalidProtocolBufferNanoException {
                    return (SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex)MessageNano.mergeFrom(new SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex(), var0);
                }

                public static SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex parseFrom(CodedInputByteBufferNano var0) throws IOException {
                    return (new SphericalMetadataOuterClass.StereoMeshConfig.Mesh.Vertex()).mergeFrom(var0);
                }
            }

            public interface GeometryType {
                @NanoEnumValue(
                        value = SphericalMetadataOuterClass.StereoMeshConfig.Mesh.GeometryType.class,
                        legacy = false
                )
                int TRIANGLES = 0;
                @NanoEnumValue(
                        value = SphericalMetadataOuterClass.StereoMeshConfig.Mesh.GeometryType.class,
                        legacy = false
                )
                int TRIANGLE_STRIP = 1;
            }
        }
    }
}
