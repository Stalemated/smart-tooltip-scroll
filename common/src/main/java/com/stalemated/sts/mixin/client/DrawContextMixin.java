package com.stalemated.sts.mixin.client;

import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.resize.TooltipDimensionManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

    @Inject(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V", at = @At("HEAD"))
    private void rst$captureDrawContext(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, CallbackInfo ci) {
        TooltipDimensionManager.setState((DrawContext) (Object) this, textRenderer);
    }

    @ModifyVariable(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;II)V", at = @At("HEAD"), argsOnly = true, index = 2)
    private List<Text> rst$applyDimensionsWidth(List<Text> text) {
        return TooltipDimensionManager.enforceWidthLimit(text);
    }

    @ModifyVariable(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V", at = @At("HEAD"), argsOnly = true, index = 2)
    private List<TooltipComponent> rst$applyDimensionsHeight(List<TooltipComponent> components) {
        if (ConfigManager.getConfig().custom_tooltip_dimensions && TooltipDimensionManager.isCurrentTooltipItemTooltip) {
            return TooltipDimensionManager.enforceHeightLimit(components);
        }
        return components;
    }

    @Inject(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V", at = @At("TAIL"))
    private void rst$clearStateAfterRender(CallbackInfo ci) {
        TooltipDimensionManager.clearState();
    }
}