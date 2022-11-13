package committee.nova.boatoverhaul.common.key;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Keys {
    public static final KeyBinding keyLeftRudder = new KeyBinding("key.boatoverhaul.leftRudder",
            KeyConflictContext.IN_GAME, KeyModifier.NONE,
            InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_KP_4, "key.categories.movement");
    public static final KeyBinding keyRightRudder = new KeyBinding("key.boatoverhaul.rightRudder",
            KeyConflictContext.IN_GAME, KeyModifier.NONE,
            InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_KP_6, "key.categories.movement");

    @SubscribeEvent
    public static void onKeyRegistry(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientRegistry.registerKeyBinding(keyLeftRudder);
            ClientRegistry.registerKeyBinding(keyRightRudder);
        });
    }
}
