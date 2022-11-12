package committee.nova.boatoverhaul.mixin;

import committee.nova.boatoverhaul.api.client.input.IInput;
import net.minecraft.util.MovementInput;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MovementInput.class)
public abstract class MixinMovementInput implements IInput {
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
