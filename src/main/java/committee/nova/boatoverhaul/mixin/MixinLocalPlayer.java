package committee.nova.boatoverhaul.mixin;

import committee.nova.boatoverhaul.api.client.input.IInput;
import committee.nova.boatoverhaul.api.common.boat.IBoat;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.util.MovementInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinLocalPlayer {

    @Shadow
    public MovementInput input;

    @Redirect(method = "rideTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/item/BoatEntity;setInput(ZZZZ)V"))
    public void redirect$rideTick(BoatEntity instance, boolean p_184442_1_, boolean p_184442_2_, boolean p_184442_3_, boolean p_184442_4_) {
        final IInput extended = (IInput) input;
        ((IBoat) instance).setInputExtended(this.input.left, this.input.right, this.input.up, this.input.down, extended.isOnLeftRudder(), extended.isOnRightRudder());
    }
}
