package com.stalemated.sts.mixin.client.compat.iceberg;

import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.util.StsManagedTitle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

@RequiresMod("iceberg")
@Pseudo
@Mixin(targets = "com.anthonyhilyard.iceberg.util.Tooltips")
public class IcebergTooltipsMixin {

    @Redirect(method = "lambda$gatherTooltipComponents$5", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;"))
    private static List<FormattedCharSequence> sts$disableIcebergWrap(Font instance, FormattedText text, int width) {
        if (SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions) {
            return instance.split(text, Integer.MAX_VALUE);
        }
        return instance.split(text, width);
    }

    @Inject(method = "centerTitle(Ljava/util/List;Lnet/minecraft/client/gui/Font;I)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private static void onCenterTitle(List<ClientTooltipComponent> components, Font font, int width, CallbackInfoReturnable<List<ClientTooltipComponent>> cir) {
        sts$handleCenterTitle(components, cir);
    }

    @Inject(method = "centerTitle(Ljava/util/List;Lnet/minecraft/client/gui/Font;II)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private static void onCenterTitleWithLines(List<ClientTooltipComponent> components, Font font, int width, int titleLines, CallbackInfoReturnable<List<ClientTooltipComponent>> cir) {
        sts$handleCenterTitle(components, cir);
    }

    @Unique
    private static void sts$handleCenterTitle(List<ClientTooltipComponent> components, CallbackInfoReturnable<List<ClientTooltipComponent>> cir) {
        if (SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions) {
            if (SmartTooltipScrollConfigRegistry.CONFIG.get().title_centering) {
                cir.setReturnValue(new ArrayList<>(components));
                return;
            }

            for (ClientTooltipComponent comp : components) {
                if (comp instanceof StsManagedTitle) {
                    cir.setReturnValue(new ArrayList<>(components));
                    return;
                }
            }
        }
    }
}
