package net.xolt.freecam.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.ITextComponent;
import net.xolt.freecam.Freecam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.xolt.freecam.Freecam.MC;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    // Prevent rendering of nametag in inventory screen
    @Inject(method = "renderNameTag", at = @At("HEAD"), cancellable = true)
    private void onRenderLabel(Entity entity, ITextComponent text, MatrixStack matrices, IRenderTypeBuffer vertexConsumers, int light, CallbackInfo ci) {
        if (Freecam.isEnabled() && !((EntityRendererManagerAccessor)MC.getEntityRenderDispatcher()).getShouldRenderShadow()) {
            ci.cancel();
        }
    }
}
