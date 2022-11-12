package committee.nova.boatoverhaul;

import committee.nova.boatoverhaul.proxies.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = BoatOverhaul.MODID, useMetadata = true)
public class BoatOverhaul {
    public static final String MODID = "boatoverhaul";
    public static final String pkgPrefix = "committee.nova.boatoverhaul.proxies.";

    @SidedProxy(clientSide = pkgPrefix + "ClientProxy", serverSide = pkgPrefix + "CommonProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}
