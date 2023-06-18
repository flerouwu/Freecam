package net.xolt.freecam.mixin;

import net.minecraft.client.renderer.LightTexture;
import net.xolt.freecam.Freecam;
import net.xolt.freecam.config.FreecamConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LightTexture.class)
public class LightTextureMixin {

    @ModifyArg(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/NativeImage;setPixelRGBA(III)V"), index = 2)
    private int onSetColor(int color) {
        if (Freecam.isEnabled() && FreecamConfig.FULL_BRIGHTNESS.get()) {
            return 0xFFFFFFFF;
        }
        return color;
    }
}
