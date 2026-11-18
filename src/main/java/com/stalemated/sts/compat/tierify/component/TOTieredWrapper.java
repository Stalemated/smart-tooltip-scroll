package com.stalemated.sts.compat.tierify.component;

import dev.xylonity.tooltipoverhaul.client.layer.impl.BackgroundLayer;
import dev.xylonity.tooltipoverhaul.client.layer.impl.InnerOverlayLayer;
import dev.xylonity.tooltipoverhaul.client.layer.impl.OverlayLayer;
import dev.xylonity.tooltipoverhaul.client.layer.impl.ShadowLayer;
import dev.xylonity.tooltipoverhaul.client.render.TooltipContext;
import dev.xylonity.tooltipoverhaul.client.render.TooltipRenderer;
import org.joml.Matrix4f;

import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;

public class TOTieredWrapper implements ClientTooltipComponent {

    private final TooltipContext toContext;
    private final TooltipRenderer toRenderer;
    private static final int TOTAL_EXTRA_PADDING = 5;
    private static final int X_SHIFT = 2;
    private  static final int Y_SHIFT = 1;

    public TOTieredWrapper(List<ClientTooltipComponent> components, GuiGraphics context, Font textRenderer, int mouseX, int mouseY, ClientTooltipPositioner positioner, ItemStack stack) {
        int screenWidth = context.guiWidth();
        int screenHeight = context.guiHeight();

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
    public int getWidth(Font textRenderer) {
        return (int) this.toContext.getTooltipSize().x - (this.toContext.getPaddingX() * 2) + TOTAL_EXTRA_PADDING - X_SHIFT;
    }

    @Override
    public void renderText(Font textRenderer, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource vertexConsumers) {
        float shiftedX = x - this.toContext.getPaddingX() + X_SHIFT;
        float shiftedY = y - this.toContext.getPaddingY() + Y_SHIFT;

        this.toContext.setTooltipPosition(new Vec2(shiftedX, shiftedY));
        this.toRenderer.render();
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {}
}
