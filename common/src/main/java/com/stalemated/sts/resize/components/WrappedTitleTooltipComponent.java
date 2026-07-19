package com.stalemated.sts.resize.components;

import com.stalemated.sts.util.StsManagedTitle;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.Text;
import org.joml.Matrix4f;

import java.util.List;

public class WrappedTitleTooltipComponent extends OrderedTextTooltipComponent implements TooltipComponent, StsManagedTitle {

    private final List<TooltipComponent> wrappedLines;

    public WrappedTitleTooltipComponent(List<TooltipComponent> wrappedLines) {
        super(Text.empty().asOrderedText());
        this.wrappedLines = wrappedLines;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        int maxWidth = 0;
        for (TooltipComponent line : this.wrappedLines) {
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
        for (TooltipComponent wrappedLine : this.wrappedLines) {
            totalHeight += wrappedLine.getHeight();
        }
        return totalHeight;
    }

    @Override
    public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix, VertexConsumerProvider.Immediate vertexConsumers) {
        int currentY = y;
        for (TooltipComponent line : this.wrappedLines) {
            line.drawText(textRenderer, x, currentY, matrix, vertexConsumers);
            currentY += line.getHeight();
        }
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        int currentY = y;
        for (TooltipComponent line : this.wrappedLines) {
            line.drawItems(textRenderer, x, currentY, context);
            currentY += line.getHeight();
        }
    }
}
