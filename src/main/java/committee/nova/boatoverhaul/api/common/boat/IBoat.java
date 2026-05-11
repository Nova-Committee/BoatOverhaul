package committee.nova.boatoverhaul.api.common.boat;

import committee.nova.boatoverhaul.common.boat.gear.Gear;
import committee.nova.boatoverhaul.common.boat.gear.Rudder;
import committee.nova.boatoverhaul.common.boat.state.GearState;
import committee.nova.boatoverhaul.common.boat.state.RudderState;

public interface IBoat {
    void boatOverhaul$setInputExtended(boolean left, boolean right, boolean forward, boolean back, boolean lRudder, boolean rRudder);

    int boatOverhaul$getRudderAccumulation();

    int boatOverhaul$getMaxRudderAccumulation();

    boolean boatOverhaul$isRudderWorking();

    int boatOverhaul$getGearAccumulation();

    int boatOverhaul$getMaxGearAccumulation();

    GearState boatOverhaul$getGearState();

    RudderState boatOverhaul$getRudderState();

    Gear boatOverhaul$getTargetGear();

    Rudder boatOverhaul$getTargetRudder();
}
