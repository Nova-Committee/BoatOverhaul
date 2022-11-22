package committee.nova.boatoverhaul.client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.SoundEvent;

public class SoundUtil {
    public static void playUISound(SoundEvent sound) {
        Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(sound, 1.0F));
    }
}
