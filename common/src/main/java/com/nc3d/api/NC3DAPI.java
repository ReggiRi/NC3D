package com.nc3d.api;

import com.nc3d.NC3DMod;
import com.nc3d.entity.NC3DEntities;
import com.nc3d.model.ModelData;
import com.nc3d.model.ModelRegistry;
import net.minecraft.world.entity.EntityType;

public final class NC3DAPI {
    private static final NC3DAPI INSTANCE = new NC3DAPI();

    private NC3DAPI() {}

    public static NC3DAPI getInstance() {
        return INSTANCE;
    }

    public EntityType<?> getModelEntityType() {
        return NC3DEntities.MODEL_ENTITY.get();
    }

    public ModelRegistry getModelRegistry() {
        return NC3DMod.getInstance().getModelRegistry();
    }

    public void registerModel(ModelData data) {
        getModelRegistry().register(data);
    }
}
