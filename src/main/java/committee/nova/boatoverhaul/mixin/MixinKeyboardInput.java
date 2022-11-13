package committee.nova.boatoverhaul.mixin;

import committee.nova.boatoverhaul.api.client.input.IInput;
import committee.nova.boatoverhaul.common.key.Keys;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MovementInputFromOptions.class)
public abstract class MixinKeyboardInput {
    @Inject(method = "tick", at = @At("HEAD"))
    public void inject$tick(boolean b, CallbackInfo ci) {
        final IInput extended = (IInput) this;
        extended.setLeftRudder(Keys.keyLeftRudder.isDown());
        extended.setRightRudder(Keys.keyRightRudder.isDown());
    }
}
