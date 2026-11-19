package com.stalemated.sts.mixin.client;

import com.stalemated.sts.state.TooltipContexts;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void smart_tooltip_scroll$clearTooltipContexts(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        TooltipContexts.clear();
    }
}
