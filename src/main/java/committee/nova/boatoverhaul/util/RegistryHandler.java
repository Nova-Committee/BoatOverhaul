package committee.nova.boatoverhaul.util;

import committee.nova.boatoverhaul.BoatOverhaul;
import committee.nova.boatoverhaul.common.config.CommonConfig;
import committee.nova.boatoverhaul.common.sound.Sounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BoatOverhaul.MODID);

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.CONFIG);
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Sounds.init();
        SOUNDS.register(bus);
    }
}
