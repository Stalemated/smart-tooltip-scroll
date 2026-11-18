package com.stalemated.sts.mixin.client;

import com.stalemated.lib.util.state.SharedTooltipState;
import com.stalemated.sts.compat.obscure.ObscureTooltipsCompat;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.resize.TooltipLayout;
import com.stalemated.sts.state.TooltipContext;
import com.stalemated.sts.state.TooltipContextManager;
import com.stalemated.sts.state.TooltipDraw;
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
    private void sts$captureDrawContext(Font font, List<ClientTooltipComponent> list, int i, int j, ClientTooltipPositioner clientTooltipPositioner, CallbackInfo ci) {
        if (SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions) {
            TooltipDraw.begin((GuiGraphics) (Object) this, font);
        }
    }

    @ModifyVariable(method = "renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V", at = @At("HEAD"), argsOnly = true, index = 2)
    private List<ClientTooltipComponent> sts$applyDimensionsHeight(List<ClientTooltipComponent> list) {
        if (SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions) {
            if (IS_OT_LOADED) {
                if (ObscureTooltipsCompat.isObscureHandling(list)) {
                    return list;
                }
            }

            TooltipContext ctx = TooltipContextManager.peek();

            boolean isItem = ctx != null;
            boolean isForced = SharedTooltipState.isCustomDimensionsRequested();

            if (isItem || isForced) {
                return TooltipLayout.apply(list, ctx);
            }
        }
        return list;
    }

    @Inject(method = "renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V", at = @At("TAIL"))
    private void sts$clearStateAfterRender(CallbackInfo ci) {
        TooltipDraw.end();
    }
}