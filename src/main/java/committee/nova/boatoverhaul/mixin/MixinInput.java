package committee.nova.boatoverhaul.mixin;

import committee.nova.boatoverhaul.api.client.input.IInput;
import net.minecraft.client.player.Input;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Input.class)
public abstract class MixinInput implements IInput {
    private boolean leftRudder;
    private boolean rightRudder;

    @Override
    public void setLeftRudder(boolean b) {
        leftRudder = b;
    }

    @Override
    public void setRightRudder(boolean b) {
        rightRudder = b;
    }

    @Override
    public boolean isOnLeftRudder() {
        return leftRudder;
    }

    @Override
    public boolean isOnRightRudder() {
        return rightRudder;
    }
}
