package committee.nova.boatoverhaul.api.client.widget;

import committee.nova.boatoverhaul.common.boat.gear.Rudder;

public interface IRudderWidget extends IWidget {
    int getXOffset();

    Rudder getRelativeRudder();

    boolean isForSelected();
}
