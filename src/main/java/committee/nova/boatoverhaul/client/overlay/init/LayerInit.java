package committee.nova.boatoverhaul.client.overlay.init;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@EventBusSubscriber(Dist.CLIENT)
public class LayerInit {
    @SubscribeEvent
    public static void registerLayer(RegisterGuiLayersEvent event) {
        event.registerAboveAll(BoatTelegraphLayer.ID, new BoatTelegraphLayer());
    }
}
