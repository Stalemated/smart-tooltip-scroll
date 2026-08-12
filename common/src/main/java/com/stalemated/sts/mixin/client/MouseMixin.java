package com.stalemated.sts.mixin.client;

import com.stalemated.lib.util.input.KeyBindingUtil;
import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.scroll.TooltipScrollManager;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.stalemated.sts.keybind.StsKeybinds.SCROLL_BYPASS_KEY;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void sts$onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (KeyBindingUtil.isKeyDownInGui(SCROLL_BYPASS_KEY)) return;

        if (ConfigManager.getConfig().custom_tooltip_dimensions) {
            if (TooltipScrollManager.INSTANCE.scroll(vertical) && ConfigManager.getConfig().lock_container_scrolling) {
                ci.cancel();
            }
        }
    }
}