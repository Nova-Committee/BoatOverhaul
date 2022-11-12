package committee.nova.boatoverhaul.mixin;

import committee.nova.boatoverhaul.api.client.input.IInput;
import committee.nova.boatoverhaul.api.common.boat.IBoat;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.MovementInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP {
    @Shadow
    public MovementInput movementInput;

    @Redirect(method = "updateRidden", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/item/EntityBoat;updateInputs(ZZZZ)V"))
    public void redirect$updateRidden(EntityBoat instance, boolean left, boolean right, boolean up, boolean down) {
        final IInput i = (IInput) movementInput;
        ((IBoat) instance).setInputExtended(left, right, up, down, i.isOnLeftRudder(), i.isOnRightRudder());
    }
}
