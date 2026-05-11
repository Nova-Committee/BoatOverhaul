package committee.nova.boatoverhaul.mixin;

import committee.nova.boatoverhaul.api.client.input.IClientInput;
import committee.nova.boatoverhaul.api.common.boat.IBoat;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer {

    @Shadow
    public ClientInput input;

    @Redirect(method = "rideTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/boat/AbstractBoat;setInput(ZZZZ)V"))
    public void redirect$rideTick$setInput(AbstractBoat instance, boolean left, boolean right, boolean up, boolean down) {
        final IClientInput extended = (IClientInput) input;
        ((IBoat) instance).boatOverhaul$setInputExtended(
                this.input.keyPresses.left(),
                this.input.keyPresses.right(),
                this.input.keyPresses.forward(),
                this.input.keyPresses.backward(),
                extended.boatOverhaul$isOnLeftRudder(),
                extended.boatOverhaul$isOnRightRudder()
        );
    }
}
