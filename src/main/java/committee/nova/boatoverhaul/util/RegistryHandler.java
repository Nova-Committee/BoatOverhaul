package committee.nova.boatoverhaul.util;

import committee.nova.boatoverhaul.BoatOverhaul;
import committee.nova.boatoverhaul.common.sound.Sounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegistryHandler {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, BoatOverhaul.MOD_ID);

    public static void register(IEventBus bus, ModContainer container) {
        Sounds.init();
        SOUNDS.register(bus);
    }
}
