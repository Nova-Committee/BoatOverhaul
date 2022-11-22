package committee.nova.boatoverhaul.mixin;

import committee.nova.boatoverhaul.api.common.boat.IBoat;
import committee.nova.boatoverhaul.client.BoatOverhaulClient;
import committee.nova.boatoverhaul.client.sound.SoundUtil;
import committee.nova.boatoverhaul.common.boat.gear.Gear;
import committee.nova.boatoverhaul.common.boat.gear.Rudder;
import committee.nova.boatoverhaul.common.boat.state.GearState;
import committee.nova.boatoverhaul.common.boat.state.RudderState;
import committee.nova.boatoverhaul.util.Utilities;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Boat.class)
public abstract class MixinBoat extends Entity implements IBoat {
    private int gearCd;
    private int rudderCd;
    private int rudderAccumulation;
    private final int maxRudderAccumulation = 20;
    private int gearAccumulation;
    private final int maxGearAccumulation = 20;
    private boolean inputLRudder;
    private boolean inputRRudder;
    private Rudder targetRudder;
    private Gear targetGear;
    private GearState gearState;
    private RudderState rudderState;

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
    public int getGearAccumulation() {
        return gearAccumulation;
    }

    @Override
    public int getMaxGearAccumulation() {
        return maxGearAccumulation;
    }

    @Override
    public GearState getGearState() {
        if (gearState == null) gearState = new GearState();
        return gearState;
    }

    @Override
    public RudderState getRudderState() {
        if (rudderState == null) rudderState = new RudderState();
        return rudderState;
    }

    @Override
    public Gear getTargetGear() {
        if (targetGear == null) targetGear = Gear.STOP;
        return targetGear;
    }

    @Override
    public Rudder getTargetRudder() {
        return targetRudder;
    }

    @Override
    public boolean isRudderWorking() {
        return this.rudderState.isWorking() || (inputLeft != inputRight);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At("RETURN"))
    public void inject$init(EntityType<?> e, Level l, CallbackInfo ci) {
        rudderState = new RudderState();
        gearState = new GearState();
    }

    @Inject(method = "controlBoat", at = @At("HEAD"), cancellable = true)
    private void controlBoat(CallbackInfo ci) {
        ci.cancel();
        if (!this.isVehicle()) return;
        handleRuddering();
        decideRudderStateByAccumulation();
        float f = 0.0F;
        if (BoatOverhaulClient.shouldAllowSteeringWhenStopped() && getRudderState().isWorking() && getGearState().hasNoAction())
            f += 0.005F;
        handleGearing();
        decideGearStateByAccumulation();
        f += getGearState().getGear().getStandardRate() * 0.04F * BoatOverhaulClient.getSpeedMultiplier();
        if (BoatOverhaulClient.shouldAllowSteeringWhenStopped() || !getGearState().hasNoAction())
            deltaRotation += (f >= 0.0F || BoatOverhaulClient.shouldReverseRudderWhenSailingAstern() ? 1.0F : -1.0F) * 0.2F * getRudderState().getRudder().getStandardRate();
        this.yRot += this.deltaRotation;
        this.setDeltaMovement(this.getDeltaMovement().add(Mth.sin(-this.yRot * ((float) Math.PI / 180F)) * f, 0.0D, Mth.cos(this.yRot * ((float) Math.PI / 180F)) * f));
        if (BoatOverhaulClient.shouldAllowSteeringWhenStopped())
            this.setPaddleState((getRudderState().isRudderingToRight() || getGearState().isAhead()), getRudderState().isRudderingToLeft() || getGearState().isAhead());
        else this.setPaddleState(getGearState().isAhead(), getGearState().isAhead());
    }

    private void handleRuddering() {
        if (this.rudderCd > 0) rudderCd--;
        if (this.inputLeft || this.inputRight) clearAutoRudder();
        if (this.rudderCd == 0 && this.inputLRudder && !this.inputRRudder) {
            if (targetRudder == null) targetRudder = this.rudderState.getRudder();
            final int origin = targetRudder.getNumerator();
            final int current = Math.max(targetRudder.getNumerator() - 1, Rudder.getMinimumNumerator());
            if (origin != current) {
                targetRudder = Rudder.getRudderFromNumerator(current);
                rudderCd = 5;
                Utilities.getSoundFromShiftable(targetRudder).ifPresent(SoundUtil::playUISound);
            }
        } else if (this.rudderCd == 0 && !this.inputLRudder && this.inputRRudder) {
            if (targetRudder == null) targetRudder = this.rudderState.getRudder();
            final int origin = targetRudder.getNumerator();
            final int current = Math.min(targetRudder.getNumerator() + 1, targetRudder.getDenominator());
            if (origin != current) {
                targetRudder = Rudder.getRudderFromNumerator(current);
                rudderCd = 5;
                Utilities.getSoundFromShiftable(targetRudder).ifPresent(SoundUtil::playUISound);
            }
        }
        if ((this.inputLeft && !this.inputRight) || (targetRudder != null && targetRudder.compareTo(getRudderState().getRudder()) < 0)) {
            if (getRudderState().getRudder() != Rudder.FULL_LEFT && rudderAccumulation > -maxRudderAccumulation)
                rudderAccumulation--;
            return;
        }
        if ((!this.inputLeft && this.inputRight) || (targetRudder != null && targetRudder.compareTo(getRudderState().getRudder()) > 0)) {
            if (getRudderState().getRudder() != Rudder.FULL_RIGHT && rudderAccumulation < maxRudderAccumulation)
                rudderAccumulation++;
            return;
        }
        if (targetRudder != null) {
            if (rudderAccumulation > 0) rudderAccumulation--;
            if (rudderAccumulation < 0) rudderAccumulation++;
            return;
        }
        if (rudderState.isRudderingToRight()) {
            rudderAccumulation--;
        } else if (rudderState.isRudderingToLeft()) {
            rudderAccumulation++;
        } else {
            if (rudderAccumulation > 0) rudderAccumulation--;
            if (rudderAccumulation < 0) rudderAccumulation++;
        }
    }

    private void decideRudderStateByAccumulation() {
        if ((getRudderState().getRudder() == Rudder.FULL_RIGHT && rudderAccumulation >= maxRudderAccumulation) ||
                (getRudderState().getRudder() == Rudder.FULL_LEFT && rudderAccumulation <= -maxRudderAccumulation)) {
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
                if (getControllingPassenger() instanceof Player) {
                    final Player p = (Player) getControllingPassenger();
                    Utilities.getSoundFromShiftable(targetGear).ifPresent(s -> p.playNotifySound(s, SoundSource.PLAYERS, 1.0F, 1.0F));
                }
            }
        } else if (this.gearCd == 0 && !this.inputUp && this.inputDown) {
            final int original = getTargetGear().getNumerator();
            final int current = Math.max(getTargetGear().getNumerator() - 1, Gear.getMinimumNumerator());
            if (original != current) {
                targetGear = Gear.getGearFromNumerator(current);
                gearCd = 5;
                if (getControllingPassenger() instanceof Player) {
                    final Player p = (Player) getControllingPassenger();
                    Utilities.getSoundFromShiftable(targetGear).ifPresent(s -> p.playNotifySound(s, SoundSource.PLAYERS, 1.0F, 1.0F));
                }
            }
        }
        if (getTargetGear().compareTo(gearState.getGear()) < 0) {
            if (gearAccumulation > -maxGearAccumulation) gearAccumulation--;
            return;
        }
        if (getTargetGear().compareTo(gearState.getGear()) > 0) {
            if (gearAccumulation < maxGearAccumulation) gearAccumulation++;
            return;
        }
        if (gearAccumulation > 0) gearAccumulation--;
        if (gearAccumulation < 0) gearAccumulation++;
    }

    private void decideGearStateByAccumulation() {
        if ((getGearState().getGear() == Gear.FULL && gearAccumulation >= maxGearAccumulation) || (getGearState().getGear() == Gear.ASTERN && gearAccumulation <= -maxGearAccumulation))
            return;
        if (gearAccumulation >= maxGearAccumulation) gearForwardAndClearAccumulation();
        if (gearAccumulation <= -maxGearAccumulation) gearBackAndClearAccumulation();
    }

    private void rightRudderAndClearAccumulation() {
        getRudderState().rightRudder();
        rudderAccumulation = 0;
    }

    private void leftRudderAndClearAccumulation() {
        getRudderState().leftRudder();
        rudderAccumulation = 0;
    }

    private void clearAutoRudder() {
        targetRudder = null;
    }

    private void gearForwardAndClearAccumulation() {
        getGearState().forward();
        gearAccumulation = 0;
    }

    private void gearBackAndClearAccumulation() {
        getGearState().back();
        gearAccumulation = 0;
    }
}
