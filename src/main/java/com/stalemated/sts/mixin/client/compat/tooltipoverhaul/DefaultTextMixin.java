package com.stalemated.sts.mixin.client.compat.tooltipoverhaul;

import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulLayoutFixer;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulStateManager;
import dev.xylonity.tooltipoverhaul.client.render.TooltipContext;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@RequiresMod("tooltipoverhaul")
@Pseudo
@Mixin(targets = "dev.xylonity.tooltipoverhaul.client.style.text.DefaultText")
public abstract class DefaultTextMixin {

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;renderText(Lnet/minecraft/client/gui/Font;IILorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;)V"))
    private void sts$redirectRenderText(ClientTooltipComponent instance, Font textRenderer, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource vertexConsumers) {
        int yOffset = TooltipOverhaulLayoutFixer.getTitleYAdjustment(TooltipOverhaulStateManager.getCurrentTOContext(), instance);
        instance.renderText(textRenderer, x, y + yOffset, matrix, vertexConsumers);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I"))
    private int sts$redirectDrawString(GuiGraphics instance, Font textRenderer, Component text, int x, int y, int color, boolean shadow) {
        y = TooltipOverhaulLayoutFixer.adjustDrawStringY(y, TooltipOverhaulStateManager.getCurrentTOContext());
        return instance.drawString(textRenderer, text, x, y, color, shadow);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Ldev/xylonity/tooltipoverhaul/client/util/Constants;getIconTitleSeparation(Ldev/xylonity/tooltipoverhaul/client/render/TooltipContext;)I"))
    private int sts$fixYPaddingForWrappedTitle(TooltipContext context) {
        return TooltipOverhaulLayoutFixer.fixIconTitleSeparation(context);
    }
}
