package committee.nova.boatoverhaul.mixin;

import committee.nova.boatoverhaul.api.client.input.IInput;
import committee.nova.boatoverhaul.common.key.Keys;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class MixinKeyboardInput {
    @Inject(method = "tick", at = @At("HEAD"))
    public void inject$tick(boolean bl, float f, CallbackInfo ci) {
        final IInput extended = (IInput) this;
        extended.setLeftRudder(Keys.keyLeftRudder.isDown());
        extended.setRightRudder(Keys.keyRightRudder.isDown());
    }
}
