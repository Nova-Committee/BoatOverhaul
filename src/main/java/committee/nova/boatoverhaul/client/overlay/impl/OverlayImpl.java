package committee.nova.boatoverhaul.client.overlay.impl;

import committee.nova.boatoverhaul.BoatOverhaul;
import committee.nova.boatoverhaul.api.client.widget.IGearWidget;
import committee.nova.boatoverhaul.api.client.widget.IRudderWidget;
import committee.nova.boatoverhaul.api.client.widget.IWidget;
import committee.nova.boatoverhaul.api.common.boat.IBoat;
import committee.nova.boatoverhaul.client.overlay.CursorWidgets;
import committee.nova.boatoverhaul.client.overlay.GearWidgets;
import committee.nova.boatoverhaul.client.overlay.RudderWidgets;
import committee.nova.boatoverhaul.common.boat.gear.Gear;
import committee.nova.boatoverhaul.common.boat.gear.Rudder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class OverlayImpl extends Gui {
    private static final ResourceLocation overlay = new ResourceLocation(BoatOverhaul.MODID, "textures/overlay/indicator.png");

    public OverlayImpl(Minecraft mc, EntityPlayer player, IBoat boat) {
        final ScaledResolution s = new ScaledResolution(mc);
        final int height = s.getScaledHeight();
        final int width = s.getScaledWidth();
        final int gearXOffset = width / 2 - 50;
        final int gearYOffset = height / 2 - 30;
        final int rudderXOffset = width / 2 - 12;
        final int rudderYOffset = height / 2 + 60;
        startRender();
        mc.getTextureManager().bindTexture(overlay);
        renderGearScale(boat.getTargetGear(), gearXOffset, gearYOffset);
        renderGearCursor(boat.getGearState().getGear(), (int) (10F * boat.getGearAccumulation() / boat.getMaxGearAccumulation()), gearXOffset + 25, gearYOffset + 42);
        if (boat.getTargetRudder() != null && (boat.getTargetRudder() != Rudder.ZERO || boat.getTargetRudder() != boat.getRudderState().getRudder())) {
            renderRudderScale(boat.getTargetRudder() == Rudder.ZERO ? null : boat.getTargetRudder(), rudderXOffset, rudderYOffset);
            renderRudderCursor(boat.getRudderState().getRudder(), (int) (25F * boat.getRudderAccumulation() / boat.getMaxRudderAccumulation()), rudderXOffset + 9, rudderYOffset + 12);
        } else if (boat.isRudderWorking()) {
            renderRudderScale(boat.getRudderState().getRudder(), rudderXOffset, rudderYOffset);
            renderRudderCursor(boat.getRudderState().getRudder(), (int) (25F * boat.getRudderAccumulation() / boat.getMaxRudderAccumulation()), rudderXOffset + 9, rudderYOffset + 12);
        }
        endRender();
    }

    private static void renderGearScale(Gear target, int x, int y) {
        for (final IGearWidget g : GearWidgets.values()) {
            if (g.isForSelected() != (g.getRelativeGear() == target)) continue;
            drawModalRectWithCustomSizedTexture(x, y + g.getYOffset(), g.getStartX(), g.getStartY(), g.getWidth(), g.getHeight(), 256, 256);
        }
    }

    private static void renderRudderScale(Rudder target, int x, int y) {
        for (final IRudderWidget r : RudderWidgets.values()) {
            if (r.isForSelected() != (r.getRelativeRudder() == target)) continue;
            drawModalRectWithCustomSizedTexture(x + r.getXOffset(), y, r.getStartX(), r.getStartY(), r.getWidth(), r.getHeight(), 256, 256);
        }
    }

    private static void renderGearCursor(Gear currentGear, int accumulationOffset, int x, int y) {
        final IWidget g = CursorWidgets.GEAR_CURSOR;
        final int actualY = y - currentGear.getNumerator() * 10 - accumulationOffset;
        drawModalRectWithCustomSizedTexture(x, actualY, g.getStartX(), g.getStartY(), g.getWidth(), g.getHeight(), 256, 256);
    }

    private static void renderRudderCursor(Rudder currentRudder, int accumulationOffset, int x, int y) {
        final IWidget r = CursorWidgets.RUDDER_CURSOR;
        final int actualX = x + currentRudder.getNumerator() * 25 + accumulationOffset;
        drawModalRectWithCustomSizedTexture(actualX, y, r.getStartX(), r.getStartY(), r.getWidth(), r.getHeight(), 256, 256);
    }

    private static void startRender() {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
    }

    private static void endRender() {
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
