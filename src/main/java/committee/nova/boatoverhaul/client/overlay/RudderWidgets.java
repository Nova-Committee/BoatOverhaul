package committee.nova.boatoverhaul.client.overlay;

import committee.nova.boatoverhaul.api.client.widget.IRudderWidget;
import committee.nova.boatoverhaul.common.boat.gear.Rudder;

public enum RudderWidgets implements IRudderWidget {
    ZERO(1, 67, 23, 9, 0, Rudder.ZERO, false),
    ZERO_SELECTED(26, 67, 23, 9, 0, Rudder.ZERO, true),
    FULL_LEFT(1, 56, 23, 9, -50, Rudder.FULL_LEFT, false),
    FULL_LEFT_SELECTED(26, 56, 23, 9, -50, Rudder.FULL_LEFT, true),
    HALF_LEFT(1, 23, 23, 9, -25, Rudder.HALF_LEFT, false),
    HALF_LEFT_SELECTED(26, 23, 23, 9, -25, Rudder.HALF_LEFT, true),
    HALF_RIGHT(1, 23, 23, 9, 25, Rudder.HALF_RIGHT, false),
    HALF_RIGHT_SELECTED(26, 23, 23, 9, 25, Rudder.HALF_RIGHT, true),
    FULL_RIGHT(1, 56, 23, 9, 50, Rudder.FULL_RIGHT, false),
    FULL_RIGHT_SELECTED(26, 56, 23, 9, 50, Rudder.FULL_RIGHT, true);

    RudderWidgets(int startX, int startY, int width, int height, int xOffset, Rudder relative, boolean selected) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.relative = relative;
        this.selected = selected;
    }

    private final int startX;
    private final int startY;
    private final int width;
    private final int height;
    private final int xOffset;
    private final Rudder relative;
    private final boolean selected;

    @Override
    public int getXOffset() {
        return xOffset;
    }

    @Override
    public Rudder getRelativeRudder() {
        return relative;
    }

    @Override
    public boolean isForSelected() {
        return selected;
    }

    @Override
    public int getStartX() {
        return startX;
    }

    @Override
    public int getStartY() {
        return startY;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
