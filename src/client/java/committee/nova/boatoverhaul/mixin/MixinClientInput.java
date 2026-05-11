package committee.nova.boatoverhaul.mixin;

import committee.nova.boatoverhaul.api.client.input.IClientInput;
import net.minecraft.client.player.ClientInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ClientInput.class)
public abstract class MixinClientInput implements IClientInput {
    @Unique
    private boolean boatOverhaul$leftRudder;
    @Unique
    private boolean boatOverhaul$rightRudder;

    @Override
    public void boatOverhaul$setLeftRudder(boolean b) {
        boatOverhaul$leftRudder = b;
    }

    @Override
    public void boatOverhaul$setRightRudder(boolean b) {
        boatOverhaul$rightRudder = b;
    }

    @Override
    public boolean boatOverhaul$isOnLeftRudder() {
        return boatOverhaul$leftRudder;
    }

    @Override
    public boolean boatOverhaul$isOnRightRudder() {
        return boatOverhaul$rightRudder;
    }
}
