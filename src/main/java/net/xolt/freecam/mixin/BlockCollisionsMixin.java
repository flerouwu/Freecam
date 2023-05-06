package net.xolt.freecam.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xolt.freecam.Freecam;
import net.xolt.freecam.config.CollisionWhitelist;
import net.xolt.freecam.config.FreecamConfig;
import net.xolt.freecam.util.FreeCamera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockCollisions.class)
public class BlockCollisionsMixin {

    private Entity entity;

    @Inject(method = "<init>(Lnet/minecraft/world/level/CollisionGetter;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Z)V", at = @At("RETURN"))
    private void onConstructed(CollisionGetter world, Entity entity, AABB box, boolean forEntity, CallbackInfo ci) {
        this.entity = entity;
    }

    @Redirect(method = "computeNext()Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;"))
    private VoxelShape onGetCollisionShape(BlockState blockState, BlockGetter world, BlockPos blockPos, CollisionContext context) {
        if (entity instanceof FreeCamera && Freecam.canUseCheats()) {
            // Unless "Always Check Initial Collision" is on and Freecam isn't enabled yet
            if (!FreecamConfig.ALWAYS_CHECK_COLLISION.get() || Freecam.isEnabled()) {
                // Ignore all collisions
                if (FreecamConfig.IGNORE_ALL_COLLISION.get()) {
                    return Shapes.empty();
                }
            }
            // Ignore transparent block collisions
            if (FreecamConfig.IGNORE_TRANSPARENT_COLLISION.get() && CollisionWhitelist.isTransparent(blockState.getBlock())) {
                return Shapes.empty();
            }
            // Ignore transparent block collisions
            if (FreecamConfig.IGNORE_TRANSPARENT_COLLISION.get() && CollisionWhitelist.isOpenable(blockState.getBlock())) {
                return Shapes.empty();
            }
        }

        return blockState.getCollisionShape(world, blockPos, context);
    }
}
