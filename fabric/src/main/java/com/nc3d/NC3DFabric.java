package com.nc3d;

import com.nc3d.network.NC3DPackets;
import net.fabricmc.api.ModInitializer;

public class NC3DFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        NC3DPackets.register();
        NC3DMod.commonInit();
    }
}
