package com.nc3d.model;

import net.minecraft.resources.ResourceLocation;

public record ModelData(
        String modelId,
        ResourceLocation modelPath,
        ResourceLocation animationPath,
        ResourceLocation texturePath,
        float scale,
        float offsetX,
        float offsetY,
        float offsetZ
) {
    public static final float DEFAULT_SCALE = 1.0f;

    public static Builder builder(String modelId, ResourceLocation modelPath, ResourceLocation texturePath) {
        return new Builder(modelId, modelPath, texturePath);
    }

    public static final class Builder {
        private final String modelId;
        private final ResourceLocation modelPath;
        private final ResourceLocation texturePath;
        private ResourceLocation animationPath;
        private float scale = DEFAULT_SCALE;
        private float offsetX;
        private float offsetY;
        private float offsetZ;

        private Builder(String modelId, ResourceLocation modelPath, ResourceLocation texturePath) {
            this.modelId = modelId;
            this.modelPath = modelPath;
            this.texturePath = texturePath;
        }

        public Builder animationPath(ResourceLocation animationPath) {
            this.animationPath = animationPath;
            return this;
        }

        public Builder scale(float scale) {
            this.scale = scale;
            return this;
        }

        public Builder offset(float x, float y, float z) {
            this.offsetX = x;
            this.offsetY = y;
            this.offsetZ = z;
            return this;
        }

        public ModelData build() {
            return new ModelData(modelId, modelPath, animationPath, texturePath, scale, offsetX, offsetY, offsetZ);
        }
    }
}
