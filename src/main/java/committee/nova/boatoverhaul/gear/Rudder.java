package committee.nova.boatoverhaul.gear;

import committee.nova.boatoverhaul.api.IShiftable;

public enum Rudder implements IShiftable {
    FULL_LEFT(-2, 2),
    HALF_LEFT(-1, 2),
    ZERO(0, 2),
    HALF_RIGHT(1, 2),
    FULL_RIGHT(2, 2);

    private final int numerator;
    private final int denominator;

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
        return -2;
    }

    public static Rudder getRudderFromNumerator(int numerator) {
        Rudder r = Rudder.ZERO;
        for (final Rudder t : Rudder.values()) if (numerator == t.numerator) r = t;
        return r;
    }
}
