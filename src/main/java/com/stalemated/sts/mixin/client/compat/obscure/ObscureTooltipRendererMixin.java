package com.stalemated.sts.mixin.client.compat.obscure;

import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.lib.util.state.SharedTooltipState;
import com.stalemated.sts.compat.obscure.ObscureTooltipsCompat;
import com.stalemated.sts.resize.TooltipDimensionManager;
import com.stalemated.sts.state.TooltipContextManager;
import dev.obscuria.tooltips.client.TooltipHelper;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.tooltip.TooltipScroll;
import dev.obscuria.tooltips.client.tooltip.layout.TooltipLayout;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;

@RequiresMod("obscure_tooltips")
@Pseudo
@Mixin(targets = "dev.obscuria.tooltips.client.TooltipRenderer")
public abstract class ObscureTooltipRendererMixin {

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)Z", at = @At("HEAD"))
    private static void sts$onRenderHead(GuiGraphics graphics, Font font, List<ClientTooltipComponent> components, int mouseX, int mouseY, ClientTooltipPositioner positioner, CallbackInfoReturnable<Boolean> cir) {
        if (ObscureTooltipsCompat.isCompatActive()) {
            TooltipDimensionManager.setState(graphics, font);
        }
    }

    @Redirect(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)Z", at = @At(value = "INVOKE", target = "Ldev/obscuria/tooltips/client/TooltipHelper;wrapLines(Lnet/minecraft/client/gui/GuiGraphics;Ljava/util/List;Lnet/minecraft/client/gui/Font;)Ljava/util/List;"))
    private static List<ClientTooltipComponent> sts$redirectWrapLines(GuiGraphics graphics, List<ClientTooltipComponent> components, Font font) {
        if (ObscureTooltipsCompat.isCompatActive()) {
            return components;
        }
        return TooltipHelper.wrapLines(graphics, components, font);
    }

    @Redirect(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)Z", at = @At(value = "INVOKE", target = "Ldev/obscuria/tooltips/client/tooltip/layout/TooltipLayout;rawProcessPostWrap(Ldev/obscuria/tooltips/client/TooltipState;Ljava/util/List;Lnet/minecraft/client/gui/Font;)Ljava/util/List;"))
    private static List<ClientTooltipComponent> sts$redirectPostWrap(TooltipLayout<TooltipState> instance, TooltipState state, List<ClientTooltipComponent> components, Font font) {
        List<ClientTooltipComponent> postWrapped = instance.rawProcessPostWrap(state, components, font);
        if (ObscureTooltipsCompat.isCompatActive()) {
            return ObscureTooltipsCompat.processComponents(postWrapped, font);
        }
        return postWrapped;
    }

    @Redirect(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)Z", at = @At(value = "INVOKE", target = "Ldev/obscuria/tooltips/client/tooltip/TooltipScroll;update(Ldev/obscuria/tooltips/client/TooltipState;II)V"))
    private static void sts$suppressScrollUpdate(TooltipState state, int tooltipHeight, int screenHeight) {
        if (ObscureTooltipsCompat.isCompatActive()) {
            return;
        }
        TooltipScroll.update(state, tooltipHeight, screenHeight);
    }

    @Redirect(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)Z", at = @At(value = "INVOKE", target = "Ldev/obscuria/tooltips/client/tooltip/TooltipScroll;getScroll()F")
    )
    private static float sts$redirectGetScroll() {
        if (ObscureTooltipsCompat.isCompatActive()) {
            return 0.0F;
        }
        return TooltipScroll.getScroll();
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)Z", at = @At("RETURN"))
    private static void sts$onRenderReturn(CallbackInfoReturnable<Boolean> cir) {
        if (ObscureTooltipsCompat.isCompatActive() && cir.getReturnValueZ()) {
            TooltipDimensionManager.clearState();
            TooltipContextManager.pop();
            SharedTooltipState.clearCustomDimensionsRequest();
        }
    }
}
