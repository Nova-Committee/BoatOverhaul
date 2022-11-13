package committee.nova.boatoverhaul.common.key;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Keys {
    public static final KeyMapping keyLeftRudder = new KeyMapping("key.boatoverhaul.leftRudder",
            KeyConflictContext.IN_GAME, KeyModifier.NONE,
            InputConstants.Type.KEYSYM, InputConstants.KEY_NUMPAD4, "key.categories.movement");
    public static final KeyMapping keyRightRudder = new KeyMapping("key.boatoverhaul.rightRudder",
            KeyConflictContext.IN_GAME, KeyModifier.NONE,
            InputConstants.Type.KEYSYM, InputConstants.KEY_NUMPAD6, "key.categories.movement");

    @SubscribeEvent
    public static void onKeyRegistry(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientRegistry.registerKeyBinding(keyLeftRudder);
            ClientRegistry.registerKeyBinding(keyRightRudder);
        });
    }
}
