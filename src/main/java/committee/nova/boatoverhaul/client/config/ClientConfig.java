package committee.nova.boatoverhaul.client.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final ForgeConfigSpec CONFIG;
    public static final ForgeConfigSpec.BooleanValue allowSteeringWhenStopped;
    public static final ForgeConfigSpec.BooleanValue reverseRudderWhenSailingAstern;
    public static final ForgeConfigSpec.IntValue gearX;
    public static final ForgeConfigSpec.IntValue gearY;
    public static final ForgeConfigSpec.IntValue rudderX;
    public static final ForgeConfigSpec.IntValue rudderY;

    static {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Boat Overhaul Client Config");
        allowSteeringWhenStopped = builder.comment("Allows a boat to turn its rudder with a very small extra forward speed when the gear state is at STOP")
                .define("allowRudderingWhenStopped", false);
        reverseRudderWhenSailingAstern = builder.comment("If set to true, a boat sailing backwards and ruddering to right, for example, will sail to the left rearward")
                .define("reverseRudderWhenSailingAstern", false);
        gearX = builder.comment("X offset of gear indicator").defineInRange("gearX", 0, -10000, 10000);
        gearY = builder.comment("Y offset of gear indicator").defineInRange("gearY", 0, -10000, 10000);
        rudderX = builder.comment("X offset of rudder indicator").defineInRange("gearX", 0, -10000, 10000);
        rudderY = builder.comment("Y offset of rudder indicator").defineInRange("gearX", 0, -10000, 10000);
        CONFIG = builder.build();
    }
}
