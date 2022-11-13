package committee.nova.boatoverhaul.api.common.boat;

import committee.nova.boatoverhaul.common.boat.gear.Gear;
import committee.nova.boatoverhaul.common.boat.gear.Rudder;
import committee.nova.boatoverhaul.common.boat.state.GearState;
import committee.nova.boatoverhaul.common.boat.state.RudderState;

public interface IBoat {
    void setInputExtended(boolean left, boolean right, boolean forward, boolean back, boolean lRudder, boolean rRudder);

    int getRudderAccumulation();

    int getMaxRudderAccumulation();

    boolean isRudderWorking();

    int getGearAccumulation();

    int getMaxGearAccumulation();

    GearState getGearState();

    RudderState getRudderState();

    Gear getTargetGear();

    Rudder getTargetRudder();
}
