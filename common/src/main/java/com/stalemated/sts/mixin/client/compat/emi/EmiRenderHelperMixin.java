package com.stalemated.sts.mixin.client.compat.emi;

import com.stalemated.lib.util.state.SharedTooltipState;
import com.stalemated.sts.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.OrderedText;
import net.minecraft.client.font.TextRenderer;
import java.util.List;

@Pseudo
@Mixin(targets = "dev.emi.emi.EmiRenderHelper", remap = false)
public class EmiRenderHelperMixin {

    @Redirect(method = "drawTooltip(Lnet/minecraft/client/gui/screen/Screen;Ldev/emi/emi/runtime/EmiDrawContext;Ljava/util/List;IIILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;wrapLines(Lnet/minecraft/text/StringVisitable;I)Ljava/util/List;"))
    private static List<OrderedText> sts$disableEmiWrap(TextRenderer instance, StringVisitable text, int width) {
        if (ConfigManager.getConfig().custom_tooltip_dimensions) {
            return instance.wrapLines(text, Integer.MAX_VALUE);
        }
        return instance.wrapLines(text, width);
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
