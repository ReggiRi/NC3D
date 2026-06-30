package com.nc3d.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ModelEntity extends Mob implements GeoAnimatable {
    private static final EntityDataAccessor<String> DATA_MODEL_ID =
            SynchedEntityData.defineId(ModelEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DATA_ANIMATION =
            SynchedEntityData.defineId(ModelEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> DATA_LOOP_ANIMATION =
            SynchedEntityData.defineId(ModelEntity.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);
    private float modelScale = 1.0f;

    public ModelEntity(EntityType<ModelEntity> type, Level level) {
        super(type, level);
        setNoAi(true);
        setInvulnerable(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_MODEL_ID, "");
        builder.define(DATA_ANIMATION, "");
        builder.define(DATA_LOOP_ANIMATION, true);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ModelId")) {
            entityData.set(DATA_MODEL_ID, tag.getString("ModelId"));
        }
        if (tag.contains("ModelScale")) {
            modelScale = tag.getFloat("ModelScale");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("ModelId", getModelId());
        tag.putFloat("ModelScale", modelScale);
    }

    public String getModelId() {
        return entityData.get(DATA_MODEL_ID);
    }

    public void setModelId(String modelId) {
        entityData.set(DATA_MODEL_ID, modelId);
    }

    public String getCurrentAnimation() {
        return entityData.get(DATA_ANIMATION);
    }

    public void setAnimation(String animationName, boolean loop) {
        entityData.set(DATA_ANIMATION, animationName != null ? animationName : "");
        entityData.set(DATA_LOOP_ANIMATION, loop);
    }

    public boolean isLoopingAnimation() {
        return entityData.get(DATA_LOOP_ANIMATION);
    }

    public float getModelScale() {
        return modelScale;
    }

    public void setModelScale(float scale) {
        this.modelScale = scale;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 0, state -> {
            String anim = getCurrentAnimation();
            if (anim.isEmpty()) {
                return PlayState.STOP;
            }
            RawAnimation raw = isLoopingAnimation()
                    ? RawAnimation.begin().thenLoop(anim)
                    : RawAnimation.begin().thenPlay(anim);
            return state.setAndContinue(raw);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animCache;
    }

    @Override
    public double getTick(Object o) {
        return tickCount;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}
}
