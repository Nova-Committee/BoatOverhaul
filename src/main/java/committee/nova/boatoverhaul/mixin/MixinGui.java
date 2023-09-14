package committee.nova.boatoverhaul.mixin;

import committee.nova.boatoverhaul.client.overlay.init.OverlayInit;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class MixinGui {
    @Inject(method = "render", at = @At("TAIL"))
    public void inject$render(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        OverlayInit.renderBoatOverlay(guiGraphics);
    }
}
