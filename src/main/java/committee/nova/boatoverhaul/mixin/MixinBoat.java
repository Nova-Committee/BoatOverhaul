package committee.nova.boatoverhaul.mixin;

import committee.nova.boatoverhaul.api.IBoat;
import committee.nova.boatoverhaul.gear.Gear;
import committee.nova.boatoverhaul.gear.Rudder;
import committee.nova.boatoverhaul.status.GearStatus;
import committee.nova.boatoverhaul.status.RudderStatus;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Boat.class)
public abstract class MixinBoat extends Entity implements IBoat {
    private int rudderAccumulation;
    private final int maxRudderAccumulation = 60;
    private boolean inputLRudder;
    private boolean inputRRudder;
    private Rudder targetRudder;
    private Gear targetGear;
    private GearStatus gearStatus;
    private RudderStatus rudderStatus;

    public MixinBoat(EntityType<?> e, Level l) {
        super(e, l);
    }

    @Shadow
    public abstract void setInput(boolean p_38343_, boolean p_38344_, boolean p_38345_, boolean p_38346_);

    @Shadow
    public abstract void setPaddleState(boolean p_38340_, boolean p_38341_);

    @Shadow
    private float deltaRotation;

    @Shadow
    private boolean inputLeft;

    @Shadow
    private boolean inputRight;

    @Shadow
    public abstract void tick();

    @Override
    public void setInputExtended(boolean left, boolean right, boolean forward, boolean back, boolean lRudder, boolean rRudder) {
        setInput(left, right, forward, back);
        inputLRudder = lRudder;
        inputRRudder = rRudder;
    }

    @Override
    public int getRudderAccumulation() {
        return rudderAccumulation;
    }

    @Override
    public int getMaxRudderAccumulation() {
        return maxRudderAccumulation;
    }

    @Override
    public boolean getInputLRudder() {
        return inputLRudder;
    }

    @Override
    public boolean getInputRRudder() {
        return inputRRudder;
    }

    @Override
    public GearStatus getGearStatus() {
        return gearStatus;
    }

    @Override
    public RudderStatus getRudderStatus() {
        return rudderStatus;
    }

    /**
     * @author Tapio
     * @reason Overhaul ship sailing mechanism
     */
    @Overwrite
    private void controlBoat() {
        if (this.isVehicle()) {
            float f = 0.0F;
            handleRuddering();
            decideRudderStateByAccumulation();
            this.deltaRotation += rudderStatus.getRudder().getNumerator();
            if (!rudderStatus.hasNoAction() && gearStatus.hasNoAction()) {
                f += 0.005F;
            }
            this.setYRot(this.getYRot() + this.deltaRotation);


            f += gearStatus.getGear().getStandardRate() * 0.08F;
            //if (this.inputUp) {
            //    f += 0.04F;
            //}
//
            //if (this.inputDown) {
            //    f -= 0.005F;
            //}

            this.setDeltaMovement(this.getDeltaMovement().add(Mth.sin(-this.getYRot() * ((float) Math.PI / 180F)) * f, 0.0D, Mth.cos(this.getYRot() * ((float) Math.PI / 180F)) * f));
            this.setPaddleState(rudderStatus.isRudderingToRight() || gearStatus.isAhead(), rudderStatus.isRudderingToLeft() || gearStatus.isAhead());
        }
    }

    private void handleRuddering() {
        if (this.inputLeft || this.inputRight) clearAutoRudder();
        if (this.inputLRudder && !this.inputRRudder) {
            targetRudder = Rudder.getRudderFromNumerator(Math.max(targetRudder.getNumerator() - 1, Rudder.getMinimumNumerator()));
        } else if (!this.inputLRudder && this.inputRRudder) {
            targetRudder = Rudder.getRudderFromNumerator(Math.min(targetRudder.getNumerator() + 1, targetRudder.getDenominator()));
        }
        if ((this.inputLeft && !this.inputRight) || (targetRudder != null && targetRudder.compareTo(rudderStatus.getRudder()) < 0)) {
            if (rudderAccumulation > -maxRudderAccumulation) rudderAccumulation--;
            return;
        }
        if ((!this.inputLeft && this.inputRight) || (targetRudder != null && targetRudder.compareTo(rudderStatus.getRudder()) > 0)) {
            if (rudderAccumulation < maxRudderAccumulation) rudderAccumulation++;
            return;
        }
        if (rudderAccumulation > 0) {
            rudderAccumulation--;
            return;
        }
        if (rudderAccumulation < 0) {
            rudderAccumulation++;
        }
    }

    private void decideRudderStateByAccumulation() {
        if ((rudderStatus.getRudder() == Rudder.FULL_RIGHT && rudderAccumulation >= maxRudderAccumulation) ||
                (rudderStatus.getRudder() == Rudder.FULL_LEFT && rudderAccumulation <= -maxRudderAccumulation)) {
            return;
        }
        if (rudderAccumulation >= maxRudderAccumulation) rightRudderAndClearAccumulation();
        if (rudderAccumulation <= -maxRudderAccumulation) leftRudderAndClearAccumulation();
    }

    private void rightRudderAndClearAccumulation() {
        rudderStatus.rightRudder();
        rudderAccumulation = 0;
    }

    private void leftRudderAndClearAccumulation() {
        rudderStatus.leftRudder();
        rudderAccumulation = 0;
    }

    private void clearAutoRudder() {
        targetRudder = null;
    }

    private void handleGearing() {
        //todo
    }
}
