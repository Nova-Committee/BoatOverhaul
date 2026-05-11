package committee.nova.boatoverhaul.api.common.boat.shiftable;

public interface IShiftable {
    int getNumerator();

    int getDenominator();

    default float getStandardRate() {
        return 1.0F * getNumerator() / getDenominator();
    }
}
