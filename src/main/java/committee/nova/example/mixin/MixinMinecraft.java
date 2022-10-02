package committee.nova.example.mixin;

import committee.nova.example.ExampleMod$;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Inject(method = "init", at = @At("HEAD"))
    public void onInit(CallbackInfo ci) {
        ExampleMod$.MODULE$.LOGGER().info("Hi from mixined scala example");
    }
}
