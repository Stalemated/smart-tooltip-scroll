package com.stalemated.sts.forge.mixin;

//? if forge {
/*import com.mojang.datafixers.util.Either;
import com.stalemated.sts.forge.ForgeTooltipWrap;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Pseudo
@Mixin(targets = "net.minecraftforge.client.ForgeHooksClient")
public class ForgeHooksClientMixin {

    @Inject(method = "gatherTooltipComponentsFromElements(Lnet/minecraft/world/item/ItemStack;Ljava/util/List;IIILnet/minecraft/client/gui/Font;)Ljava/util/List;", at = @At("HEAD"))
    private static void smart_tooltip_scroll$beginGather(ItemStack stack, List<Either<FormattedText, TooltipComponent>> elements, int mouseX, int screenWidth, int screenHeight, Font fallbackFont, CallbackInfoReturnable<List<ClientTooltipComponent>> cir) {
        ForgeTooltipWrap.beginGather(stack);
    }

    @Inject(method = "gatherTooltipComponentsFromElements(Lnet/minecraft/world/item/ItemStack;Ljava/util/List;IIILnet/minecraft/client/gui/Font;)Ljava/util/List;", at = @At("RETURN"))
    private static void smart_tooltip_scroll$endGather(ItemStack stack, List<Either<FormattedText, TooltipComponent>> elements, int mouseX, int screenWidth, int screenHeight, Font fallbackFont, CallbackInfoReturnable<List<ClientTooltipComponent>> cir) {
        ForgeTooltipWrap.endGather();
    }

    @ModifyArg(method = "splitLine(Lnet/minecraft/network/chat/FormattedText;Lnet/minecraft/client/gui/Font;I)Ljava/util/stream/Stream;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;"), index = 1)
    private static int smart_tooltip_scroll$keepLinesWhole(int width) {
        return ForgeTooltipWrap.lineWidth(width);
    }
}
*///?}
