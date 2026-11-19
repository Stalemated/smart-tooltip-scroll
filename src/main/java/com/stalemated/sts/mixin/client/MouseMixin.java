package com.stalemated.sts.mixin.client;

import com.stalemated.sts.scroll.TooltipScrollManager;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseMixin {
    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void smart_tooltip_scroll$scrollTooltip(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (TooltipScrollManager.INSTANCE.consumeWheel(vertical)) ci.cancel();
    }
}
