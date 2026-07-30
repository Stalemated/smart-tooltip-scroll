package com.stalemated.sts.mixin.client.compat.emi;

import com.stalemated.lib.util.state.SharedTooltipState;
import com.stalemated.sts.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.font.TextRenderer;

@Pseudo
@Mixin(targets = "dev.emi.emi.EmiRenderHelper")
public class EmiRenderHelperMixin {

    @Redirect(method = "drawTooltip(Lnet/minecraft/client/gui/screen/Screen;Ldev/emi/emi/runtime/EmiDrawContext;Ljava/util/List;IIILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/tooltip/OrderedTextTooltipComponent;getWidth(Lnet/minecraft/client/font/TextRenderer;)I"))
    private static int sts$preventEmiWrap(OrderedTextTooltipComponent instance, TextRenderer textRenderer) {
        if (ConfigManager.getConfig().custom_tooltip_dimensions) {
            return 0;
        }
        return instance.getWidth(textRenderer);
    }

    @Inject(method = "drawTooltip(Lnet/minecraft/client/gui/screen/Screen;Ldev/emi/emi/runtime/EmiDrawContext;Ljava/util/List;IIILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V", at = @At("HEAD"))
    private static void sts$forceDimensionsOn(CallbackInfo ci) {
        SharedTooltipState.forceCustomDimensions = true;
    }

    @Inject(method = "drawTooltip(Lnet/minecraft/client/gui/screen/Screen;Ldev/emi/emi/runtime/EmiDrawContext;Ljava/util/List;IIILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V", at = @At("TAIL"))
    private static void sts$forceDimensionsOff(CallbackInfo ci) {
        SharedTooltipState.forceCustomDimensions = false;
    }
}
