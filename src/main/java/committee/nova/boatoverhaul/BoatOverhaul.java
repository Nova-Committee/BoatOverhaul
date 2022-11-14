package committee.nova.boatoverhaul;

import committee.nova.boatoverhaul.common.config.Config;
import committee.nova.boatoverhaul.util.RegistryHandler;
import net.fabricmc.api.ModInitializer;

public class BoatOverhaul implements ModInitializer {
    private static boolean allowSteeringWhenStopped;
    private static boolean reverseRudderWhenSailingAstern;
    private static Config config;

    public static final String MODID = "boatoverhaul";

    @Override
    public void onInitialize() {
        config = Config.of("boatoverhaul-config").provider(path ->
                """
                        # BoatOverhaul Configs
                        # Allows a boat to turn its rudder with a very small extra forward speed when the gear state is at STOP
                        allowRudderingWhenStopped=false
                        If set to true, a boat sailing backwards and ruddering to right, for example, will sail to the left rearward
                        reverseRudderWhenSailingAstern=false
                        """
        ).request();
        allowSteeringWhenStopped = config.getOrDefault("allowRudderingWhenStopped", false);
        reverseRudderWhenSailingAstern = config.getOrDefault("reverseRudderWhenSailingAstern", false);
        RegistryHandler.register();
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
}
