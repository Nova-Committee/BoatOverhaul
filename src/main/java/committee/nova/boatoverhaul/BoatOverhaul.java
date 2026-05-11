package committee.nova.boatoverhaul;

import committee.nova.boatoverhaul.client.config.ClientConfig;
import committee.nova.boatoverhaul.util.RegistryHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = BoatOverhaul.MOD_ID, dist = Dist.CLIENT)
public class BoatOverhaul {
    public static final String MOD_ID = "boatoverhaul";

    public BoatOverhaul(IEventBus bus, ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CONFIG);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        RegistryHandler.register(bus, container);
    }
}
