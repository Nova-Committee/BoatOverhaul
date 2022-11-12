package committee.nova.boatoverhaul.common.sound;

import committee.nova.boatoverhaul.BoatOverhaul;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Optional;

@Mod.EventBusSubscriber
public enum Sounds implements Sound {
    GEAR_ASTERN("gear_astern"),
    GEAR_STOP("gear_stop"),
    GEAR_AHEAD_1("gear_ahead_1"),
    GEAR_AHEAD_2("gear_ahead_2"),
    GEAR_AHEAD_3("gear_ahead_3"),
    GEAR_AHEAD_4("gear_ahead_4"),
    RUDDER_HALF("rudder_half"),
    RUDDER_FULL("rudder_full");

    Sounds(String id) {
        this.id = id;
    }

    private final String id;

    @Override
    public String getId() {
        return id;
    }

    private static final HashMap<String, SoundEvent> soundList = new HashMap<>();

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        for (final Sound sound : Sounds.values()) {
            final ResourceLocation n = new ResourceLocation(BoatOverhaul.MODID, sound.getId());
            final SoundEvent s = new SoundEvent(n).setRegistryName(n);
            event.getRegistry().register(s);
            soundList.put(sound.getId(), s);
        }

    }

    public static Optional<SoundEvent> getSound(Sound sound) {
        final SoundEvent s = soundList.getOrDefault(sound.getId(), null);
        return s == null ? Optional.empty() : Optional.of(s);
    }
}
