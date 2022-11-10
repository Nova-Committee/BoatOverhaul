package committee.nova.boatoverhaul.mixin;

import committee.nova.boatoverhaul.api.IBoat;
import committee.nova.boatoverhaul.api.IInput;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer {
    @Shadow
    public Input input;

    @Redirect(method = "rideTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/Boat;setInput(ZZZZ)V"))
    public void redirect$rideTick(Boat instance, boolean p_38343_, boolean p_38344_, boolean p_38345_, boolean p_38346_) {
        final IInput extended = (IInput) input;
        ((IBoat) instance).setInputExtended(this.input.left, this.input.right, this.input.up, this.input.down, extended.isOnLeftRudder(), extended.isOnRightRudder());
    }
}
