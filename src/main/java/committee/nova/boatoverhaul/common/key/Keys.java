package committee.nova.boatoverhaul.common.key;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class Keys {
    public static final KeyMapping keyLeftRudder = new KeyMapping("key.boatoverhaul.leftRudder",
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_4, "key.categories.movement");
    public static final KeyMapping keyRightRudder = new KeyMapping("key.boatoverhaul.rightRudder",
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_KP_6, "key.categories.movement");
}
