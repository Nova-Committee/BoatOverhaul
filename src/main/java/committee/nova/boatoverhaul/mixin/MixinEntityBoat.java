package committee.nova.boatoverhaul.mixin;

import committee.nova.boatoverhaul.api.common.boat.IBoat;
import committee.nova.boatoverhaul.client.sound.SoundUtil;
import committee.nova.boatoverhaul.common.boat.gear.Gear;
import committee.nova.boatoverhaul.common.boat.gear.Rudder;
import committee.nova.boatoverhaul.common.boat.state.GearState;
import committee.nova.boatoverhaul.common.boat.state.RudderState;
import committee.nova.boatoverhaul.common.config.CommonConfig;
import committee.nova.boatoverhaul.util.Utilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(EntityBoat.class)
public abstract class MixinEntityBoat extends Entity implements IBoat {
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

    public MixinEntityBoat(World worldIn) {
        super(worldIn);
    }

    @Shadow
    public abstract void setPaddleState(boolean p_38340_, boolean p_38341_);

    @Shadow
    private float deltaRotation;

    @Shadow
    @Nullable
    public abstract Entity getControllingPassenger();

    @Shadow
    public abstract void updateInputs(boolean p_184442_1_, boolean p_184442_2_, boolean p_184442_3_, boolean p_184442_4_);

    @Shadow
    private boolean leftInputDown;

    @Shadow
    private boolean rightInputDown;

    @Shadow
    private boolean forwardInputDown;

    @Shadow
    private boolean backInputDown;

    @Override
    public void setInputExtended(boolean left, boolean right, boolean forward, boolean back, boolean lRudder, boolean rRudder) {
        inputLRudder = lRudder;
        inputRRudder = rRudder;
        updateInputs(left, right, forward, back);
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
        return this.rudderState.isWorking() || (leftInputDown != rightInputDown);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;)V", at = @At("RETURN"))
    public void inject$init(World w, CallbackInfo ci) {
        rudderState = new RudderState();
        gearState = new GearState();
    }
    
    @Inject(method = "controlBoat", at = @At("HEAD"), cancellable = true)
    private void inject$controlBoat(CallbackInfo ci) {
        ci.cancel();
        if (!this.isBeingRidden()) return;
        float f = 0.0F;
        handleRuddering();
        decideRudderStateByAccumulation();
        if (CommonConfig.shouldAllowSteeringWhenStopped() && getRudderState().isWorking() && getGearState().hasNoAction())
            f += 0.005F;
        handleGearing();
        decideGearStateByAccumulation();
        f += getGearState().getGear().getStandardRate() * 0.08F;
        if (CommonConfig.shouldAllowSteeringWhenStopped() || !getGearState().hasNoAction())
            deltaRotation += ((f >= 0.0f || CommonConfig.shouldReverseRudderWhenSailingAstern()) ? 1.0F : -1.0F) * 0.2F * getRudderState().getRudder().getStandardRate();
        this.rotationYaw += this.deltaRotation;
        this.motionX += MathHelper.sin(-this.rotationYaw * 0.017453292F) * f;
        this.motionZ += MathHelper.cos(this.rotationYaw * 0.017453292F) * f;
        if (CommonConfig.shouldAllowSteeringWhenStopped())
            this.setPaddleState((getRudderState().isRudderingToRight() || getGearState().isAhead()), getRudderState().isRudderingToLeft() || getGearState().isAhead());
        else this.setPaddleState(getGearState().isAhead(), getGearState().isAhead());
    }

    private void handleRuddering() {
        if (this.rudderCd > 0) rudderCd--;
        if (this.leftInputDown || this.rightInputDown) clearAutoRudder();
        if (this.rudderCd == 0 && this.inputLRudder && !this.inputRRudder) {
            if (targetRudder == null) targetRudder = this.rudderState.getRudder();
            final int origin = targetRudder.getNumerator();
            final int current = Math.max(targetRudder.getNumerator() - 1, Rudder.getMinimumNumerator());
            if (origin != current) {
                targetRudder = Rudder.getRudderFromNumerator(current);
                rudderCd = 5;
                if (getControllingPassenger() instanceof EntityPlayer) {
                    final EntityPlayer p = (EntityPlayer) getControllingPassenger();
                    Utilities.getSoundFromShiftable(targetRudder).ifPresent(s -> {
                        if (p.world.isRemote) SoundUtil.playUISound(s);
                    });
                    //todo: notifySound
                }
            }
        } else if (this.rudderCd == 0 && !this.inputLRudder && this.inputRRudder) {
            if (targetRudder == null) targetRudder = this.rudderState.getRudder();
            final int origin = targetRudder.getNumerator();
            final int current = Math.min(targetRudder.getNumerator() + 1, targetRudder.getDenominator());
            if (origin != current) {
                targetRudder = Rudder.getRudderFromNumerator(current);
                rudderCd = 5;
                if (getControllingPassenger() instanceof EntityPlayer) {
                    final EntityPlayer p = (EntityPlayer) getControllingPassenger();
                    Utilities.getSoundFromShiftable(targetRudder).ifPresent(s -> {
                        if (p.world.isRemote) SoundUtil.playUISound(s);
                    });
                }
            }
        }
        if ((this.leftInputDown && !this.rightInputDown) || (targetRudder != null && targetRudder.compareTo(getRudderState().getRudder()) < 0)) {
            if (getRudderState().getRudder() != Rudder.FULL_LEFT && rudderAccumulation > -maxRudderAccumulation)
                rudderAccumulation--;
            return;
        }
        if ((!this.leftInputDown && this.rightInputDown) || (targetRudder != null && targetRudder.compareTo(getRudderState().getRudder()) > 0)) {
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
        if (this.gearCd == 0 && this.forwardInputDown && !this.backInputDown) {
            final int original = getTargetGear().getNumerator();
            final int current = Math.min(getTargetGear().getNumerator() + 1, getTargetGear().getDenominator());
            if (original != current) {
                targetGear = Gear.getGearFromNumerator(current);
                gearCd = 5;
                if (getControllingPassenger() instanceof EntityPlayer) {
                    final EntityPlayer p = (EntityPlayer) getControllingPassenger();
                    Utilities.getSoundFromShiftable(targetGear).ifPresent(s -> {
                        if (p.world.isRemote) SoundUtil.playUISound(s);
                    });
                }
            }
        } else if (this.gearCd == 0 && !this.forwardInputDown && this.backInputDown) {
            final int original = getTargetGear().getNumerator();
            final int current = Math.max(getTargetGear().getNumerator() - 1, Gear.getMinimumNumerator());
            if (original != current) {
                targetGear = Gear.getGearFromNumerator(current);
                gearCd = 5;
                if (getControllingPassenger() instanceof EntityPlayer) {
                    final EntityPlayer p = (EntityPlayer) getControllingPassenger();
                    Utilities.getSoundFromShiftable(targetGear).ifPresent(s -> {
                        if (p.world.isRemote) SoundUtil.playUISound(s);
                    });
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
