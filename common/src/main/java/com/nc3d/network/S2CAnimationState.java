package com.nc3d.network;

import com.nc3d.NC3DConstants;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record S2CAnimationState(int entityId, String animationName, boolean loop) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CAnimationState> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.parse(NC3DConstants.MOD_ID + ":anim_state"));

    public static final StreamCodec<ByteBuf, S2CAnimationState> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, S2CAnimationState::entityId,
            ByteBufCodecs.STRING_UTF8, S2CAnimationState::animationName,
            ByteBufCodecs.BOOL, S2CAnimationState::loop,
            S2CAnimationState::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
