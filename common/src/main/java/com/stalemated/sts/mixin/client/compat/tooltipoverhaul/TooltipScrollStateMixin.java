package com.stalemated.sts.mixin.client.compat.tooltipoverhaul;

import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "dev.xylonity.tooltipoverhaul.client.util.TooltipScrollState")
public class TooltipScrollStateMixin {

    @Inject(method = "begin", at = @At("HEAD"), cancellable = true)
    private static void sts$disableToScroll(int contentHeight, int viewportHeight, CallbackInfo ci) {
        TooltipOverhaulStateManager.disableToScroll(ci);
    }
}
