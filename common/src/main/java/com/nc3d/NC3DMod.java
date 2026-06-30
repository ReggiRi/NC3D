package com.nc3d;

import com.nc3d.entity.NC3DEntities;
import com.nc3d.event.NCCharacterHandler;
import com.nc3d.ink.InkPlayAnimation;
import com.nc3d.ink.InkSetModel;
import com.nc3d.ink.InkSpawnModel;
import com.nc3d.ink.InkStopAnimation;
import com.nc3d.model.ModelRegistry;
import fr.loudo.narrativecraft.api.AddonContext;
import fr.loudo.narrativecraft.api.NarrativeCraftAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NC3DMod {
    private static final NC3DMod INSTANCE = new NC3DMod();
    private static final Logger LOGGER = LoggerFactory.getLogger(NC3DConstants.MOD_NAME);

    private final ModelRegistry modelRegistry = new ModelRegistry();
    private AddonContext addonContext;
    private net.minecraft.server.MinecraftServer server;

    private NC3DMod() {}

    public static NC3DMod getInstance() {
        return INSTANCE;
    }

    public void setServer(net.minecraft.server.MinecraftServer server) {
        this.server = server;
    }

    public net.minecraft.server.MinecraftServer getServer() {
        return server;
    }

    public static void commonInit() {
        NC3DEntities.register();

        NarrativeCraftAPI api = NarrativeCraftAPI.getInstance();
        NC3DMod mod = getInstance();

        mod.addonContext = api.createAddon(
                NC3DConstants.MOD_ID,
                NC3DConstants.MOD_NAME,
                "3D Model support for NarrativeCraft",
                "NC3D Team",
                "",
                1);

        mod.addonContext.registerInkAction(InkSpawnModel.class, InkSpawnModel::new);
        mod.addonContext.registerInkAction(InkSetModel.class, InkSetModel::new);
        mod.addonContext.registerInkAction(InkPlayAnimation.class, InkPlayAnimation::new);
        mod.addonContext.registerInkAction(InkStopAnimation.class, InkStopAnimation::new);

        mod.addonContext.registerEvent(
                fr.loudo.narrativecraft.api.events.character.CharacterSpawnEvent.class, NCCharacterHandler::onSpawn);
        mod.addonContext.registerEvent(
                fr.loudo.narrativecraft.api.events.character.CharacterDespawnEvent.class,
                NCCharacterHandler::onDespawn);

        LOGGER.info("NC3D initialized");
    }

    public ModelRegistry getModelRegistry() {
        return modelRegistry;
    }

    public AddonContext getAddonContext() {
        return addonContext;
    }
}
