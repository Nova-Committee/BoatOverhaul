package committee.nova.boatoverhaul.client;

import committee.nova.boatoverhaul.common.config.Config;
import net.fabricmc.api.ClientModInitializer;

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
                        speedMultiplier=1.0
                        """
        ).request();
        allowSteeringWhenStopped = config.getOrDefault("allowRudderingWhenStopped", false);
        reverseRudderWhenSailingAstern = config.getOrDefault("reverseRudderWhenSailingAstern", false);
        speedMultiplier = config.getOrDefault("speedMultiplier", 1.0);
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
