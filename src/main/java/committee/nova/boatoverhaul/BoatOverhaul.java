package committee.nova.boatoverhaul;

import committee.nova.boatoverhaul.util.RegistryHandler;
import net.fabricmc.api.ModInitializer;

public class BoatOverhaul implements ModInitializer {
    public static final String MODID = "boatoverhaul";

    @Override
    public void onInitialize() {
        RegistryHandler.register();
    }
}
