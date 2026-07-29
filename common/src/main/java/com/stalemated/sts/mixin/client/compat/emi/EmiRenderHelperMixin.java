package com.stalemated.sts.mixin.client.compat.emi;

import com.stalemated.lib.util.state.SharedTooltipState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "dev.emi.emi.EmiRenderHelper", remap = false)
public class EmiRenderHelperMixin {

    @Inject(method = "drawTooltip(Lnet/minecraft/client/gui/screen/Screen;Ldev/emi/emi/runtime/EmiDrawContext;Ljava/util/List;IIILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V", at = @At("HEAD"))
    private static void sts$forceDimensionsOn(CallbackInfo ci) {
        SharedTooltipState.forceCustomDimensions = true;
    }

    @Inject(method = "drawTooltip(Lnet/minecraft/client/gui/screen/Screen;Ldev/emi/emi/runtime/EmiDrawContext;Ljava/util/List;IIILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V", at = @At("TAIL"))
    private static void sts$forceDimensionsOff(CallbackInfo ci) {
        SharedTooltipState.forceCustomDimensions = false;
    }
}
