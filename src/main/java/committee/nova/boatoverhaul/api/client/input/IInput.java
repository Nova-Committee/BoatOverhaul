package committee.nova.boatoverhaul.api.client.input;

public interface IInput {
    void setLeftRudder(boolean b);

    void setRightRudder(boolean b);

    boolean isOnLeftRudder();

    boolean isOnRightRudder();
}
