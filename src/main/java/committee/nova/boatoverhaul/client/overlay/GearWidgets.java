package committee.nova.boatoverhaul.client.overlay;

import committee.nova.boatoverhaul.api.client.widget.IGearWidget;
import committee.nova.boatoverhaul.common.boat.gear.Gear;

public enum GearWidgets implements IGearWidget {
    FULL(1, 1, 23, 9, 0, Gear.FULL, false),
    FULL_SELECTED(26, 1, 23, 9, 0, Gear.FULL, true),
    TQ(1, 12, 23, 9, 10, Gear.THREE_QUARTERS, false),
    TQ_SELECTED(26, 12, 23, 9, 10, Gear.THREE_QUARTERS, true),
    HALF(1, 23, 23, 9, 20, Gear.ONE_HALF, false),
    HALF_SELECTED(26, 23, 23, 9, 20, Gear.ONE_HALF, true),
    OQ(1, 34, 23, 9, 30, Gear.ONE_QUARTER, false),
    OQ_SELECTED(26, 34, 23, 9, 30, Gear.ONE_QUARTER, true),
    STOP(1, 45, 23, 9, 40, Gear.STOP, false),
    STOP_SELECTED(26, 45, 23, 9, 40, Gear.STOP, true),
    ASTERN(1, 1, 23, 9, 50, Gear.ASTERN, false),
    ASTERN_SELECTED(26, 1, 23, 9, 50, Gear.ASTERN, true);

    GearWidgets(int startX, int startY, int width, int height, int yOffset, Gear relative, boolean selected) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.yOffset = yOffset;
        this.relative = relative;
        this.selected = selected;
    }

    private final int startX;
    private final int startY;
    private final int width;
    private final int height;
    private final int yOffset;
    private final Gear relative;
    private final boolean selected;

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

    @Override
    public int getYOffset() {
        return yOffset;
    }

    @Override
    public Gear getRelativeGear() {
        return relative;
    }

    @Override
    public boolean isForSelected() {
        return selected;
    }
}
