package com.stalemated.sts.compat.tierify.component;

import dev.xylonity.tooltipoverhaul.client.layer.impl.BackgroundLayer;
import dev.xylonity.tooltipoverhaul.client.layer.impl.InnerOverlayLayer;
import dev.xylonity.tooltipoverhaul.client.layer.impl.OverlayLayer;
import dev.xylonity.tooltipoverhaul.client.layer.impl.ShadowLayer;
import dev.xylonity.tooltipoverhaul.client.render.TooltipContext;
import dev.xylonity.tooltipoverhaul.client.render.TooltipRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec2f;
import org.joml.Matrix4f;

import java.util.List;

public class TOTieredWrapper implements TooltipComponent {

    private final TooltipContext toContext;
    private final TooltipRenderer toRenderer;
    private static final int TOTAL_EXTRA_PADDING = 5;
    private static final int X_SHIFT = 2;
    private  static final int Y_SHIFT = 1;

    public TOTieredWrapper(List<TooltipComponent> components, DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY, TooltipPositioner positioner, ItemStack stack) {
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();

        this.toContext = new TooltipContext(context, textRenderer, components, mouseX, mouseY, screenWidth, screenHeight, positioner, stack, true);
        this.toRenderer = new TooltipRenderer(this.toContext);
        this.toRenderer.init();

        if (this.toContext.getTooltipLayers() != null) {
            this.toContext.getTooltipLayers().removeIf(layer -> 
                layer instanceof BackgroundLayer || layer instanceof OverlayLayer ||
                layer instanceof InnerOverlayLayer || layer instanceof ShadowLayer
            );
        }
    }

    @Override
    public int getHeight() {
        return (int) this.toContext.getTooltipSize().y - (this.toContext.getPaddingY() * 2) + TOTAL_EXTRA_PADDING - Y_SHIFT;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return (int) this.toContext.getTooltipSize().x - (this.toContext.getPaddingX() * 2) + TOTAL_EXTRA_PADDING - X_SHIFT;
    }

    @Override
    public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix, VertexConsumerProvider.Immediate vertexConsumers) {
        float shiftedX = x - this.toContext.getPaddingX() + X_SHIFT;
        float shiftedY = y - this.toContext.getPaddingY() + Y_SHIFT;

        this.toContext.setTooltipPosition(new Vec2f(shiftedX, shiftedY));
        this.toRenderer.render();
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {}
}
