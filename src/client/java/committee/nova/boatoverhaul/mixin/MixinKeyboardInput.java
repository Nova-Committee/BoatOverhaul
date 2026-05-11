package committee.nova.boatoverhaul.mixin;

import committee.nova.boatoverhaul.api.client.input.IClientInput;
import committee.nova.boatoverhaul.common.key.Keys;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class MixinKeyboardInput {
    @Inject(method = "tick", at = @At("HEAD"))
    public void inject$tick(CallbackInfo ci) {
        final IClientInput extended = (IClientInput) this;
        extended.boatOverhaul$setLeftRudder(Keys.keyLeftRudder.isDown());
        extended.boatOverhaul$setRightRudder(Keys.keyRightRudder.isDown());
    }
}
