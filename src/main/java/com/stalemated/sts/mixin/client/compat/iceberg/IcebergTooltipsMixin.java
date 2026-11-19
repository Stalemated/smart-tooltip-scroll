package com.stalemated.sts.mixin.client.compat.iceberg;

import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.sts.compat.iceberg.IcebergCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

@RequiresMod("iceberg")
@Pseudo
@Mixin(targets = "com.anthonyhilyard.iceberg.util.Tooltips")
public abstract class IcebergTooltipsMixin {

    @ModifyArg(method = "lambda$gatherTooltipComponents$5", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;"), index = 1)
    private static int smart_tooltip_scroll$keepLinesWhole(int width) {
        return IcebergCompat.lineWidth(width);
    }

    @Inject(method = "centerTitle(Ljava/util/List;Lnet/minecraft/client/gui/Font;I)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private static void smart_tooltip_scroll$keepTitleUncentered(List<ClientTooltipComponent> components, Font font, int width, CallbackInfoReturnable<List<ClientTooltipComponent>> cir) {
        if (IcebergCompat.skipsTitleCentering(components)) cir.setReturnValue(new ArrayList<>(components));
    }

    @Inject(method = "centerTitle(Ljava/util/List;Lnet/minecraft/client/gui/Font;II)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private static void smart_tooltip_scroll$keepWrappedTitleUncentered(List<ClientTooltipComponent> components, Font font, int width, int titleLines, CallbackInfoReturnable<List<ClientTooltipComponent>> cir) {
        if (IcebergCompat.skipsTitleCentering(components)) cir.setReturnValue(new ArrayList<>(components));
    }
}
