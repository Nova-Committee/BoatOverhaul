package committee.nova.boatoverhaul.mixin;

import committee.nova.boatoverhaul.api.IInput;
import committee.nova.boatoverhaul.key.KeyInit;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class MixinKeyboardInput {
    @Shadow
    private static float calculateImpulse(boolean p_205578_, boolean p_205579_) {
        return 0;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void inject$tick(boolean b, float f, CallbackInfo ci) {
        final IInput extended = (IInput) this;
        extended.setLeftRudder(KeyInit.keyLeftRudder.isDown());
        extended.setRightRudder(KeyInit.keyRightRudder.isDown());
    }
}
