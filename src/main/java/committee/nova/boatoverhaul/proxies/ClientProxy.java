package committee.nova.boatoverhaul.proxies;

import committee.nova.boatoverhaul.client.key.Keys;
import committee.nova.boatoverhaul.common.config.ClientConfig;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        ClientConfig.init(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        Keys.init();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }
}
