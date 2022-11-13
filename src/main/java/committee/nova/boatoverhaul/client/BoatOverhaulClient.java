package committee.nova.boatoverhaul.client;

import committee.nova.boatoverhaul.common.key.Keys;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class BoatOverhaulClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(Keys.keyLeftRudder);
        KeyBindingHelper.registerKeyBinding(Keys.keyRightRudder);
    }
}
