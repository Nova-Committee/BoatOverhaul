package committee.nova.boatoverhaul.client.overlay.init;

import committee.nova.boatoverhaul.api.client.widget.IGearWidget;
import committee.nova.boatoverhaul.api.client.widget.IRudderWidget;
import committee.nova.boatoverhaul.api.client.widget.IWidget;
import committee.nova.boatoverhaul.api.common.boat.IBoat;
import committee.nova.boatoverhaul.BoatOverhaulClient;
import committee.nova.boatoverhaul.client.overlay.CursorWidgets;
import committee.nova.boatoverhaul.client.overlay.GearWidgets;
import committee.nova.boatoverhaul.client.overlay.RudderWidgets;
import committee.nova.boatoverhaul.common.boat.gear.Gear;
import committee.nova.boatoverhaul.common.boat.gear.Rudder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix3x2fStack;

public class OverlayInit {
    private static final Identifier overlay = Identifier.fromNamespaceAndPath(BoatOverhaulClient.MOD_ID, "textures/gui/overlay/indicator.png");

    public static void renderBoatOverlay(GuiGraphicsExtractor graphics) {
        final Player player = Minecraft.getInstance().player;
        if (player == null) return;
        final Entity vehicle = player.getVehicle();
        if (!(vehicle instanceof final IBoat boat)) return;
        final Minecraft mc = Minecraft.getInstance();
        final int height = mc.getWindow().getGuiScaledHeight();
        final int width = mc.getWindow().getGuiScaledWidth();
        final int gearXOffset = width / 2 - 50 + BoatOverhaulClient.getGearX();
        final int gearYOffset = height / 2 - 30 + BoatOverhaulClient.getGearY();
        final int rudderXOffset = width / 2 - 12 + BoatOverhaulClient.getRudderX();
        final int rudderYOffset = height / 2 + 60 + BoatOverhaulClient.getRudderY();
        final Matrix3x2fStack matrix = graphics.pose();
        matrix.pushMatrix();
        renderGearScale(boat.boatOverhaul$getTargetGear(), graphics, gearXOffset, gearYOffset);
        renderGearCursor(boat.boatOverhaul$getGearState().getGear(), (int) (10F * boat.boatOverhaul$getGearAccumulation() / boat.boatOverhaul$getMaxGearAccumulation()), graphics, gearXOffset + 25, gearYOffset + 42);
        if (boat.boatOverhaul$getTargetRudder() != null && (boat.boatOverhaul$getTargetRudder() != Rudder.ZERO || boat.boatOverhaul$getTargetRudder() != boat.boatOverhaul$getRudderState().getRudder())) {
            renderRudderScale(boat.boatOverhaul$getTargetRudder() == Rudder.ZERO ? null : boat.boatOverhaul$getTargetRudder(), graphics, rudderXOffset, rudderYOffset);
            renderRudderCursor(boat.boatOverhaul$getRudderState().getRudder(), (int) (25F * boat.boatOverhaul$getRudderAccumulation() / boat.boatOverhaul$getMaxRudderAccumulation()), graphics, rudderXOffset + 9, rudderYOffset + 12);
        } else if (boat.boatOverhaul$isRudderWorking()) {
            renderRudderScale(boat.boatOverhaul$getRudderState().getRudder(), graphics, rudderXOffset, rudderYOffset);
            renderRudderCursor(boat.boatOverhaul$getRudderState().getRudder(), (int) (25F * boat.boatOverhaul$getRudderAccumulation() / boat.boatOverhaul$getMaxRudderAccumulation()), graphics, rudderXOffset + 9, rudderYOffset + 12);
        }
        matrix.popMatrix();
    }

    private static void renderGearScale(Gear target, GuiGraphicsExtractor graphics, int x, int y) {
        for (final IGearWidget g : GearWidgets.values()) {
            if (g.isForSelected() != (g.getRelativeGear() == target)) continue;
            graphics.blit(RenderPipelines.GUI_TEXTURED, overlay, x, y + g.getYOffset(), g.getStartX(), g.getStartY(), g.getWidth(), g.getHeight(), 256, 256);
        }
    }

    private static void renderRudderScale(Rudder target, GuiGraphicsExtractor graphics, int x, int y) {
        for (final IRudderWidget r : RudderWidgets.values()) {
            if (r.isForSelected() != (r.getRelativeRudder() == target)) continue;
            graphics.blit(RenderPipelines.GUI_TEXTURED, overlay, x + r.getXOffset(), y, r.getStartX(), r.getStartY(), r.getWidth(), r.getHeight(), 256, 256);

        }
    }

    private static void renderGearCursor(Gear currentGear, int accumulationOffset, GuiGraphicsExtractor graphics, int x, int y) {
        final IWidget g = CursorWidgets.GEAR_CURSOR;
        final int actualY = y - currentGear.getNumerator() * 10 / 4 - accumulationOffset / 4;
        graphics.blit(RenderPipelines.GUI_TEXTURED, overlay, x, actualY, g.getStartX(), g.getStartY(), g.getWidth(), g.getHeight(), 256, 256);
    }

    private static void renderRudderCursor(Rudder currentRudder, int accumulationOffset, GuiGraphicsExtractor graphics, int x, int y) {
        final IWidget r = CursorWidgets.RUDDER_CURSOR;
        final int actualX = x + currentRudder.getNumerator() * 25 / 4 + accumulationOffset / 4;
        graphics.blit(RenderPipelines.GUI_TEXTURED, overlay, actualX, y, r.getStartX(), r.getStartY(), r.getWidth(), r.getHeight(), 256, 256);
    }
}
