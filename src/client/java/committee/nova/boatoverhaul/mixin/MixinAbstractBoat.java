package committee.nova.boatoverhaul.mixin;

import committee.nova.boatoverhaul.api.client.sound.SoundUtil;
import committee.nova.boatoverhaul.api.common.boat.IBoat;
import committee.nova.boatoverhaul.BoatOverhaulClient;
import committee.nova.boatoverhaul.common.boat.gear.Gear;
import committee.nova.boatoverhaul.common.boat.gear.Rudder;
import committee.nova.boatoverhaul.common.boat.state.GearState;
import committee.nova.boatoverhaul.common.boat.state.RudderState;
import committee.nova.boatoverhaul.util.Utilities;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(AbstractBoat.class)
public abstract class MixinAbstractBoat extends Entity implements IBoat {
    @Shadow
    public abstract void setInput(boolean left, boolean right, boolean up, boolean down);

    @Shadow
    private float deltaRotation;

    @Shadow
    public abstract void setPaddleState(boolean left, boolean right);

    @Shadow
    private boolean inputLeft;
    @Shadow
    private boolean inputRight;
    @Shadow
    private boolean inputUp;
    @Shadow
    private boolean inputDown;
    @Unique
    private int boatOverhaul$gearCd;
    @Unique
    private int boatOverhaul$rudderCd;
    @Unique
    private int boatOverhaul$rudderAccumulation;
    @Unique
    private final int boatOverhaul$maxRudderAccumulation = 5;
    @Unique
    private int boatOverhaul$gearAccumulation;
    @Unique
    private final int boatOverhaul$maxGearAccumulation = 5;
    @Unique
    private boolean boatOverhaul$inputLRudder;
    @Unique
    private boolean boatOverhaul$inputRRudder;
    @Unique
    private Rudder boatOverhaul$targetRudder;
    @Unique
    private Gear boatOverhaul$targetGear;
    @Unique
    private GearState boatOverhaul$gearState;
    @Unique
    private RudderState boatOverhaul$rudderState;

    public MixinAbstractBoat(EntityType<?> e, Level l) {
        super(e, l);
    }

    @Override
    public void boatOverhaul$setInputExtended(boolean left, boolean right, boolean forward, boolean back, boolean lRudder, boolean rRudder) {
        setInput(left, right, forward, back);
        boatOverhaul$inputLRudder = lRudder;
        boatOverhaul$inputRRudder = rRudder;
    }

    @Override
    public int boatOverhaul$getRudderAccumulation() {
        return boatOverhaul$rudderAccumulation;
    }

    @Override
    public int boatOverhaul$getMaxRudderAccumulation() {
        return boatOverhaul$maxRudderAccumulation;
    }

    @Override
    public int boatOverhaul$getGearAccumulation() {
        return boatOverhaul$gearAccumulation;
    }

    @Override
    public int boatOverhaul$getMaxGearAccumulation() {
        return boatOverhaul$maxGearAccumulation;
    }

    @Override
    public GearState boatOverhaul$getGearState() {
        if (boatOverhaul$gearState == null) boatOverhaul$gearState = new GearState();
        return boatOverhaul$gearState;
    }

    @Override
    public RudderState boatOverhaul$getRudderState() {
        if (boatOverhaul$rudderState == null) boatOverhaul$rudderState = new RudderState();
        return boatOverhaul$rudderState;
    }

    @Override
    public Gear boatOverhaul$getTargetGear() {
        if (boatOverhaul$targetGear == null) boatOverhaul$targetGear = Gear.STOP;
        return boatOverhaul$targetGear;
    }

    @Override
    public Rudder boatOverhaul$getTargetRudder() {
        return boatOverhaul$targetRudder;
    }

    @Override
    public boolean boatOverhaul$isRudderWorking() {
        return this.boatOverhaul$rudderState.isWorking() || (inputLeft != inputRight);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void inject$init(EntityType<? extends AbstractBoat> type, Level level, Supplier<Item> dropItem, CallbackInfo ci) {
        boatOverhaul$rudderState = new RudderState();
        boatOverhaul$gearState = new GearState();
    }

    @Inject(method = "controlBoat", at = @At("HEAD"), cancellable = true)
    private void inject$controlBoat(CallbackInfo ci) {
        ci.cancel();
        if (!this.isVehicle()) return;
        boatOverhaul$handleRuddering();
        boatOverhaul$decideRudderStateByAccumulation();
        float f = 0.0F;
        if (BoatOverhaulClient.shouldAllowSteeringWhenStopped() && boatOverhaul$getRudderState().isWorking() && boatOverhaul$getGearState().hasNoAction())
            f += 0.005F;
        boatOverhaul$handleGearing();
        boatOverhaul$decideGearStateByAccumulation();
        f += boatOverhaul$getGearState().getGear().getStandardRate() * 0.04F;
        if (BoatOverhaulClient.shouldAllowSteeringWhenStopped() || !boatOverhaul$getGearState().hasNoAction())
            deltaRotation += (f >= 0.0F || BoatOverhaulClient.shouldReverseRudderWhenSailingAstern() ? 1.0F : -1.0F) * 0.2F * boatOverhaul$getRudderState().getRudder().getStandardRate();
        this.setYRot(this.getYRot() + this.deltaRotation);
        this.setDeltaMovement(this.getDeltaMovement().add(Mth.sin(-this.getYRot() * ((float) Math.PI / 180F)) * f, 0.0D, Mth.cos(this.getYRot() * ((float) Math.PI / 180F)) * f));
        if (BoatOverhaulClient.shouldAllowSteeringWhenStopped())
            this.setPaddleState((boatOverhaul$getRudderState().isRudderingToRight() || boatOverhaul$getGearState().isAhead()), boatOverhaul$getRudderState().isRudderingToLeft() || boatOverhaul$getGearState().isAhead());
        else this.setPaddleState(boatOverhaul$getGearState().isAhead(), boatOverhaul$getGearState().isAhead());
    }

    @Unique
    private void boatOverhaul$handleRuddering() {
        if (this.boatOverhaul$rudderCd > 0) boatOverhaul$rudderCd--;
        if (this.inputLeft || this.inputRight) boatOverhaul$clearAutoRudder();
        if (this.boatOverhaul$rudderCd == 0 && this.boatOverhaul$inputLRudder && !this.boatOverhaul$inputRRudder) {
            if (boatOverhaul$targetRudder == null) boatOverhaul$targetRudder = this.boatOverhaul$rudderState.getRudder();
            final int origin = boatOverhaul$targetRudder.getNumerator();
            final int current = Math.max(boatOverhaul$targetRudder.getNumerator() - 4, Rudder.getMinimumNumerator());
            if (origin != current) {
                boatOverhaul$targetRudder = Rudder.getNearestExplicitRudder(current, false);
                boatOverhaul$rudderCd = 5;
                Utilities.getSoundFromShiftable(boatOverhaul$targetRudder).ifPresent(SoundUtil::playUISound);
            }
        } else if (this.boatOverhaul$rudderCd == 0 && !this.boatOverhaul$inputLRudder && this.boatOverhaul$inputRRudder) {
            if (boatOverhaul$targetRudder == null) boatOverhaul$targetRudder = this.boatOverhaul$rudderState.getRudder();
            final int origin = boatOverhaul$targetRudder.getNumerator();
            final int current = Math.min(boatOverhaul$targetRudder.getNumerator() + 4, boatOverhaul$targetRudder.getDenominator());
            if (origin != current) {
                boatOverhaul$targetRudder = Rudder.getNearestExplicitRudder(current, true);
                boatOverhaul$rudderCd = 5;
                Utilities.getSoundFromShiftable(boatOverhaul$targetRudder).ifPresent(SoundUtil::playUISound);
            }
        }
        if ((this.inputLeft && !this.inputRight) || (boatOverhaul$targetRudder != null && boatOverhaul$targetRudder.compareTo(boatOverhaul$getRudderState().getRudder()) < 0)) {
            if (boatOverhaul$getRudderState().getRudder() != Rudder.FULL_LEFT && boatOverhaul$rudderAccumulation > -boatOverhaul$maxRudderAccumulation)
                boatOverhaul$rudderAccumulation--;
            return;
        }
        if ((!this.inputLeft && this.inputRight) || (boatOverhaul$targetRudder != null && boatOverhaul$targetRudder.compareTo(boatOverhaul$getRudderState().getRudder()) > 0)) {
            if (boatOverhaul$getRudderState().getRudder() != Rudder.FULL_RIGHT && boatOverhaul$rudderAccumulation < boatOverhaul$maxRudderAccumulation)
                boatOverhaul$rudderAccumulation++;
            return;
        }
        if (boatOverhaul$targetRudder != null) {
            if (boatOverhaul$rudderAccumulation > 0) boatOverhaul$rudderAccumulation--;
            if (boatOverhaul$rudderAccumulation < 0) boatOverhaul$rudderAccumulation++;
            return;
        }
        if (boatOverhaul$rudderState.isRudderingToRight()) {
            boatOverhaul$rudderAccumulation--;
        } else if (boatOverhaul$rudderState.isRudderingToLeft()) {
            boatOverhaul$rudderAccumulation++;
        } else {
            if (boatOverhaul$rudderAccumulation > 0) boatOverhaul$rudderAccumulation--;
            if (boatOverhaul$rudderAccumulation < 0) boatOverhaul$rudderAccumulation++;
        }
    }

    @Unique
    private void boatOverhaul$decideRudderStateByAccumulation() {
        if ((boatOverhaul$getRudderState().getRudder() == Rudder.FULL_RIGHT && boatOverhaul$rudderAccumulation >= boatOverhaul$maxRudderAccumulation) ||
                (boatOverhaul$getRudderState().getRudder() == Rudder.FULL_LEFT && boatOverhaul$rudderAccumulation <= -boatOverhaul$maxRudderAccumulation)) {
            return;
        }
        if (boatOverhaul$rudderAccumulation >= boatOverhaul$maxRudderAccumulation) boatOverhaul$rightRudderAndClearAccumulation();
        if (boatOverhaul$rudderAccumulation <= -boatOverhaul$maxRudderAccumulation) boatOverhaul$leftRudderAndClearAccumulation();
    }

    @Unique
    private void boatOverhaul$handleGearing() {
        if (this.boatOverhaul$gearCd > 0) boatOverhaul$gearCd--;
        if (this.boatOverhaul$gearCd == 0 && this.inputUp && !this.inputDown) {
            final int original = boatOverhaul$getTargetGear().getNumerator();
            final int current = Math.min(boatOverhaul$getTargetGear().getNumerator() + 4, boatOverhaul$getTargetGear().getDenominator());
            if (original != current) {
                boatOverhaul$targetGear = Gear.getGearFromNumerator(current);
                boatOverhaul$gearCd = 5;
                Utilities.getSoundFromShiftable(boatOverhaul$targetGear).ifPresent(SoundUtil::playUISound);
            }
        } else if (this.boatOverhaul$gearCd == 0 && !this.inputUp && this.inputDown) {
            final int original = boatOverhaul$getTargetGear().getNumerator();
            final int current = Math.max(boatOverhaul$getTargetGear().getNumerator() - 4, Gear.getMinimumNumerator());
            if (original != current) {
                boatOverhaul$targetGear = Gear.getGearFromNumerator(current);
                boatOverhaul$gearCd = 5;
                Utilities.getSoundFromShiftable(boatOverhaul$targetGear).ifPresent(SoundUtil::playUISound);
            }
        }
        if (boatOverhaul$getTargetGear().compareTo(boatOverhaul$gearState.getGear()) < 0) {
            if (boatOverhaul$gearAccumulation > -boatOverhaul$maxGearAccumulation) boatOverhaul$gearAccumulation--;
            return;
        }
        if (boatOverhaul$getTargetGear().compareTo(boatOverhaul$gearState.getGear()) > 0) {
            if (boatOverhaul$gearAccumulation < boatOverhaul$maxGearAccumulation) boatOverhaul$gearAccumulation++;
            return;
        }
        if (boatOverhaul$gearAccumulation > 0) boatOverhaul$gearAccumulation--;
        if (boatOverhaul$gearAccumulation < 0) boatOverhaul$gearAccumulation++;
    }

    @Unique
    private void boatOverhaul$decideGearStateByAccumulation() {
        if ((boatOverhaul$getGearState().getGear() == Gear.FULL && boatOverhaul$gearAccumulation >= boatOverhaul$maxGearAccumulation) || (boatOverhaul$getGearState().getGear() == Gear.ASTERN && boatOverhaul$gearAccumulation <= -boatOverhaul$maxGearAccumulation))
            return;
        if (boatOverhaul$gearAccumulation >= boatOverhaul$maxGearAccumulation) boatOverhaul$gearForwardAndClearAccumulation();
        if (boatOverhaul$gearAccumulation <= -boatOverhaul$maxGearAccumulation) boatOverhaul$gearBackAndClearAccumulation();
    }

    @Unique
    private void boatOverhaul$rightRudderAndClearAccumulation() {
        boatOverhaul$getRudderState().rightRudder();
        boatOverhaul$rudderAccumulation = 0;
    }

    @Unique
    private void boatOverhaul$leftRudderAndClearAccumulation() {
        boatOverhaul$getRudderState().leftRudder();
        boatOverhaul$rudderAccumulation = 0;
    }

    @Unique
    private void boatOverhaul$clearAutoRudder() {
        boatOverhaul$targetRudder = null;
    }

    @Unique
    private void boatOverhaul$gearForwardAndClearAccumulation() {
        boatOverhaul$getGearState().forward();
        boatOverhaul$gearAccumulation = 0;
    }

    @Unique
    private void boatOverhaul$gearBackAndClearAccumulation() {
        boatOverhaul$getGearState().back();
        boatOverhaul$gearAccumulation = 0;
    }
}
