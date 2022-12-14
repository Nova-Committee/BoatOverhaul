package committee.nova.boatoverhaul.common.boat.gear;

import committee.nova.boatoverhaul.api.common.boat.shiftable.IShiftable;

public enum Gear implements IShiftable {
    ASTERN(-1, 4),
    STOP(0, 4),
    ONE_QUARTER(1, 4),
    ONE_HALF(2, 4),
    THREE_QUARTERS(3, 4),
    FULL(4, 4);
    private final int numerator;
    private final int denominator;

    Gear(int numerator, int denominator) {
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
        return -1;
    }

    public static Gear getGearFromNumerator(int numerator) {
        Gear g = Gear.STOP;
        for (final Gear t : Gear.values()) if (numerator == t.numerator) g = t;
        return g;
    }
}
