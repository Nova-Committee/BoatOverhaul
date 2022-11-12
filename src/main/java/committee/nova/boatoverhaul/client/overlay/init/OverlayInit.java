package committee.nova.boatoverhaul.client.overlay.init;

import committee.nova.boatoverhaul.BoatOverhaul;
import committee.nova.boatoverhaul.api.common.boat.IBoat;
import committee.nova.boatoverhaul.client.overlay.impl.OverlayImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class OverlayInit {
    private static final ResourceLocation overlay = new ResourceLocation(BoatOverhaul.MODID, "textures/overlay/indicator.png");

    @SubscribeEvent
    public static void renderBoatOverlay(RenderGameOverlayEvent.Post event) {
        final EntityPlayer player = Minecraft.getMinecraft().player;
        if (player == null) return;
        final Entity vehicle = player.getRidingEntity();
        if (!(vehicle instanceof IBoat)) return;
        final IBoat boat = (IBoat) vehicle;
        final Minecraft mc = Minecraft.getMinecraft();
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) new OverlayImpl(mc, player, boat);
    }
}
