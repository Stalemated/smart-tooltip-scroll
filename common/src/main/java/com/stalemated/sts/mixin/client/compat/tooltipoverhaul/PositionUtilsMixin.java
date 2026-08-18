package com.stalemated.sts.mixin.client.compat.tooltipoverhaul;


import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulAlignments;
import dev.xylonity.tooltipoverhaul.client.render.TooltipContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "dev.xylonity.tooltipoverhaul.client.util.PositionUtils")
public abstract class PositionUtilsMixin {

    @Inject(method = "getRatingTextAlignment", at = @At("HEAD"), cancellable = true)
    private static void sts$forceRatingMiddle(TooltipContext context, CallbackInfoReturnable<String> cir) {
        String align = TooltipOverhaulAlignments.getForcedRatingAlignment();
        if (align != null) cir.setReturnValue(align);
    }

    @Inject(method = "getTitleTextAlignment", at = @At("HEAD"), cancellable = true)
    private static void sts$forceTitleLeft(TooltipContext context, CallbackInfoReturnable<String> cir) {
        String align = TooltipOverhaulAlignments.getForcedTitleAlignment();
        if (align != null) cir.setReturnValue(align);
    }
}
