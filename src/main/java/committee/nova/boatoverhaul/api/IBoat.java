package committee.nova.boatoverhaul.api;

import committee.nova.boatoverhaul.status.GearStatus;
import committee.nova.boatoverhaul.status.RudderStatus;

public interface IBoat {
    void setInputExtended(boolean left, boolean right, boolean forward, boolean back, boolean lRudder, boolean rRudder);

    int getRudderAccumulation();

    int getMaxRudderAccumulation();

    boolean getInputLRudder();

    boolean getInputRRudder();

    GearStatus getGearStatus();

    RudderStatus getRudderStatus();
}
