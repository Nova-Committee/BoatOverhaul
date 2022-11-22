package committee.nova.boatoverhaul.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final ForgeConfigSpec CONFIG;
    public static final ForgeConfigSpec.BooleanValue allowSteeringWhenStopped;
    public static final ForgeConfigSpec.BooleanValue reverseRudderWhenSailingAstern;
    public static final ForgeConfigSpec.DoubleValue speedMultiplier;

    static {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Boat Overhaul Common Config");
        allowSteeringWhenStopped = builder.comment("Allows a boat to turn its rudder with a very small extra forward speed when the gear state is at STOP")
                .define("allowRudderingWhenStopped", false);
        reverseRudderWhenSailingAstern = builder.comment("If set to true, a boat sailing backwards and ruddering to right, for example, will sail to the left rearward")
                .define("reverseRudderWhenSailingAstern", false);
        speedMultiplier = builder.comment("The multiplier of the boat's max speed forward.")
                .defineInRange("speedMultiplier", 1.0, 0.1, 2.0);
        CONFIG = builder.build();
    }
}
