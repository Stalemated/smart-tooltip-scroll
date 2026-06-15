package com.stalemated.sts.mixin.client;

import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.scroll.TooltipScrollManager;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void rst$onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (ConfigManager.getConfig().custom_tooltip_dimensions) {
            if (TooltipScrollManager.scroll(vertical) && ConfigManager.getConfig().lock_container_scrolling) {
                ci.cancel();
            }
        }
    }
}