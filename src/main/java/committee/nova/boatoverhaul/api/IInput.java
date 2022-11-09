package committee.nova.boatoverhaul.api;

public interface IInput {
    void setLeftRudder(boolean b);

    void setRightRudder(boolean b);

    boolean isOnLeftRudder();

    boolean isOnRightRudder();
}
