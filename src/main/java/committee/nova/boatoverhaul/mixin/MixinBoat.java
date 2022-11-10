package committee.nova.boatoverhaul.mixin;

import committee.nova.boatoverhaul.api.IBoat;
import committee.nova.boatoverhaul.common.gear.Gear;
import committee.nova.boatoverhaul.common.gear.Rudder;
import committee.nova.boatoverhaul.common.status.GearStatus;
import committee.nova.boatoverhaul.common.status.RudderStatus;
import committee.nova.boatoverhaul.util.Utilities;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Boat.class)
public abstract class MixinBoat extends Entity implements IBoat {
    private int gearCd;
    private int rudderCd;
    private int rudderAccumulation;
    private final int maxRudderAccumulation = 30;
    private int gearAccumulation;
    private final int maxGearAccumulation = 30;
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

    @Shadow
    private boolean inputUp;

    @Shadow
    private boolean inputDown;

    @Shadow
    @Nullable
    public abstract Entity getControllingPassenger();

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
        if (gearStatus == null) gearStatus = new GearStatus();
        return gearStatus;
    }

    @Override
    public RudderStatus getRudderStatus() {
        if (rudderStatus == null) rudderStatus = new RudderStatus();
        return rudderStatus;
    }

    public Gear getTargetGear() {
        if (targetGear == null) targetGear = Gear.STOP;
        return targetGear;
    }

    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At("RETURN"))
    public void inject$init(EntityType<?> e, Level l, CallbackInfo ci) {
        rudderStatus = new RudderStatus();
        gearStatus = new GearStatus();
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
            this.deltaRotation = this.deltaRotation + 0.8F * getRudderStatus().getRudder().getNumerator() / getRudderStatus().getRudder().getDenominator();
            if (!getRudderStatus().hasNoAction() && getGearStatus().hasNoAction()) {
                f += 0.005F;
            }
            this.setYRot(this.getYRot() + this.deltaRotation);
            handleGearing();
            decideGearStateByAccumulation();
            f += getGearStatus().getGear().getStandardRate() * 0.08F;

            this.setDeltaMovement(this.getDeltaMovement().add(Mth.sin(-this.getYRot() * ((float) Math.PI / 180F)) * f, 0.0D, Mth.cos(this.getYRot() * ((float) Math.PI / 180F)) * f));
            this.setPaddleState(getRudderStatus().isRudderingToRight() || getGearStatus().isAhead(), getRudderStatus().isRudderingToLeft() || getGearStatus().isAhead());
        }
    }

    private void handleRuddering() {
        if (this.rudderCd > 0) rudderCd--;
        if (this.inputLeft || this.inputRight) clearAutoRudder();
        if (this.rudderCd == 0 && this.inputLRudder && !this.inputRRudder) {
            if (targetRudder == null) targetRudder = Rudder.ZERO;
            final int origin = targetRudder.getNumerator();
            final int current = Math.max(targetRudder.getNumerator() - 1, Rudder.getMinimumNumerator());
            if (origin != current) {
                targetRudder = Rudder.getRudderFromNumerator(current);
                rudderCd = 5;
                if (getControllingPassenger() instanceof Player p)
                    Utilities.getSoundFromShiftable(targetRudder).ifPresent(s -> p.playNotifySound(s, SoundSource.PLAYERS, 1.0F, 1.0F));
                //todo: play sound
            }
        } else if (this.rudderCd == 0 && !this.inputLRudder && this.inputRRudder) {
            if (targetRudder == null) targetRudder = Rudder.ZERO;
            final int origin = targetRudder.getNumerator();
            final int current = Math.min(targetRudder.getNumerator() + 1, targetRudder.getDenominator());
            if (origin != current) {
                targetRudder = Rudder.getRudderFromNumerator(current);
                rudderCd = 5;
                if (getControllingPassenger() instanceof Player p)
                    Utilities.getSoundFromShiftable(targetRudder).ifPresent(s -> p.playNotifySound(s, SoundSource.PLAYERS, 1.0F, 1.0F));

                //todo: play sound
            }
        }
        if ((this.inputLeft && !this.inputRight) || (targetRudder != null && targetRudder.compareTo(getRudderStatus().getRudder()) < 0)) {
            if (rudderAccumulation > -maxRudderAccumulation) rudderAccumulation--;
            return;
        }
        if ((!this.inputLeft && this.inputRight) || (targetRudder != null && targetRudder.compareTo(getRudderStatus().getRudder()) > 0)) {
            if (rudderAccumulation < maxRudderAccumulation) rudderAccumulation++;
            return;
        }
        if (rudderStatus.isRudderingToRight()) {
            rudderAccumulation--;
        } else if (rudderStatus.isRudderingToLeft()) {
            rudderAccumulation++;
        } else {
            if (rudderAccumulation > 0) {
                rudderAccumulation--;
            } else rudderAccumulation++;
        }
    }

    private void decideRudderStateByAccumulation() {
        if ((getRudderStatus().getRudder() == Rudder.FULL_RIGHT && rudderAccumulation >= maxRudderAccumulation) ||
                (getRudderStatus().getRudder() == Rudder.FULL_LEFT && rudderAccumulation <= -maxRudderAccumulation)) {
            return;
        }
        if (rudderAccumulation >= maxRudderAccumulation) rightRudderAndClearAccumulation();
        if (rudderAccumulation <= -maxRudderAccumulation) leftRudderAndClearAccumulation();
    }

    private void handleGearing() {
        if (this.gearCd > 0) gearCd--;
        if (this.gearCd == 0 && this.inputUp && !this.inputDown) {
            final int original = getTargetGear().getNumerator();
            final int current = Math.min(getTargetGear().getNumerator() + 1, getTargetGear().getDenominator());
            if (original != current) {
                targetGear = Gear.getGearFromNumerator(current);
                gearCd = 5;
                if (getControllingPassenger() instanceof Player p)
                    Utilities.getSoundFromShiftable(targetGear).ifPresent(s -> p.playNotifySound(s, SoundSource.PLAYERS, 1.0F, 1.0F));
            }
        } else if (this.gearCd == 0 && !this.inputUp && this.inputDown) {
            final int original = getTargetGear().getNumerator();
            final int current = Math.max(getTargetGear().getNumerator() - 1, Gear.getMinimumNumerator());
            if (original != current) {
                targetGear = Gear.getGearFromNumerator(current);
                gearCd = 5;
                if (getControllingPassenger() instanceof Player p)
                    Utilities.getSoundFromShiftable(targetGear).ifPresent(s -> p.playNotifySound(s, SoundSource.PLAYERS, 1.0F, 1.0F));
                //todo: play sound
            }
        }
        if (getTargetGear().compareTo(gearStatus.getGear()) < 0) {
            if (gearAccumulation > -maxGearAccumulation) gearAccumulation--;
            return;
        }
        if (getTargetGear().compareTo(gearStatus.getGear()) > 0) {
            if (gearAccumulation < maxGearAccumulation) gearAccumulation++;
        }
    }

    private void decideGearStateByAccumulation() {
        if ((getGearStatus().getGear() == Gear.FULL && gearAccumulation >= maxGearAccumulation) || (getGearStatus().getGear() == Gear.ASTERN && gearAccumulation <= -maxGearAccumulation))
            return;
        if (gearAccumulation >= maxGearAccumulation) gearForwardAndClearAccumulation();
        if (gearAccumulation <= -maxGearAccumulation) gearBackAndClearAccumulation();
    }

    private void rightRudderAndClearAccumulation() {
        getRudderStatus().rightRudder();
        rudderAccumulation = 0;
    }

    private void leftRudderAndClearAccumulation() {
        getRudderStatus().leftRudder();
        rudderAccumulation = 0;
    }

    private void clearAutoRudder() {
        targetRudder = null;
    }

    private void gearForwardAndClearAccumulation() {
        getGearStatus().forward();
        gearAccumulation = 0;
    }

    private void gearBackAndClearAccumulation() {
        getGearStatus().back();
        gearAccumulation = 0;
    }
}
