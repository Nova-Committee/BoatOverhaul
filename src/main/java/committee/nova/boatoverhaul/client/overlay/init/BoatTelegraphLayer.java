package committee.nova.boatoverhaul.client.overlay.init;

import committee.nova.boatoverhaul.BoatOverhaul;
import committee.nova.boatoverhaul.api.client.widget.IGearWidget;
import committee.nova.boatoverhaul.api.client.widget.IRudderWidget;
import committee.nova.boatoverhaul.api.client.widget.IWidget;
import committee.nova.boatoverhaul.api.common.boat.IBoat;
import committee.nova.boatoverhaul.client.config.ClientConfig;
import committee.nova.boatoverhaul.client.overlay.CursorWidgets;
import committee.nova.boatoverhaul.client.overlay.GearWidgets;
import committee.nova.boatoverhaul.client.overlay.RudderWidgets;
import committee.nova.boatoverhaul.common.boat.gear.Gear;
import committee.nova.boatoverhaul.common.boat.gear.Rudder;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.GuiLayer;
import org.joml.Matrix3x2fStack;

public class BoatTelegraphLayer implements GuiLayer {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(BoatOverhaul.MOD_ID, "telegraph");
    // TODO: Fix these rendering issues
    private static final Identifier overlay = Identifier.fromNamespaceAndPath(BoatOverhaul.MOD_ID, "textures/gui/layer/indicator.png");

    @Override
    public void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        final Player player = Minecraft.getInstance().player;
        if (player == null) return;
        final Entity vehicle = player.getVehicle();
        if (!(vehicle instanceof IBoat boat)) return;
        final int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        final int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        final int gearXOffset = width / 2 - 50 + ClientConfig.gearX.get();
        final int gearYOffset = height / 2 - 30 + ClientConfig.gearY.get();
        final int rudderXOffset = width / 2 - 12 + ClientConfig.rudderX.get();
        final int rudderYOffset = height / 2 + 60 + ClientConfig.rudderY.get();
        final Matrix3x2fStack matrix = graphics.pose();
        matrix.pushMatrix();

        // TODO
        renderGearScale(boat.boatOverhaul$getTargetGear(), graphics, overlay, gearXOffset, gearYOffset);
        renderGearCursor(boat.boatOverhaul$getGearState().getGear(), (int) (10F * boat.boatOverhaul$getGearAccumulation() / boat.boatOverhaul$getMaxGearAccumulation()), graphics, overlay, gearXOffset + 25, gearYOffset + 42);
        if (boat.boatOverhaul$getTargetRudder() != null && (boat.boatOverhaul$getTargetRudder() != Rudder.ZERO || boat.boatOverhaul$getTargetRudder() != boat.boatOverhaul$getRudderState().getRudder())) {
            renderRudderScale(boat.boatOverhaul$getTargetRudder() == Rudder.ZERO ? null : boat.boatOverhaul$getTargetRudder(), graphics, overlay, rudderXOffset, rudderYOffset);
            renderRudderCursor(boat.boatOverhaul$getRudderState().getRudder(), (int) (25F * boat.boatOverhaul$getRudderAccumulation() / boat.boatOverhaul$getMaxRudderAccumulation()), graphics, overlay, rudderXOffset + 9, rudderYOffset + 12);
        } else if (boat.boatOverhaul$isRudderWorking()) {
            renderRudderScale(boat.boatOverhaul$getRudderState().getRudder(), graphics, overlay, rudderXOffset, rudderYOffset);
            renderRudderCursor(boat.boatOverhaul$getRudderState().getRudder(), (int) (25F * boat.boatOverhaul$getRudderAccumulation() / boat.boatOverhaul$getMaxRudderAccumulation()), graphics, overlay, rudderXOffset + 9, rudderYOffset + 12);
        }
        // TODO
        matrix.popMatrix();
    }

    private static void renderGearScale(Gear target, GuiGraphicsExtractor graphics, Identifier tex, int x, int y) {
        for (final IGearWidget g : GearWidgets.values()) {
            if (g.isForSelected() != (g.getRelativeGear() == target)) continue;
            graphics.blit(RenderPipelines.GUI_TEXTURED, tex, x, y + g.getYOffset(), g.getStartX(), g.getStartY(), g.getWidth(), g.getHeight(), 256, 256);
        }
    }

    private static void renderRudderScale(Rudder target, GuiGraphicsExtractor graphics, Identifier tex, int x, int y) {
        for (final IRudderWidget r : RudderWidgets.values()) {
            if (r.isForSelected() != (r.getRelativeRudder() == target)) continue;
            graphics.blit(RenderPipelines.GUI_TEXTURED, tex, x + r.getXOffset(), y, r.getStartX(), r.getStartY(), r.getWidth(), r.getHeight(), 256, 256);
        }
    }

    private static void renderGearCursor(Gear currentGear, int accumulationOffset, GuiGraphicsExtractor graphics, Identifier tex, int x, int y) {
        final IWidget g = CursorWidgets.GEAR_CURSOR;
        final int actualY = y - currentGear.getNumerator() * 10 / 4 - accumulationOffset / 4;
        graphics.blit(RenderPipelines.GUI_TEXTURED, tex, x, actualY, g.getStartX(), g.getStartY(), g.getWidth(), g.getHeight(), 256, 256);
    }

    private static void renderRudderCursor(Rudder currentRudder, int accumulationOffset, GuiGraphicsExtractor graphics, Identifier tex, int x, int y) {
        final IWidget r = CursorWidgets.RUDDER_CURSOR;
        final int actualX = x + currentRudder.getNumerator() * 25 / 4 + accumulationOffset / 4;
        graphics.blit(RenderPipelines.GUI_TEXTURED, tex, actualX, y, r.getStartX(), r.getStartY(), r.getWidth(), r.getHeight(), 256, 256);
    }
}
