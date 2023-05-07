package net.xolt.freecam.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xolt.freecam.Freecam;
import net.xolt.freecam.config.CollisionWhitelist;
import net.xolt.freecam.config.FreecamConfig;
import net.xolt.freecam.util.FreeCamera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    private void onGetCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (context instanceof EntityCollisionContext && ((EntityCollisionContext)context).getEntity().isPresent() && ((EntityCollisionContext)context).getEntity().get() instanceof FreeCamera && Freecam.canUseCheats()) {
            // Unless "Always Check Initial Collision" is on and Freecam isn't enabled yet
            if (!FreecamConfig.ALWAYS_CHECK_COLLISION.get() || Freecam.isEnabled()) {
                // Ignore all collisions
                if (FreecamConfig.IGNORE_ALL_COLLISION.get()) {
                    cir.setReturnValue(Shapes.empty());
                }
            }
            // Ignore transparent block collisions
            if (FreecamConfig.IGNORE_TRANSPARENT_COLLISION.get() && CollisionWhitelist.isTransparent(state.getBlock())) {
                cir.setReturnValue(Shapes.empty());
            }
            // Ignore transparent block collisions
            if (FreecamConfig.IGNORE_OPENABLE_COLLISION.get() && CollisionWhitelist.isOpenable(state.getBlock())) {
                cir.setReturnValue(Shapes.empty());
            }
        }
    }
}
