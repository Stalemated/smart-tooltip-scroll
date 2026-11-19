package com.stalemated.sts.mixin.client.compat.tooltipoverhaul;

import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulDraw;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@RequiresMod("tooltipoverhaul")
@Pseudo
@Mixin(targets = "dev.xylonity.tooltipoverhaul.client.util.TooltipScrollState")
public abstract class TooltipScrollStateMixin {

    @Inject(method = "begin", at = @At("HEAD"), cancellable = true)
    private static void smart_tooltip_scroll$disableToScroll(int contentHeight, int viewportHeight, CallbackInfo ci) {
        if (TooltipOverhaulDraw.takeOverToScroll()) ci.cancel();
    }
}
