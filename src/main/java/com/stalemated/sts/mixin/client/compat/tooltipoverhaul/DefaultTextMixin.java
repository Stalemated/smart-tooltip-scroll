package com.stalemated.sts.mixin.client.compat.tooltipoverhaul;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulLayoutFixer;
import dev.xylonity.tooltipoverhaul.client.render.TooltipContext;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@RequiresMod("tooltipoverhaul")
@Pseudo
@Mixin(targets = "dev.xylonity.tooltipoverhaul.client.style.text.DefaultText")
public abstract class DefaultTextMixin {

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;renderText(Lnet/minecraft/client/gui/Font;IILorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;)V"))
    private void smart_tooltip_scroll$moveTitleText(ClientTooltipComponent title, Font font, int x, int y, Matrix4f matrix4f, MultiBufferSource.BufferSource bufferSource, Operation<Void> original) {
        original.call(title, font, x, TooltipOverhaulLayoutFixer.titleTextY(title, y), matrix4f, bufferSource);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I"), index = 3)
    private int smart_tooltip_scroll$moveRatingText(int y) {
        return TooltipOverhaulLayoutFixer.adjustDrawStringY(y);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Ldev/xylonity/tooltipoverhaul/client/util/Constants;getIconTitleSeparation(Ldev/xylonity/tooltipoverhaul/client/render/TooltipContext;)I"))
    private int smart_tooltip_scroll$fixYPaddingForWrappedTitle(TooltipContext context, Operation<Integer> original) {
        return TooltipOverhaulLayoutFixer.fixIconTitleSeparation(context, original.call(context));
    }
}
