package com.nc3d.entity;

import com.nc3d.NC3DConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class NC3DEntities {
    private static final String ENTITY_ID = NC3DConstants.MOD_ID + ":model_entity";

    public static final EntityType<ModelEntity> MODEL_ENTITY = EntityType.Builder.<ModelEntity>of(
                    ModelEntity::new, MobCategory.MISC)
            .sized(0.6f, 1.8f)
            .clientTrackingRange(10)
            .build(ENTITY_ID);

    private NC3DEntities() {}

    public static void register() {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.parse(ENTITY_ID), MODEL_ENTITY);
    }
}
