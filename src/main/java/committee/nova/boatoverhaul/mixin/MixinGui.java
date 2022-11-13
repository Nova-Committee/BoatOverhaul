package committee.nova.boatoverhaul.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import committee.nova.boatoverhaul.client.overlay.init.OverlayInit;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class MixinGui {
    @Inject(method = "render", at = @At("TAIL"))
    public void inject$render(PoseStack poseStack, float f, CallbackInfo ci) {
        OverlayInit.renderBoatOverlay(poseStack);
    }
}
