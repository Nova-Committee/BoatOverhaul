package committee.nova.boatoverhaul.api.client.input;

public interface IClientInput {
    void boatOverhaul$setLeftRudder(boolean b);

    void boatOverhaul$setRightRudder(boolean b);

    boolean boatOverhaul$isOnLeftRudder();

    boolean boatOverhaul$isOnRightRudder();
}
