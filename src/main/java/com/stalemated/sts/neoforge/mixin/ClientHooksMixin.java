package com.stalemated.sts.neoforge.mixin;

//? if neoforge {
/*import com.stalemated.sts.forge.ForgeTooltipWrap;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(ClientHooks.class)
public abstract class ClientHooksMixin {

    @Inject(method = "gatherTooltipComponents(Lnet/minecraft/world/item/ItemStack;Ljava/util/List;Ljava/util/Optional;IIILnet/minecraft/client/gui/Font;)Ljava/util/List;", at = @At("HEAD"))
    private static void smart_tooltip_scroll$beginGather(ItemStack stack, List<? extends FormattedText> textElements, Optional<TooltipComponent> itemComponent, int mouseX, int screenWidth, int screenHeight, Font fallbackFont, CallbackInfoReturnable<List<ClientTooltipComponent>> cir) {
        ForgeTooltipWrap.beginGather(stack);
    }

    @Inject(method = "gatherTooltipComponents(Lnet/minecraft/world/item/ItemStack;Ljava/util/List;Ljava/util/Optional;IIILnet/minecraft/client/gui/Font;)Ljava/util/List;", at = @At("RETURN"))
    private static void smart_tooltip_scroll$endGather(ItemStack stack, List<? extends FormattedText> textElements, Optional<TooltipComponent> itemComponent, int mouseX, int screenWidth, int screenHeight, Font fallbackFont, CallbackInfoReturnable<List<ClientTooltipComponent>> cir) {
        ForgeTooltipWrap.endGather();
    }

    @ModifyArg(method = "splitLine(Lnet/minecraft/network/chat/FormattedText;Lnet/minecraft/client/gui/Font;I)Ljava/util/stream/Stream;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;"), index = 1)
    private static int smart_tooltip_scroll$keepLinesWhole(int width) {
        return ForgeTooltipWrap.lineWidth(width);
    }
}
*///?}
