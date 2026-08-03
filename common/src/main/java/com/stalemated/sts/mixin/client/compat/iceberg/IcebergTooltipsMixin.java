package com.stalemated.sts.mixin.client.compat.iceberg;

import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.util.StsManagedTitle;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.OrderedText;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Mixin(targets = "com.anthonyhilyard.iceberg.util.Tooltips")
public class IcebergTooltipsMixin {

    @Redirect(method = "lambda$gatherTooltipComponents$5", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;wrapLines(Lnet/minecraft/text/StringVisitable;I)Ljava/util/List;"))
    private static List<OrderedText> sts$disableIcebergWrap(TextRenderer instance, StringVisitable text, int width) {
        if (ConfigManager.getConfig().custom_tooltip_dimensions) {
            return instance.wrapLines(text, Integer.MAX_VALUE);
        }
        return instance.wrapLines(text, width);
    }

    @Inject(method = "centerTitle*", at = @At("HEAD"), cancellable = true)
    private static void onCenterTitle(List<TooltipComponent> components, TextRenderer font, int width, CallbackInfoReturnable<List<TooltipComponent>> cir) {
        for (TooltipComponent comp : components) {
            if (comp instanceof StsManagedTitle) {
                cir.setReturnValue(new ArrayList<>(components));
                return;
            }
        }
    }

    @Inject(method = "centerTitle*", at = @At("HEAD"), cancellable = true)
    private static void onCenterTitleWithLines(List<TooltipComponent> components, TextRenderer font, int width, int titleLines, CallbackInfoReturnable<List<TooltipComponent>> cir) {
        for (TooltipComponent comp : components) {
            if (comp instanceof StsManagedTitle) {
                cir.setReturnValue(new ArrayList<>(components));
                return;
            }
        }
    }
}
