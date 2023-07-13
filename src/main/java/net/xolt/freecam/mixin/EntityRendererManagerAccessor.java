package net.xolt.freecam.mixin;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityRendererManager.class)
public interface EntityRendererManagerAccessor {

    @Accessor
    boolean getShouldRenderShadow();
}
