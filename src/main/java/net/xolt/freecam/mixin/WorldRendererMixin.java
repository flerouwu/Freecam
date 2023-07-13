package net.xolt.freecam.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.xolt.freecam.Freecam;
import net.xolt.freecam.config.FreecamConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.xolt.freecam.Freecam.MC;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    // Disable player rendering if show player is disabled (non-camera LocalPlayers are rendered by default on Forge)
    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    private void onRenderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, IRenderTypeBuffer vertexConsumers, CallbackInfo ci) {
        if (entity == MC.player && Freecam.isEnabled() && !FreecamConfig.SHOW_PLAYER.get()) {
            ci.cancel();
        }
    }
}
