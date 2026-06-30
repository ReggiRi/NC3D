package com.nc3d.event;

import com.nc3d.NC3DMod;
import com.nc3d.entity.ModelEntity;
import fr.loudo.narrativecraft.api.NarrativeCraftAPI;
import fr.loudo.narrativecraft.api.events.character.CharacterDespawnEvent;
import fr.loudo.narrativecraft.api.events.character.CharacterSpawnEvent;
import fr.loudo.narrativecraft.api.narrative.IStoryHandler;
import fr.loudo.narrativecraft.api.narrative.character.ICharacter;
import fr.loudo.narrativecraft.api.session.IPlayerSession;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public final class NCCharacterHandler {
    private NCCharacterHandler() {}

    public static void onSpawn(CharacterSpawnEvent event) {
        Entity entity = findEntity(event);
        if (entity instanceof ModelEntity modelEntity && modelEntity.getModelId().isEmpty()) {
            modelEntity.setModelId(event.character().getName().toLowerCase());
        }
    }

    public static void onDespawn(CharacterDespawnEvent event) {
        Entity entity = findEntity(event);
        if (entity instanceof ModelEntity modelEntity) {
            modelEntity.setAnimation("", false);
        }
    }

    private static Entity findEntity(Object event) {
        MinecraftServer server = NC3DMod.getInstance().getServer();
        if (server == null) return null;

        ICharacter character = null;
        if (event instanceof CharacterSpawnEvent se) character = se.character();
        if (event instanceof CharacterDespawnEvent de) character = de.character();
        if (character == null) return null;

        var sessionManager = NarrativeCraftAPI.getInstance().getPlayerSessionManager();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            IPlayerSession session = sessionManager.getByPlayer(player);
            if (session == null) continue;
            IStoryHandler handler = session.getStoryHandler();
            if (handler == null) continue;
            Entity entity = handler.getEntityFromCharacter(character);
            if (entity != null) return entity;
        }
        return null;
    }
}
