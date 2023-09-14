package committee.nova.boatoverhaul.client.overlay.init;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import committee.nova.boatoverhaul.BoatOverhaul;
import committee.nova.boatoverhaul.api.client.widget.IGearWidget;
import committee.nova.boatoverhaul.api.client.widget.IRudderWidget;
import committee.nova.boatoverhaul.api.client.widget.IWidget;
import committee.nova.boatoverhaul.api.common.boat.IBoat;
import committee.nova.boatoverhaul.client.BoatOverhaulClient;
import committee.nova.boatoverhaul.client.overlay.CursorWidgets;
import committee.nova.boatoverhaul.client.overlay.GearWidgets;
import committee.nova.boatoverhaul.client.overlay.RudderWidgets;
import committee.nova.boatoverhaul.common.boat.gear.Gear;
import committee.nova.boatoverhaul.common.boat.gear.Rudder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class OverlayInit {
    private static final ResourceLocation overlay = new ResourceLocation(BoatOverhaul.MODID, "textures/overlay/indicator.png");

    public static void renderBoatOverlay(GuiGraphics graphics) {
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
        final PoseStack matrix = graphics.pose();
        matrix.pushPose();
        startRender();
        renderGearScale(boat.getTargetGear(), graphics, gearXOffset, gearYOffset);
        renderGearCursor(boat.getGearState().getGear(), (int) (10F * boat.getGearAccumulation() / boat.getMaxGearAccumulation()), graphics, gearXOffset + 25, gearYOffset + 42);
        if (boat.getTargetRudder() != null && (boat.getTargetRudder() != Rudder.ZERO || boat.getTargetRudder() != boat.getRudderState().getRudder())) {
            renderRudderScale(boat.getTargetRudder() == Rudder.ZERO ? null : boat.getTargetRudder(), graphics, rudderXOffset, rudderYOffset);
            renderRudderCursor(boat.getRudderState().getRudder(), (int) (25F * boat.getRudderAccumulation() / boat.getMaxRudderAccumulation()), graphics, rudderXOffset + 9, rudderYOffset + 12);
        } else if (boat.isRudderWorking()) {
            renderRudderScale(boat.getRudderState().getRudder(), graphics, rudderXOffset, rudderYOffset);
            renderRudderCursor(boat.getRudderState().getRudder(), (int) (25F * boat.getRudderAccumulation() / boat.getMaxRudderAccumulation()), graphics, rudderXOffset + 9, rudderYOffset + 12);
        }

        endRender();
        matrix.popPose();
    }

    private static void renderGearScale(Gear target, GuiGraphics graphics, int x, int y) {
        for (final IGearWidget g : GearWidgets.values()) {
            if (g.isForSelected() != (g.getRelativeGear() == target)) continue;
            graphics.blit(overlay, x, y + g.getYOffset(), g.getStartX(), g.getStartY(), g.getWidth(), g.getHeight(), 256, 256);
        }
    }

    private static void renderRudderScale(Rudder target, GuiGraphics graphics, int x, int y) {
        for (final IRudderWidget r : RudderWidgets.values()) {
            if (r.isForSelected() != (r.getRelativeRudder() == target)) continue;
            graphics.blit(overlay, x + r.getXOffset(), y, r.getStartX(), r.getStartY(), r.getWidth(), r.getHeight(), 256, 256);
        }
    }

    private static void renderGearCursor(Gear currentGear, int accumulationOffset, GuiGraphics graphics, int x, int y) {
        final IWidget g = CursorWidgets.GEAR_CURSOR;
        final int actualY = y - currentGear.getNumerator() * 10 / 4 - accumulationOffset / 4;
        graphics.blit(overlay, x, actualY, g.getStartX(), g.getStartY(), g.getWidth(), g.getHeight(), 256, 256);
    }

    private static void renderRudderCursor(Rudder currentRudder, int accumulationOffset, GuiGraphics graphics, int x, int y) {
        final IWidget r = CursorWidgets.RUDDER_CURSOR;
        final int actualX = x + currentRudder.getNumerator() * 25 / 4 + accumulationOffset / 4;
        graphics.blit(overlay, actualX, y, r.getStartX(), r.getStartY(), r.getWidth(), r.getHeight(), 256, 256);
    }

    private static void startRender() {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void endRender() {
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
