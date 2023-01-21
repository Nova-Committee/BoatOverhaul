package committee.nova.boatoverhaul.util;

import committee.nova.boatoverhaul.api.common.boat.shiftable.IShiftable;
import committee.nova.boatoverhaul.common.boat.gear.Gear;
import committee.nova.boatoverhaul.common.boat.gear.Rudder;
import committee.nova.boatoverhaul.common.sound.Sounds;
import net.minecraft.sounds.SoundEvent;

import java.util.Optional;

public class Utilities {
    public static Optional<SoundEvent> getSoundFromShiftable(IShiftable s) {
        Optional<SoundEvent> option = Optional.empty();
        if (s instanceof final Gear g) {
            switch (g.getNumerator()) {
                case -4 -> option = Sounds.getSound(Sounds.GEAR_ASTERN);
                case 4 -> option = Sounds.getSound(Sounds.GEAR_AHEAD_1);
                case 8 -> option = Sounds.getSound(Sounds.GEAR_AHEAD_2);
                case 12 -> option = Sounds.getSound(Sounds.GEAR_AHEAD_3);
                case 16 -> option = Sounds.getSound(Sounds.GEAR_AHEAD_4);
                default -> option = Sounds.getSound(Sounds.GEAR_STOP);
            }
        }
        if (s instanceof final Rudder r) {
            switch (r.getNumerator()) {
                case -8, 8 -> option = Sounds.getSound(Sounds.RUDDER_FULL);
                default -> option = Sounds.getSound(Sounds.RUDDER_HALF);
            }
        }
        return option;
    }
}
