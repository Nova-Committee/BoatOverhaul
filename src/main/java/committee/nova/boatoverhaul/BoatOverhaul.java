package committee.nova.boatoverhaul;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(BoatOverhaul.MODID)
public class BoatOverhaul {
    public static final String MODID = "boatoverhaul";

    public BoatOverhaul() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
