package com.stalemated.sts.forge.mixin;

//? if forge {
/*import com.mojang.datafixers.util.Either;
import com.stalemated.lib.util.state.SharedTooltipState;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.state.TooltipContextManager;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Pseudo
@Mixin(targets = "net.minecraftforge.client.ForgeHooksClient")
public class ForgeHooksClientMixin {

    @Unique
    private static boolean sts$isCustomActive = false;

    @Inject(method = "gatherTooltipComponentsFromElements(Lnet/minecraft/world/item/ItemStack;Ljava/util/List;IIILnet/minecraft/client/gui/Font;)Ljava/util/List;", at = @At("HEAD"))
    private static void sts$onGatherStart(ItemStack stack, List<Either<FormattedText, TooltipComponent>> elements, int mouseX, int screenWidth, int screenHeight, Font fallbackFont, CallbackInfoReturnable<List<ClientTooltipComponent>> cir) {
        if (!SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions) {
            sts$isCustomActive = false;
            return;
        }

        boolean isItem = (stack != null && !stack.isEmpty()) || TooltipContextManager.peek() != null;
        boolean isForced = SharedTooltipState.isCustomDimensionsRequested();

        sts$isCustomActive = isItem || isForced;
    }

    @Inject(method = "gatherTooltipComponentsFromElements(Lnet/minecraft/world/item/ItemStack;Ljava/util/List;IIILnet/minecraft/client/gui/Font;)Ljava/util/List;", at = @At("RETURN"))
    private static void sts$onGatherEnd(ItemStack stack, List<Either<FormattedText, TooltipComponent>> elements, int mouseX, int screenWidth, int screenHeight, Font fallbackFont, CallbackInfoReturnable<List<ClientTooltipComponent>> cir) {
        sts$isCustomActive = false;
    }

    @Redirect(method = "splitLine(Lnet/minecraft/network/chat/FormattedText;Lnet/minecraft/client/gui/Font;I)Ljava/util/stream/Stream;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;"))
    private static List<FormattedCharSequence> sts$disableForgeWrap(Font font, FormattedText text, int width) {
        if (sts$isCustomActive) {
            return font.split(text, Integer.MAX_VALUE);
        }
        return font.split(text, width);
    }
}
*///?}
