package committee.nova.boatoverhaul.common.boat.gear;

import committee.nova.boatoverhaul.api.common.boat.shiftable.IShiftable;

public enum Rudder implements IShiftable {
    FULL_LEFT(-8, 8),
    L7(-7, 8),
    L6(-6, 8),
    L5(-5, 8),
    HALF_LEFT(-4, 8),
    L3(-3, 8),
    L2(-2, 8),
    L1(-1, 8),
    ZERO(0, 8),
    R1(1, 8),
    R2(2, 8),
    R3(3, 8),
    HALF_RIGHT(4, 8),
    R5(5, 8),
    R6(6, 8),
    R7(7, 8),
    FULL_RIGHT(8, 8);

    private final int numerator;
    private final int denominator;
    public static final Rudder[] EXPLICITS = {FULL_LEFT, HALF_LEFT, ZERO, HALF_RIGHT, FULL_RIGHT};

    Rudder(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public int getNumerator() {
        return numerator;
    }

    @Override
    public int getDenominator() {
        return denominator;
    }

    public static int getMinimumNumerator() {
        return -8;
    }

    public static Rudder getRudderFromNumerator(int numerator) {
        Rudder r = Rudder.ZERO;
        for (final Rudder t : Rudder.values()) if (numerator == t.numerator) r = t;
        return r;
    }

    public static Rudder getNearestExplicitRudder(int numerator, boolean right) {
        final Rudder raw = getRudderFromNumerator(numerator);
        for (final Rudder t : EXPLICITS)
            if (right != (t.getNumerator() > raw.getNumerator()) && Math.abs(t.getNumerator() - raw.getNumerator()) < 4)
                return t;
        return raw;
    }
}
