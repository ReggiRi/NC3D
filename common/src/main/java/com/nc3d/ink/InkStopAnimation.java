package com.nc3d.ink;

import com.nc3d.entity.ModelEntity;
import fr.loudo.narrativecraft.api.inkAction.InkAction;
import fr.loudo.narrativecraft.api.inkAction.InkActionResult;
import fr.loudo.narrativecraft.api.inkAction.InkCommand;
import fr.loudo.narrativecraft.api.inkAction.Side;
import fr.loudo.narrativecraft.api.inkAction.syntax.ParsedCommand;
import fr.loudo.narrativecraft.api.narrative.scene.IScene;
import fr.loudo.narrativecraft.api.session.IPlayerSession;
import net.minecraft.world.entity.Entity;

@InkCommand(
        keyword = "stop_animation",
        description = "Stops the current animation on a 3D character model.",
        syntax = "stop_animation <characterName:string> [--block]",
        side = Side.SERVER)
public class InkStopAnimation extends InkAction {

    private String characterName;

    @Override
    protected InkActionResult doValidate(ParsedCommand cmd, IScene scene) {
        characterName = cmd.getString("characterName");
        return InkActionResult.ok();
    }

    @Override
    protected InkActionResult doExecute(IPlayerSession playerSession) {
        Entity entity = playerSession.getStoryHandler().getCharacterEntities().get(characterName.toLowerCase());
        if (entity instanceof ModelEntity modelEntity) {
            modelEntity.setAnimation("", false);
        }
        isRunning = false;
        return InkActionResult.ok();
    }
}
