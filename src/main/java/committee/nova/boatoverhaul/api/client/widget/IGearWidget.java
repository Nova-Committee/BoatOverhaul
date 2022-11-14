package committee.nova.boatoverhaul.api.client.widget;

import committee.nova.boatoverhaul.common.boat.gear.Gear;

public interface IGearWidget extends IWidget {
    int getYOffset();

    Gear getRelativeGear();

    boolean isForSelected();
}
