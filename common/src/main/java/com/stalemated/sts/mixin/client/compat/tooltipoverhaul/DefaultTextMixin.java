package com.stalemated.sts.mixin.client.compat.tooltipoverhaul;

import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulLayoutFixer;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulStateManager;
import dev.xylonity.tooltipoverhaul.client.render.TooltipContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "dev.xylonity.tooltipoverhaul.client.style.text.DefaultText")
public abstract class DefaultTextMixin {

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/tooltip/TooltipComponent;drawText(Lnet/minecraft/client/font/TextRenderer;IILorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;)V"))
    private void sts$redirectRenderText(TooltipComponent instance, TextRenderer textRenderer, int x, int y, Matrix4f matrix, VertexConsumerProvider.Immediate vertexConsumers) {
        int yOffset = TooltipOverhaulLayoutFixer.getTitleYAdjustment(TooltipOverhaulStateManager.getCurrentTOContext(), instance);
        instance.drawText(textRenderer, x, y + yOffset, matrix, vertexConsumers);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I"))
    private int sts$redirectDrawString(DrawContext instance, TextRenderer textRenderer, Text text, int x, int y, int color, boolean shadow) {
        y = TooltipOverhaulLayoutFixer.adjustDrawStringY(y, TooltipOverhaulStateManager.getCurrentTOContext());
        return instance.drawText(textRenderer, text, x, y, color, shadow);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Ldev/xylonity/tooltipoverhaul/client/util/Constants;getIconTitleSeparation(Ldev/xylonity/tooltipoverhaul/client/render/TooltipContext;)I", ordinal = 1))
    private int sts$fixYPaddingForWrappedTitle(TooltipContext context) {
        return TooltipOverhaulLayoutFixer.fixIconTitleSeparation(context);
    }
}
