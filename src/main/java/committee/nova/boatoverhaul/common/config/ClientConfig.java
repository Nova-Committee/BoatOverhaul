package committee.nova.boatoverhaul.common.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ClientConfig {
    private static Configuration config;
    private static boolean allowSteeringWhenStopped;
    private static boolean reverseRudderWhenSailingAstern;
    private static float speedMultiplier;

    public static boolean shouldAllowSteeringWhenStopped() {
        return allowSteeringWhenStopped;
    }

    public static boolean shouldReverseRudderWhenSailingAstern() {
        return reverseRudderWhenSailingAstern;
    }

    public static float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public static void init(FMLPreInitializationEvent event) {
        config = new Configuration(new File(event.getModConfigurationDirectory(), "BoatOverhaul.cfg"));
        sync();
    }

    public static void sync() {
        config.load();
        allowSteeringWhenStopped = config.getBoolean("allowSteeringWhenStopped", Configuration.CATEGORY_CLIENT, false,
                "Allows a boat to turn its rudder with a very small extra forward speed when the gear state is at STOP");
        reverseRudderWhenSailingAstern = config.getBoolean("reverseRudderWhenSailingAstern", Configuration.CATEGORY_CLIENT, false,
                "If set to true, a boat sailing backwards and ruddering to right, for example, will sail to the left rearward");
        speedMultiplier = config.getFloat("speedMultiplier", Configuration.CATEGORY_CLIENT, 1.0F,
                0.1F, 2.0F, "The multiplier of the boat's max speed forward");
        config.save();
    }
}
