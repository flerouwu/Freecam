package net.xolt.freecam.mixin;

import net.minecraft.entity.Entity;
import net.xolt.freecam.Freecam;
import net.xolt.freecam.config.FreecamConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.xolt.freecam.Freecam.MC;

@Mixin(Entity.class)
public class EntityMixin {

    // Makes mouse input rotate the FreeCamera.
    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    private void onChangeLookDirection(double x, double y, CallbackInfo ci) {
        if (Freecam.isEnabled() && this.equals(MC.player) && !Freecam.isPlayerControlEnabled()) {
            Freecam.getFreeCamera().turn(x, y);
            ci.cancel();
        }
    }

    // Prevents FreeCamera from pushing/getting pushed by entities.
    @Inject(method = "push(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    private void onPushAwayFrom(Entity entity, CallbackInfo ci) {
        if (Freecam.isEnabled() && (entity.equals(Freecam.getFreeCamera()) || this.equals(Freecam.getFreeCamera()))) {
            ci.cancel();
        }
    }

    // Freezes the player's position if freezePlayer is enabled.
    @Inject(method = "setDeltaMovement", at = @At("HEAD"), cancellable = true)
    private void onSetVelocity(CallbackInfo ci) {
        if (Freecam.isEnabled() && FreecamConfig.FREEZE_PLAYER.get() && !Freecam.isPlayerControlEnabled() && this.equals(MC.player)) {
            ci.cancel();
        }
    }

    // Freezes the player's position if freezePlayer is enabled.
    @Inject(method = "moveRelative", at = @At("HEAD"), cancellable = true)
    private void onUpdateVelocity(CallbackInfo ci) {
        if (Freecam.isEnabled() && FreecamConfig.FREEZE_PLAYER.get() && !Freecam.isPlayerControlEnabled() && this.equals(MC.player)) {
            ci.cancel();
        }
    }

    // Freezes the player's position if freezePlayer is enabled.
    @Inject(method = "setPos", at = @At("HEAD"), cancellable = true)
    private void onSetPosition(CallbackInfo ci) {
        if (Freecam.isEnabled() && FreecamConfig.FREEZE_PLAYER.get() && !Freecam.isPlayerControlEnabled() && this.equals(MC.player)) {
            ci.cancel();
        }
    }

    // Freezes the player's position if freezePlayer is enabled.
    @Inject(method = "setPos", at = @At("HEAD"), cancellable = true)
    private void onSetPos(CallbackInfo ci) {
        if (Freecam.isEnabled() && FreecamConfig.FREEZE_PLAYER.get() && !Freecam.isPlayerControlEnabled() && this.equals(MC.player)) {
            ci.cancel();
        }
    }

    // Freezes the player's position if freezePlayer is enabled.
    @Inject(method = "setPosRaw", at = @At("HEAD"), cancellable = true)
    private void onSetPosRaw(CallbackInfo ci) {
        if (Freecam.isEnabled() && FreecamConfig.FREEZE_PLAYER.get() && !Freecam.isPlayerControlEnabled() && this.equals(MC.player)) {
            ci.cancel();
        }
    }
}
