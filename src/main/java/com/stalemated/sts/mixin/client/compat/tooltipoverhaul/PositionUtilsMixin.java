package com.stalemated.sts.mixin.client.compat.tooltipoverhaul;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulAlignments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@RequiresMod("tooltipoverhaul")
@Pseudo
@Mixin(targets = "dev.xylonity.tooltipoverhaul.client.util.PositionUtils")
public abstract class PositionUtilsMixin {

    @ModifyReturnValue(method = "getRatingTextAlignment", at = @At("RETURN"))
    private static String smart_tooltip_scroll$centerRating(String alignment) {
        return TooltipOverhaulAlignments.ratingAlignment(alignment);
    }

    @ModifyReturnValue(method = "getTitleTextAlignment", at = @At("RETURN"))
    private static String smart_tooltip_scroll$alignTitleLeft(String alignment) {
        return TooltipOverhaulAlignments.titleAlignment(alignment);
    }
}
