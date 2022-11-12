package committee.nova.boatoverhaul.client.key;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class Keys {
    public static final KeyBinding keyLeftRudder = new KeyBinding("key.boatoverhaul.leftRudder",
            Keyboard.KEY_NUMPAD4, "key.categories.movement");
    public static final KeyBinding keyRightRudder = new KeyBinding("key.boatoverhaul.rightRudder",
            Keyboard.KEY_NUMPAD6, "key.categories.movement");

    public static void init() {
        ClientRegistry.registerKeyBinding(keyLeftRudder);
        ClientRegistry.registerKeyBinding(keyRightRudder);
    }
}
