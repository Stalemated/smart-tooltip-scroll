package com.stalemated.sts.mixin.client.compat.iceberg;

import com.stalemated.sts.util.StsManagedTitle;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Mixin(targets = "com.anthonyhilyard.iceberg.util.Tooltips")
public class IcebergTooltipsMixin {

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
