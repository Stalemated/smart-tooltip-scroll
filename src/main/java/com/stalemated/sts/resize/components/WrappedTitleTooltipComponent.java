package com.stalemated.sts.resize.components;

import com.stalemated.sts.util.StsManagedTitle;
import org.joml.Matrix4f;

import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

public class WrappedTitleTooltipComponent extends ClientTextTooltip implements ClientTooltipComponent, StsManagedTitle {

    private final List<ClientTooltipComponent> wrappedLines;

    public WrappedTitleTooltipComponent(List<ClientTooltipComponent> wrappedLines) {
        super(Component.empty().getVisualOrderText());
        this.wrappedLines = wrappedLines;
    }

    public List<ClientTooltipComponent> getWrappedLines() {
        return this.wrappedLines;
    }

    @Override
    public int getWidth(Font textRenderer) {
        int maxWidth = 0;
        for (ClientTooltipComponent line : this.wrappedLines) {
            maxWidth = Math.max(maxWidth, line.getWidth(textRenderer));
        }
        return maxWidth;
    }

    public int getFirstLineHeight() {
        if (!this.wrappedLines.isEmpty()) {
            return this.wrappedLines.get(0).getHeight();
        }
        return 10;
    }

    @Override
    public int getHeight() {
        int totalHeight = 0;
        for (ClientTooltipComponent wrappedLine : this.wrappedLines) {
            totalHeight += wrappedLine.getHeight();
        }
        return totalHeight;
    }

    @Override
    public void renderText(Font textRenderer, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource vertexConsumers) {
        int currentY = y;
        for (ClientTooltipComponent line : this.wrappedLines) {
            line.renderText(textRenderer, x, currentY, matrix, vertexConsumers);
            currentY += line.getHeight();
        }
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
        int currentY = y;
        for (ClientTooltipComponent line : this.wrappedLines) {
            line.renderImage(textRenderer, x, currentY, context);
            currentY += line.getHeight();
        }
    }
}
