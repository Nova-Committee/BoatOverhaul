package committee.nova.boatoverhaul.common.sound;

import committee.nova.boatoverhaul.BoatOverhaul;
import committee.nova.boatoverhaul.util.RegistryHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Optional;

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

    private static final HashMap<String, RegistryObject<SoundEvent>> soundList = new HashMap<>();

    public static void init() {
        for (final Sound sound : Sounds.values())
            soundList.put(sound.getId(), RegistryHandler.SOUNDS.register(sound.getId(), () -> new SoundEvent(new ResourceLocation(BoatOverhaul.MODID, sound.getId()))));
    }

    public static Optional<SoundEvent> getSound(Sound sound) {
        final SoundEvent s = soundList.get(sound.getId()).orElse(null);
        return s == null ? Optional.empty() : Optional.of(s);
    }
}
