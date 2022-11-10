package committee.nova.boatoverhaul.client.overlay.init;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class OverlayInit {
    private static final ResourceLocation overlay = new ResourceLocation(BoatOverhaul.MODID, "textures/overlay/indicator.png");

    @SubscribeEvent
    public static void renderBoatOverlay(RenderGuiOverlayEvent.Post event) {
        final Player player = Minecraft.getInstance().player;
        if (player == null) return;
        final Entity vehicle = player.getVehicle();
        if (!(vehicle instanceof IBoat boat)) return;
        final int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        final int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        final int gearXOffset = width / 2 - 50;
        final int gearYOffset = height / 2 - 30;
        final int rudderXOffset = width / 2 - 12;
        final int rudderYOffset = height / 2 + 60;
        final PoseStack matrix = event.getPoseStack();
        matrix.pushPose();
        startRender();
        RenderSystem.setShaderTexture(0, overlay);
        renderGearScale(boat.getTargetGear(), matrix, gearXOffset, gearYOffset);
        renderGearCursor(boat.getGearState().getGear(), (int) (10F * boat.getGearAccumulation() / boat.getMaxGearAccumulation()), matrix, gearXOffset + 25, gearYOffset + 42);
        if (boat.getTargetRudder() != null && (boat.getTargetRudder() != Rudder.ZERO || boat.getTargetRudder() != boat.getRudderState().getRudder())) {
            renderRudderScale(boat.getTargetRudder() == Rudder.ZERO ? null : boat.getTargetRudder(), matrix, rudderXOffset, rudderYOffset);
            renderRudderCursor(boat.getRudderState().getRudder(), (int) (25F * boat.getRudderAccumulation() / boat.getMaxRudderAccumulation()), matrix, rudderXOffset + 9, rudderYOffset + 12);
        } else if (boat.isRudderWorking()) {
            renderRudderScale(boat.getRudderState().getRudder(), matrix, rudderXOffset, rudderYOffset);
            renderRudderCursor(boat.getRudderState().getRudder(), (int) (25F * boat.getRudderAccumulation() / boat.getMaxRudderAccumulation()), matrix, rudderXOffset + 9, rudderYOffset + 12);
        }

        endRender();
        matrix.popPose();
    }

    private static void renderGearScale(Gear target, PoseStack matrix, int x, int y) {
        for (final IGearWidget g : GearWidgets.values()) {
            if (g.isForSelected() != (g.getRelativeGear() == target)) continue;
            GuiComponent.blit(matrix, x, y + g.getYOffset(), g.getStartX(), g.getStartY(), g.getWidth(), g.getHeight(), 256, 256);
        }
    }

    private static void renderRudderScale(Rudder target, PoseStack matrix, int x, int y) {
        for (final IRudderWidget r : RudderWidgets.values()) {
            if (r.isForSelected() != (r.getRelativeRudder() == target)) continue;
            GuiComponent.blit(matrix, x + r.getXOffset(), y, r.getStartX(), r.getStartY(), r.getWidth(), r.getHeight(), 256, 256);
        }
    }

    private static void renderGearCursor(Gear currentGear, int accumulationOffset, PoseStack matrix, int x, int y) {
        final IWidget g = CursorWidgets.GEAR_CURSOR;
        final int actualY = y - currentGear.getNumerator() * 10 - accumulationOffset;
        GuiComponent.blit(matrix, x, actualY, g.getStartX(), g.getStartY(), g.getWidth(), g.getHeight(), 256, 256);
    }

    private static void renderRudderCursor(Rudder currentRudder, int accumulationOffset, PoseStack matrix, int x, int y) {
        final IWidget r = CursorWidgets.RUDDER_CURSOR;
        final int actualX = x + currentRudder.getNumerator() * 25 + accumulationOffset;
        GuiComponent.blit(matrix, actualX, y, r.getStartX(), r.getStartY(), r.getWidth(), r.getHeight(), 256, 256);
    }

    private static void startRender() {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.5F);
        RenderSystem.setShaderTexture(0, overlay);
    }

    private static void endRender() {
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
