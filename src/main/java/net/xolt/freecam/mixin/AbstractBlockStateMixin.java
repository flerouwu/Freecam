package net.xolt.freecam.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.IBlockReader;
import net.xolt.freecam.Freecam;
import net.xolt.freecam.config.CollisionWhitelist;
import net.xolt.freecam.config.FreecamConfig;
import net.xolt.freecam.util.FreeCamera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {

    @Shadow
    public abstract Block getBlock();

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/shapes/ISelectionContext;)Lnet/minecraft/util/math/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
    private void onGetCollisionShape(IBlockReader world, BlockPos pos, ISelectionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (context instanceof EntitySelectionContext && context.getEntity() instanceof FreeCamera) {
            // Unless "Always Check Initial Collision" is on and Freecam isn't enabled yet
            if (!FreecamConfig.ALWAYS_CHECK_COLLISION.get() || Freecam.isEnabled()) {
                // Ignore all collisions
                if (FreecamConfig.IGNORE_ALL_COLLISION.get()) {
                    cir.setReturnValue(VoxelShapes.empty());
                }
            }
            // Ignore transparent block collisions
            if (FreecamConfig.IGNORE_TRANSPARENT_COLLISION.get() && CollisionWhitelist.isTransparent(getBlock())) {
                cir.setReturnValue(VoxelShapes.empty());
            }
            // Ignore transparent block collisions
            if (FreecamConfig.IGNORE_OPENABLE_COLLISION.get() && CollisionWhitelist.isOpenable(getBlock())) {
                cir.setReturnValue(VoxelShapes.empty());
            }
        }
    }
}
