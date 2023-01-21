package committee.nova.boatoverhaul.client;

import committee.nova.boatoverhaul.common.config.Config;
import committee.nova.boatoverhaul.common.key.Keys;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class BoatOverhaulClient implements ClientModInitializer {
    private static boolean allowSteeringWhenStopped;
    private static boolean reverseRudderWhenSailingAstern;
    private static int gearX;
    private static int gearY;
    private static int rudderX;
    private static int rudderY;
    private static Config config;

    @Override
    public void onInitializeClient() {
        config = Config.of("boatoverhaul-config").provider(path ->
                "# BoatOverhaul Configs" + "\n" +
                        "# Allows a boat to turn its rudder with a very small extra forward speed when the gear state is at STOP" + "\n" +
                        "allowRudderingWhenStopped=false" + "\n" +
                        "# If set to true, a boat sailing backwards and ruddering to right, for example, will sail to the left rearward" + "\n" +
                        "reverseRudderWhenSailingAstern=false" + "\n" +
                        "# The X Offset of gear indicator" + "\n" +
                        "gearX=0" + "\n" +
                        "# The Y Offset of gear indicator" + "\n" +
                        "gearY=0" + "\n" +
                        "# The X Offset of rudder indicator" + "\n" +
                        "rudderX=0" + "\n" +
                        "# The Y Offset of rudder indicator" + "\n" +
                        "rudderY=0"
        ).request();
        allowSteeringWhenStopped = config.getOrDefault("allowRudderingWhenStopped", false);
        reverseRudderWhenSailingAstern = config.getOrDefault("reverseRudderWhenSailingAstern", false);
        gearX = config.getOrDefault("gearX", 0);
        gearY = config.getOrDefault("gearY", 0);
        rudderX = config.getOrDefault("rudderX", 0);
        rudderY = config.getOrDefault("rudderY", 0);
        KeyBindingHelper.registerKeyBinding(Keys.keyLeftRudder);
        KeyBindingHelper.registerKeyBinding(Keys.keyRightRudder);
    }


    @SuppressWarnings("unused")
    public static Config getConfig() {
        return config;
    }

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
}
