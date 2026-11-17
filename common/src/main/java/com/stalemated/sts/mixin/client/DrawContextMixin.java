package com.stalemated.sts.mixin.client;

import com.stalemated.lib.util.state.SharedTooltipState;
import com.stalemated.sts.compat.obscure.ObscureTooltipsCompat;
import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.resize.TooltipDimensionManager;
import com.stalemated.sts.state.TooltipContext;
import com.stalemated.sts.state.TooltipContextManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;

import static com.stalemated.sts.state.StateManager.IS_OT_LOADED;

@Mixin(GuiGraphics.class)
public abstract class DrawContextMixin {

    @Inject(method = "renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V", at = @At("HEAD"))
    private void sts$captureDrawContext(Font textRenderer, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, CallbackInfo ci) {
        if (ConfigManager.getConfig().custom_tooltip_dimensions) {
            TooltipDimensionManager.setState((GuiGraphics) (Object) this, textRenderer);
        }
    }

    @ModifyVariable(method = "renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V", at = @At("HEAD"), argsOnly = true, index = 2)
    private List<ClientTooltipComponent> sts$applyDimensionsHeight(List<ClientTooltipComponent> components) {
        if (ConfigManager.getConfig().custom_tooltip_dimensions) {
            if (IS_OT_LOADED) {
                if (ObscureTooltipsCompat.isObscureHandling(components)) {
                    return components;
                }
            }

            TooltipContext ctx = TooltipContextManager.peek();

            boolean isItem = ctx != null;
            boolean isForced = SharedTooltipState.forceCustomDimensions;

            if (isItem || isForced) {
                return TooltipDimensionManager.enforceHeightLimit(components, ctx);
            }
        }
        return components;
    }

    @Inject(method = "renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V", at = @At("TAIL"))
    private void sts$clearStateAfterRender(CallbackInfo ci) {
        if (ConfigManager.getConfig().custom_tooltip_dimensions) {
            TooltipDimensionManager.clearState();
            TooltipContextManager.pop();
            SharedTooltipState.forceCustomDimensions = false;
        }
    }
}