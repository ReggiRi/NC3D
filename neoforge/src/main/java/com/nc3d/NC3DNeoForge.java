package com.nc3d;

import com.nc3d.entity.ModelEntityRenderer;
import com.nc3d.entity.NC3DEntities;
import com.nc3d.network.NC3DPackets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(NC3DConstants.MOD_ID)
public class NC3DNeoForge {
    public NC3DNeoForge(IEventBus eventBus) {
        NC3DPackets.register();
        NC3DMod.commonInit();

        eventBus.addListener(this::onClientSetup);
        eventBus.addListener(this::onRegisterRenderers);
    }

    private void onClientSetup(FMLClientSetupEvent event) {}

    private void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(NC3DEntities.MODEL_ENTITY.get(), ModelEntityRenderer::new);
    }
}
