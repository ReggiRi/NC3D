package com.nc3d;

import com.nc3d.entity.ModelEntityRenderer;
import com.nc3d.entity.NC3DEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class NC3DFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(NC3DEntities.MODEL_ENTITY, ModelEntityRenderer::new);
    }
}
