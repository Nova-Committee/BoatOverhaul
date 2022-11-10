package committee.nova.boatoverhaul.common.status;

import committee.nova.boatoverhaul.common.gear.Rudder;

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

    public void rightRudder() {
        rudder = Rudder.getRudderFromNumerator(Math.min(rudder.getNumerator() + 1, rudder.getDenominator()));
    }

    public void leftRudder() {
        rudder = Rudder.getRudderFromNumerator(Math.max(rudder.getNumerator() - 1, Rudder.getMinimumNumerator()));
    }
}
