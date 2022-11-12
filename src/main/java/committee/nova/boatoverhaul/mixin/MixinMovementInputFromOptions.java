package committee.nova.boatoverhaul.mixin;

import committee.nova.boatoverhaul.api.client.input.IInput;
import committee.nova.boatoverhaul.client.key.Keys;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MovementInputFromOptions.class)
public abstract class MixinMovementInputFromOptions {
    @Inject(method = "updatePlayerMoveState", at = @At("HEAD"))
    public void inject$updatePlayerMoveState(CallbackInfo ci) {
        final IInput i = (IInput) this;
        i.setLeftRudder(Keys.keyLeftRudder.isKeyDown());
        i.setRightRudder(Keys.keyRightRudder.isKeyDown());
    }
}
