package committee.nova.boatoverhaul.common.boat.gear;

import committee.nova.boatoverhaul.api.common.boat.shiftable.IShiftable;

public enum Gear implements IShiftable {
    ASTERN(-4, 16),
    N3(-3, 16),
    N2(-2, 16),
    N1(-1, 16),
    STOP(0, 16),
    P1(1, 16),
    P2(2, 16),
    P3(3, 16),
    ONE_QUARTER(4, 16),
    P5(5, 16),
    P6(6, 16),
    P7(7, 16),
    ONE_HALF(8, 16),
    P9(9, 16),
    P10(10, 16),
    P11(11, 16),
    THREE_QUARTERS(12, 16),
    P13(13, 16),
    P14(14, 16),
    P15(15, 16),
    FULL(16, 16);
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
        return -4;
    }

    public static Gear getGearFromNumerator(int numerator) {
        Gear g = Gear.STOP;
        for (final Gear t : Gear.values()) if (numerator == t.numerator) g = t;
        return g;
    }
}
