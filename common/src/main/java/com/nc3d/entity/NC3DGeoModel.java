package com.nc3d.entity;

import com.nc3d.NC3DMod;
import com.nc3d.model.ModelData;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class NC3DGeoModel extends GeoModel<ModelEntity> {
    @Override
    public ResourceLocation getModelResource(ModelEntity entity) {
        return resolveData(entity).map(ModelData::modelPath).orElse(ResourceLocation.parse("nc3d:missing"));
    }

    @Override
    public ResourceLocation getTextureResource(ModelEntity entity) {
        return resolveData(entity).map(ModelData::texturePath).orElse(ResourceLocation.parse("nc3d:missing"));
    }

    @Override
    public ResourceLocation getAnimationResource(ModelEntity entity) {
        return resolveData(entity)
                .flatMap(data -> {
                    if (data.animationPath() != null) {
                        return Optional.of(data.animationPath());
                    }
                    return Optional.empty();
                })
                .orElse(null);
    }

    private Optional<ModelData> resolveData(ModelEntity entity) {
        String modelId = entity.getModelId();
        if (modelId.isEmpty()) {
            return Optional.empty();
        }
        return NC3DMod.getInstance().getModelRegistry().get(modelId);
    }
}
