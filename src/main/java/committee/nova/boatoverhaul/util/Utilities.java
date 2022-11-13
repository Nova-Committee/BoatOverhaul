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
        if (s instanceof Gear) {
            final Gear g = (Gear) s;
            switch (g.getNumerator()) {
                case -1: {
                    option = Sounds.getSound(Sounds.GEAR_ASTERN);
                    break;
                }
                case 1: {
                    option = Sounds.getSound(Sounds.GEAR_AHEAD_1);
                    break;
                }
                case 2: {
                    option = Sounds.getSound(Sounds.GEAR_AHEAD_2);
                    break;
                }
                case 3: {
                    option = Sounds.getSound(Sounds.GEAR_AHEAD_3);
                    break;
                }
                case 4: {
                    option = Sounds.getSound(Sounds.GEAR_AHEAD_4);
                    break;
                }
                default:
                    option = Sounds.getSound(Sounds.GEAR_STOP);
            }
        }
        if (s instanceof Rudder) {
            final Rudder r = (Rudder) s;
            switch (r.getNumerator()) {
                case -2:
                case 2: {
                    option = Sounds.getSound(Sounds.RUDDER_FULL);
                    break;
                }
                default:
                    option = Sounds.getSound(Sounds.RUDDER_HALF);
            }
        }
        return option;
    }
}
