package committee.nova.boatoverhaul.common.sound;

import committee.nova.boatoverhaul.BoatOverhaul;
import committee.nova.boatoverhaul.util.RegistryHandler;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Objects;
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

    private static final HashMap<String, DeferredHolder<SoundEvent, SoundEvent>> soundList = new HashMap<>();

    public static void init() {
        for (final Sound sound : Sounds.values())
            soundList.put(sound.getId(), RegistryHandler.SOUNDS.register(sound.getId(), () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(BoatOverhaul.MOD_ID, sound.getId()))));
    }

    public static Optional<SoundEvent> getSound(Sound sound) {
        final SoundEvent s = soundList.get(sound.getId()).get();
        return Optional.of(s);
    }
}
