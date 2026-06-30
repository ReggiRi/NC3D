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
        keyword = "set_model",
        description = "Changes the 3D model on an existing character.",
        syntax = "set_model <characterName:string> <modelId:string> [--block]",
        side = Side.SERVER)
public class InkSetModel extends InkAction {

    private String characterName;
    private String modelId;

    @Override
    protected InkActionResult doValidate(ParsedCommand cmd, IScene scene) {
        characterName = cmd.getString("characterName");
        modelId = cmd.getString("modelId");
        return InkActionResult.ok();
    }

    @Override
    protected InkActionResult doExecute(IPlayerSession playerSession) {
        Entity entity = playerSession.getStoryHandler().getCharacterEntities().get(characterName.toLowerCase());
        if (entity instanceof ModelEntity modelEntity) {
            modelEntity.setModelId(modelId);
        }
        isRunning = false;
        return InkActionResult.ok();
    }
}
