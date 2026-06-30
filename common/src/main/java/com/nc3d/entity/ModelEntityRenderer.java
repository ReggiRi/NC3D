package com.nc3d.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ModelEntityRenderer extends GeoEntityRenderer<ModelEntity> {
    public ModelEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new NC3DGeoModel());
    }

    @Override
    protected void applyRotations(
            ModelEntity entity,
            PoseStack poseStack,
            float ageInTicks,
            float rotationYaw,
            float partialTick,
            float scale) {
        super.applyRotations(entity, poseStack, ageInTicks, rotationYaw, partialTick, scale);
        float entityScale = entity.getModelScale();
        if (entityScale != 1.0f) {
            poseStack.scale(entityScale, entityScale, entityScale);
        }
    }
}
