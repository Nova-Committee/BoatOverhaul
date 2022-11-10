package committee.nova.boatoverhaul.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    public static final ForgeConfigSpec CONFIG;
    public static final ForgeConfigSpec.BooleanValue allowSteeringWhenStopped;

    static {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Boat Overhaul Common Config");
        allowSteeringWhenStopped = builder.comment("Allows a boat to steer with a very small extra forward speed when the gear state is at STOP")
                .define("allowSteeringWhenStopped", false);
        CONFIG = builder.build();
    }
}
