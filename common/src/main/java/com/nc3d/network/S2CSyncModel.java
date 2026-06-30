package com.nc3d.network;

import com.nc3d.NC3DConstants;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record S2CSyncModel(int entityId, String modelId, float scale) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CSyncModel> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.parse(NC3DConstants.MOD_ID + ":sync_model"));

    public static final StreamCodec<ByteBuf, S2CSyncModel> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            S2CSyncModel::entityId,
            ByteBufCodecs.STRING_UTF8,
            S2CSyncModel::modelId,
            ByteBufCodecs.FLOAT,
            S2CSyncModel::scale,
            S2CSyncModel::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
