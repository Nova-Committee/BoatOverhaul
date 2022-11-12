package committee.nova.boatoverhaul.proxies;

import committee.nova.boatoverhaul.client.key.Keys;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        Keys.init();
    }
}
