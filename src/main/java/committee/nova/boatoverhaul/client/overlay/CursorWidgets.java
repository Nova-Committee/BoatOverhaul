package committee.nova.boatoverhaul.client.overlay;

import committee.nova.boatoverhaul.api.client.widget.IWidget;

public enum CursorWidgets implements IWidget {
    GEAR_CURSOR(1, 78, 5, 5),
    RUDDER_CURSOR(7, 78, 5, 5);

    CursorWidgets(int startX, int startY, int width, int height) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
    }

    private final int startX;
    private final int startY;
    private final int width;
    private final int height;

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
