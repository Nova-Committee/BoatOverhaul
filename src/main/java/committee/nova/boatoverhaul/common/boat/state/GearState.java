package committee.nova.boatoverhaul.common.boat.state;

import committee.nova.boatoverhaul.common.boat.gear.Gear;

public class GearState {
    private Gear gear;

    public GearState() {
        gear = Gear.STOP;
    }

    public Gear getGear() {
        return gear;
    }

    public boolean hasNoAction() {
        return gear == Gear.STOP;
    }

    public boolean isAhead() {
        return gear.getNumerator() > 0;
    }

    public boolean isAstern() {
        return gear.getNumerator() < 0;
    }

    public void forward() {
        gear = Gear.getGearFromNumerator(Math.min(gear.getNumerator() + 1, gear.getDenominator()));
    }

    public void back() {
        gear = Gear.getGearFromNumerator(Math.max(gear.getNumerator() - 1, Gear.getMinimumNumerator()));
    }
}
