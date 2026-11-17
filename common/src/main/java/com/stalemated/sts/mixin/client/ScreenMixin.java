package com.stalemated.sts.mixin.client;

import com.stalemated.sts.state.TooltipContextManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void sts$clearTrashFromStack(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        TooltipContextManager.clear();
    }
}
