package committee.nova.boatoverhaul.status;

import committee.nova.boatoverhaul.gear.Rudder;

public class RudderStatus {
    private Rudder rudder;

    public RudderStatus() {
        rudder = Rudder.ZERO;
    }

    public Rudder getRudder() {
        return rudder;
    }

    public boolean hasNoAction() {
        return rudder == Rudder.ZERO;
    }

    public boolean isRudderingToLeft() {
        return rudder.getNumerator() < 0;
    }

    public boolean isRudderingToRight() {
        return rudder.getNumerator() > 0;
    }

    public boolean rightRudder() {
        rudder = Rudder.getRudderFromNumerator(Math.min(rudder.getNumerator() + 1, rudder.getDenominator()));
        return rudder == Rudder.FULL_RIGHT;
    }

    public boolean leftRudder() {
        rudder = Rudder.getRudderFromNumerator(Math.max(rudder.getNumerator() - 1, rudder.getDenominator()));
        return rudder == Rudder.FULL_LEFT;
    }
}
