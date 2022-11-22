package committee.nova.boatoverhaul.client;

import committee.nova.boatoverhaul.common.config.Config;
import committee.nova.boatoverhaul.common.key.Keys;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class BoatOverhaulClient implements ClientModInitializer {
    private static boolean allowSteeringWhenStopped;
    private static boolean reverseRudderWhenSailingAstern;
    private static double speedMultiplier;
    private static Config config;

    @Override
    public void onInitializeClient() {
        config = Config.of("boatoverhaul-config").provider(path ->
                """
                        # BoatOverhaul Configs
                        # Allows a boat to turn its rudder with a very small extra forward speed when the gear state is at STOP
                        allowRudderingWhenStopped=false
                        # If set to true, a boat sailing backwards and ruddering to right, for example, will sail to the left rearward
                        reverseRudderWhenSailingAstern=false
                        # The multiplier of the maximum sailing speed
                        """
        ).request();
        allowSteeringWhenStopped = config.getOrDefault("allowRudderingWhenStopped", false);
        reverseRudderWhenSailingAstern = config.getOrDefault("reverseRudderWhenSailingAstern", false);
        speedMultiplier = config.getOrDefault("speedMultiplier", 1.0);
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

    public static double getSpeedMultiplier() {
        return speedMultiplier;
    }
}
