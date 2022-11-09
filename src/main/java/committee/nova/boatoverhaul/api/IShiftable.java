package committee.nova.boatoverhaul.api;

public interface IShiftable {
    int getNumerator();

    int getDenominator();

    default float getStandardRate() {
        return 1.0F * getNumerator() / getDenominator();
    }
}
