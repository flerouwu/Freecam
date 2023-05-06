package net.xolt.freecam.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapeSpliterator;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.xolt.freecam.Freecam;
import net.xolt.freecam.config.CollisionWhitelist;
import net.xolt.freecam.config.FreecamConfig;
import net.xolt.freecam.util.FreeCamera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VoxelShapeSpliterator.class)
public class VoxelShapeSpliteratorMixin {

    @Shadow
    private Entity source;

    @Redirect(method = "collisionCheck", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getCollisionShape(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/shapes/ISelectionContext;)Lnet/minecraft/util/math/shapes/VoxelShape;"))
    private VoxelShape onGetCollisionShape(BlockState blockState, IBlockReader world, BlockPos blockPos, ISelectionContext context) {
        if (source instanceof FreeCamera) {
            // Unless "Always Check Initial Collision" is on and Freecam isn't enabled yet
            if (!FreecamConfig.ALWAYS_CHECK_COLLISION.get() || Freecam.isEnabled()) {
                // Ignore all collisions
                if (FreecamConfig.IGNORE_ALL_COLLISION.get()) {
                    return VoxelShapes.empty();
                }
            }
            // Ignore transparent block collisions
            if (FreecamConfig.IGNORE_TRANSPARENT_COLLISION.get() && CollisionWhitelist.isTransparent(blockState.getBlock())) {
                return VoxelShapes.empty();
            }
            // Ignore transparent block collisions
            if (FreecamConfig.IGNORE_TRANSPARENT_COLLISION.get() && CollisionWhitelist.isOpenable(blockState.getBlock())) {
                return VoxelShapes.empty();
            }
        }

        return blockState.getCollisionShape(world, blockPos, context);
    }
}
