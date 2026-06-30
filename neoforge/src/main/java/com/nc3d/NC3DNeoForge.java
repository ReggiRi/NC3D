package com.nc3d;

import com.nc3d.entity.ModelEntityRenderer;
import com.nc3d.entity.NC3DEntities;
import com.nc3d.network.NC3DPackets;
import com.nc3d.network.S2CAnimationState;
import com.nc3d.network.S2CSyncModel;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(NC3DConstants.MOD_ID)
public class NC3DNeoForge {
    public NC3DNeoForge(IEventBus eventBus) {
        NC3DPackets.register();
        NC3DMod.commonInit();

        eventBus.addListener((ServerStartedEvent event) ->
                NC3DMod.getInstance().setServer(event.getServer()));
        eventBus.addListener(this::onRegisterPayloads);
        eventBus.addListener(this::onClientSetup);
        eventBus.addListener(this::onRegisterRenderers);
    }

    private void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.getRegistrar(NC3DConstants.MOD_ID);
        registrar.playToClient(S2CSyncModel.TYPE, S2CSyncModel.STREAM_CODEC, (payload, context) -> {});
        registrar.playToClient(S2CAnimationState.TYPE, S2CAnimationState.STREAM_CODEC, (payload, context) -> {});
    }

    private void onClientSetup(FMLClientSetupEvent event) {}

    private void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(NC3DEntities.MODEL_ENTITY.get(), ModelEntityRenderer::new);
    }
}
