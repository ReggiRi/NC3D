package com.nc3d;

import com.nc3d.network.NC3DPackets;
import com.nc3d.network.S2CAnimationState;
import com.nc3d.network.S2CSyncModel;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class NC3DFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(S2CSyncModel.TYPE, S2CSyncModel.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(S2CAnimationState.TYPE, S2CAnimationState.STREAM_CODEC);

        NC3DPackets.register();
        NC3DMod.commonInit();

        ServerLifecycleEvents.SERVER_STARTED.register(
                server -> NC3DMod.getInstance().setServer(server));
    }
}
