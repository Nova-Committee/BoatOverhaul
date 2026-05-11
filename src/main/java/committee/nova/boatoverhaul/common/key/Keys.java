package committee.nova.boatoverhaul.common.key;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;

@EventBusSubscriber(net.neoforged.api.distmarker.Dist.CLIENT)
public class Keys {
    public static final KeyMapping keyLeftRudder = new KeyMapping("key.boatoverhaul.leftRudder",
            KeyConflictContext.IN_GAME, KeyModifier.NONE,
            InputConstants.Type.KEYSYM, InputConstants.KEY_NUMPAD4, KeyMapping.Category.MOVEMENT);
    public static final KeyMapping keyRightRudder = new KeyMapping("key.boatoverhaul.rightRudder",
            KeyConflictContext.IN_GAME, KeyModifier.NONE,
            InputConstants.Type.KEYSYM, InputConstants.KEY_NUMPAD6, KeyMapping.Category.MOVEMENT);

    @SubscribeEvent
    public static void onKeyRegistry(RegisterKeyMappingsEvent event) {
        event.register(keyLeftRudder);
        event.register(keyRightRudder);
    }
}
