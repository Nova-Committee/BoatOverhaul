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
        if (s instanceof Gear g) {
            switch (g.getNumerator()) {
                case -1 -> option = Sounds.getSound(Sounds.GEAR_ASTERN);
                case 1 -> option = Sounds.getSound(Sounds.GEAR_AHEAD_1);
                case 2 -> option = Sounds.getSound(Sounds.GEAR_AHEAD_2);
                case 3 -> option = Sounds.getSound(Sounds.GEAR_AHEAD_3);
                case 4 -> option = Sounds.getSound(Sounds.GEAR_AHEAD_4);
                default -> option = Sounds.getSound(Sounds.GEAR_STOP);
            }
        }
        if (s instanceof Rudder r) {
            switch (r.getNumerator()) {
                case -2, 2 -> option = Sounds.getSound(Sounds.RUDDER_FULL);
                default -> option = Sounds.getSound(Sounds.RUDDER_HALF);
            }
        }
        return option;
    }
}
