package committee.nova.boatoverhaul.common.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class CommonConfig {
    private static Configuration config;
    private static boolean allowSteeringWhenStopped;

    public static boolean shouldAllowSteeringWhenStopped() {
        return allowSteeringWhenStopped;
    }

    public static void init(FMLPreInitializationEvent event) {
        config = new Configuration(new File(event.getModConfigurationDirectory(), "BoatOverhaul.cfg"));
        sync();
    }

    public static void sync() {
        config.load();
        allowSteeringWhenStopped = config.getBoolean("allowSteeringWhenStopped", Configuration.CATEGORY_GENERAL, false,
                "Allows a boat to turn its rudder with a very small extra forward speed when the gear state is at STOP", "config.boatoverhaul.allowSteeringWhenStopped");
        config.save();
    }
}
