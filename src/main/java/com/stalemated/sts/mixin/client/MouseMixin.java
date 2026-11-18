package com.stalemated.sts.mixin.client;

import com.stalemated.lib.util.input.KeyBindingUtil;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.scroll.TooltipScrollManager;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.stalemated.sts.keybind.StsKeybinds.SCROLL_BYPASS_KEY;

@Mixin(MouseHandler.class)
public abstract class MouseMixin {
    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void sts$onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (KeyBindingUtil.isKeyDownInGui(SCROLL_BYPASS_KEY)) return;

        if (SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions) {
            if (TooltipScrollManager.INSTANCE.scroll(vertical) && SmartTooltipScrollConfigRegistry.CONFIG.get().lock_container_scrolling) {
                ci.cancel();
            }
        }
    }
}