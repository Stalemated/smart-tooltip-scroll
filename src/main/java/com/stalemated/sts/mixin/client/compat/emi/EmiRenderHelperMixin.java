package com.stalemated.sts.mixin.client.compat.emi;

import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.lib.util.state.SharedTooltipState;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import dev.emi.emi.runtime.EmiDrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;

@RequiresMod("emi")
@Pseudo
@Mixin(targets = "dev.emi.emi.EmiRenderHelper")
public class EmiRenderHelperMixin {

    @ModifyVariable(method = "drawTooltip(Lnet/minecraft/client/gui/screens/Screen;Ldev/emi/emi/runtime/EmiDrawContext;Ljava/util/List;IIILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V",
            at = @At("HEAD"), argsOnly = true, ordinal = 2)
    private static int sts$adjustMaxWidth(int maxWidth, Screen screen, EmiDrawContext context, List<ClientTooltipComponent> components) {
        if (SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions && components != null && !components.isEmpty()) {
            return Integer.MAX_VALUE;
        }
        return maxWidth;
    }

    @Inject(method = "drawTooltip(Lnet/minecraft/client/gui/screens/Screen;Ldev/emi/emi/runtime/EmiDrawContext;Ljava/util/List;IIILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V",
            at = @At(value = "INVOKE", target = "Ldev/emi/emi/mixin/accessor/DrawContextAccessor;invokeDrawTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V"))
    private static void sts$beforeDrawTooltip(Screen screen, EmiDrawContext context, List<ClientTooltipComponent> components, int x, int y, int maxWidth, ClientTooltipPositioner positioner, CallbackInfo ci) {
        if (SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions) {
            SharedTooltipState.requestCustomDimensions();
        }
    }

    @Inject(method = "drawTooltip(Lnet/minecraft/client/gui/screens/Screen;Ldev/emi/emi/runtime/EmiDrawContext;Ljava/util/List;IIILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V",
            at = @At(value = "INVOKE", target = "Ldev/emi/emi/mixin/accessor/DrawContextAccessor;invokeDrawTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V", shift = At.Shift.AFTER))
    private static void sts$afterDrawTooltip(Screen screen, EmiDrawContext context, List<ClientTooltipComponent> components, int x, int y, int maxWidth, ClientTooltipPositioner positioner, CallbackInfo ci) {
        SharedTooltipState.clearCustomDimensionsRequest();
    }
}
