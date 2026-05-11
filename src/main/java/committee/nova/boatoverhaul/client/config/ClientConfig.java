package committee.nova.boatoverhaul.client.config;


import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    public static final ModConfigSpec CONFIG;
    public static final ModConfigSpec.BooleanValue allowSteeringWhenStopped;
    public static final ModConfigSpec.BooleanValue reverseRudderWhenSailingAstern;
    public static final ModConfigSpec.IntValue gearX;
    public static final ModConfigSpec.IntValue gearY;
    public static final ModConfigSpec.IntValue rudderX;
    public static final ModConfigSpec.IntValue rudderY;

    static {
        final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment("Boat Overhaul Client Config");
        allowSteeringWhenStopped = builder.comment("Allows a boat to turn its rudder with a very small extra forward speed when the gear state is at STOP")
                .define("allowRudderingWhenStopped", false);
        reverseRudderWhenSailingAstern = builder.comment("If set to true, a boat sailing backwards and ruddering to right, for example, will sail to the left rearward")
                .define("reverseRudderWhenSailingAstern", false);
        gearX = builder.comment("X offset of gear indicator").defineInRange("gearX", 0, -10000, 10000);
        gearY = builder.comment("Y offset of gear indicator").defineInRange("gearY", 0, -10000, 10000);
        rudderX = builder.comment("X offset of rudder indicator").defineInRange("rudderX", 0, -10000, 10000);
        rudderY = builder.comment("Y offset of rudder indicator").defineInRange("rudderY", 0, -10000, 10000);
        CONFIG = builder.build();
    }
}
