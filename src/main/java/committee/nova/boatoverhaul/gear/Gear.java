package committee.nova.boatoverhaul.gear;

import committee.nova.boatoverhaul.api.IShiftable;

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

    public void forward() {

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
