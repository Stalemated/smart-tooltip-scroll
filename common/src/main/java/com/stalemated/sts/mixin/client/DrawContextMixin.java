package com.stalemated.sts.mixin.client;

import com.stalemated.lib.util.state.SharedTooltipState;
import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.resize.TooltipDimensionManager;
import com.stalemated.sts.state.TooltipContext;
import com.stalemated.sts.state.TooltipContextManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
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
        if (ConfigManager.getConfig().custom_tooltip_dimensions) {
            TooltipDimensionManager.setState((DrawContext) (Object) this, textRenderer);
        }
    }

    @ModifyVariable(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V", at = @At("HEAD"), argsOnly = true, index = 2)
    private List<TooltipComponent> rst$applyDimensionsHeight(List<TooltipComponent> components) {
        if (ConfigManager.getConfig().custom_tooltip_dimensions) {
            TooltipContext ctx = TooltipContextManager.peek();

            boolean isItem = ctx != null;
            boolean isForced = SharedTooltipState.forceCustomDimensions;

            if (isItem || isForced) {
                return TooltipDimensionManager.enforceHeightLimit(components, ctx);
            }
        }
        return components;
    }

    @Inject(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V", at = @At("TAIL"))
    private void rst$clearStateAfterRender(CallbackInfo ci) {
        if (ConfigManager.getConfig().custom_tooltip_dimensions) {
            TooltipDimensionManager.clearState();
            TooltipContextManager.pop();
            SharedTooltipState.forceCustomDimensions = false;
        }
    }
}