package com.stalemated.sts.forge.mixin;

import com.mojang.datafixers.util.Either;
import com.stalemated.lib.util.state.SharedTooltipState;
import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.resize.enforce.TooltipEnforcerRegistry;
import com.stalemated.sts.state.TooltipContextManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ForgeHooksClient.class)
public class ForgeHooksClientMixin {

    @Unique
    private static boolean sts$isCustomActive = false;

    @Inject(method = "gatherTooltipComponentsFromElements(Lnet/minecraft/item/ItemStack;Ljava/util/List;IIILnet/minecraft/client/font/TextRenderer;)Ljava/util/List;", at = @At("HEAD"), remap = false)
    private static void sts$onGatherStart(ItemStack stack, List<Either<StringVisitable, TooltipData>> elements, int mouseX, int screenWidth, int screenHeight, TextRenderer fallbackFont, CallbackInfoReturnable<List<TooltipComponent>> cir) {
        if (!ConfigManager.getConfig().custom_tooltip_dimensions) {
            sts$isCustomActive = false;
            return;
        }

        boolean isItem = (stack != null && !stack.isEmpty()) || TooltipContextManager.peek() != null;
        boolean isForced = SharedTooltipState.forceCustomDimensions;

        boolean isEnforced = false;
        if (!isItem && !isForced && elements != null && !elements.isEmpty()) {
            List<TooltipComponent> components = new ArrayList<>();
            for (Either<StringVisitable, TooltipData> elem : elements) {
                elem.ifLeft(text -> {
                    if (text instanceof Text t) {
                        components.add(TooltipComponent.of(t.asOrderedText()));
                    } else if (text != null) {
                        components.add(TooltipComponent.of(OrderedText.styledForwardsVisitedString(text.getString(), Style.EMPTY)));
                    }
                });
                elem.ifRight(data -> {
                    if (data instanceof TooltipComponent comp) {
                        components.add(comp);
                    } else if (data != null) {
                        try {
                            components.add(TooltipComponent.of(data));
                        } catch (Throwable ignored) {}
                    }
                });
            }
            isEnforced = TooltipEnforcerRegistry.isEnforced(components);
        }

        sts$isCustomActive = isItem || isForced || isEnforced;
    }

    @Inject(method = "gatherTooltipComponentsFromElements(Lnet/minecraft/item/ItemStack;Ljava/util/List;IIILnet/minecraft/client/font/TextRenderer;)Ljava/util/List;", at = @At("RETURN"), remap = false)
    private static void sts$onGatherEnd(ItemStack stack, List<Either<StringVisitable, TooltipData>> elements, int mouseX, int screenWidth, int screenHeight, TextRenderer fallbackFont, CallbackInfoReturnable<List<TooltipComponent>> cir) {
        sts$isCustomActive = false;
    }

    @Redirect(method = "splitLine(Lnet/minecraft/text/StringVisitable;Lnet/minecraft/client/font/TextRenderer;I)Ljava/util/stream/Stream;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;wrapLines(Lnet/minecraft/text/StringVisitable;I)Ljava/util/List;"), remap = false)
    private static List<OrderedText> sts$disableForgeWrap(TextRenderer font, StringVisitable text, int width) {
        if (sts$isCustomActive) {
            return font.wrapLines(text, Integer.MAX_VALUE);
        }
        return font.wrapLines(text, width);
    }
}
