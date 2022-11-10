package committee.nova.boatoverhaul;

import committee.nova.boatoverhaul.util.RegistryHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(BoatOverhaul.MODID)
public class BoatOverhaul {
    public static final String MODID = "boatoverhaul";

    public BoatOverhaul() {
        RegistryHandler.register();
        MinecraftForge.EVENT_BUS.register(this);
    }
}
