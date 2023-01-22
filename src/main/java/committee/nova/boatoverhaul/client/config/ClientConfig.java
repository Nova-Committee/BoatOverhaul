package committee.nova.boatoverhaul.client.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ClientConfig {
    private static Configuration config;
    private static boolean allowSteeringWhenStopped;
    private static boolean reverseRudderWhenSailingAstern;
    private static int gearX;
    private static int gearY;
    private static int rudderX;
    private static int rudderY;

    public static boolean shouldAllowSteeringWhenStopped() {
        return allowSteeringWhenStopped;
    }

    public static boolean shouldReverseRudderWhenSailingAstern() {
        return reverseRudderWhenSailingAstern;
    }

    public static int getGearX() {
        return gearX;
    }

    public static int getGearY() {
        return gearY;
    }

    public static int getRudderX() {
        return rudderX;
    }

    public static int getRudderY() {
        return rudderY;
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
        gearX = config.getInt("gearX", Configuration.CATEGORY_CLIENT, 0, -10000, 10000, "X Offset of gear indicator");
        gearY = config.getInt("gearY", Configuration.CATEGORY_CLIENT, 0, -10000, 10000, "Y Offset of gear indicator");
        rudderX = config.getInt("rudderX", Configuration.CATEGORY_CLIENT, 0, -10000, 10000, "X Offset of rudder indicator");
        rudderY = config.getInt("rudderY", Configuration.CATEGORY_CLIENT, 0, -10000, 10000, "Y Offset of rudder indicator");
        config.save();
    }
}
