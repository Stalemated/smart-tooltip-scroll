package com.stalemated.sts.mixin.client.compat.obscure;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.sts.compat.obscure.ObscureTooltipsCompat;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.tooltip.layout.TooltipLayout;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
    private static void smart_tooltip_scroll$beginDraw(GuiGraphics graphics, Font font, List<ClientTooltipComponent> components, int mouseX, int mouseY, ClientTooltipPositioner positioner, CallbackInfoReturnable<Boolean> cir) {
        ObscureTooltipsCompat.beginDraw(graphics, font);
    }

    @WrapOperation(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)Z", at = @At(value = "INVOKE", target = "Ldev/obscuria/tooltips/client/TooltipHelper;wrapLines(Lnet/minecraft/client/gui/GuiGraphics;Ljava/util/List;Lnet/minecraft/client/gui/Font;)Ljava/util/List;"))
    private static List<ClientTooltipComponent> smart_tooltip_scroll$keepLinesWhole(GuiGraphics graphics, List<ClientTooltipComponent> components, Font font, Operation<List<ClientTooltipComponent>> original) {
        if (ObscureTooltipsCompat.isCompatActive()) return components;
        return original.call(graphics, components, font);
    }

    @WrapOperation(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)Z", at = @At(value = "INVOKE", target = "Ldev/obscuria/tooltips/client/tooltip/layout/TooltipLayout;rawProcessPostWrap(Ldev/obscuria/tooltips/client/TooltipState;Ljava/util/List;Lnet/minecraft/client/gui/Font;)Ljava/util/List;"))
    private static List<ClientTooltipComponent> smart_tooltip_scroll$layOut(TooltipLayout<TooltipState> layout, TooltipState state, List<ClientTooltipComponent> components, Font font, Operation<List<ClientTooltipComponent>> original) {
        return ObscureTooltipsCompat.layOut(original.call(layout, state, components, font), font);
    }

    @WrapWithCondition(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)Z", at = @At(value = "INVOKE", target = "Ldev/obscuria/tooltips/client/tooltip/TooltipScroll;update(Ldev/obscuria/tooltips/client/TooltipState;II)V"))
    private static boolean smart_tooltip_scroll$keepObscureScroll(TooltipState state, int tooltipHeight, int screenHeight) {
        return !ObscureTooltipsCompat.isCompatActive();
    }

    @ModifyExpressionValue(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)Z", at = @At(value = "INVOKE", target = "Ldev/obscuria/tooltips/client/tooltip/TooltipScroll;getScroll()F"))
    private static float smart_tooltip_scroll$scroll(float obscureScroll) {
        return ObscureTooltipsCompat.scroll(obscureScroll);
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)Z", at = @At("RETURN"))
    private static void smart_tooltip_scroll$endDraw(CallbackInfoReturnable<Boolean> cir) {
        ObscureTooltipsCompat.endDraw(cir.getReturnValueZ());
    }
}
