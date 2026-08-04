package com.stalemated.sts.resize.components;

import com.stalemated.sts.util.StsManagedTitle;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.OrderedText;
import org.joml.Matrix4f;

public class CenteredTextTooltipComponent extends OrderedTextTooltipComponent implements TooltipComponent, StsManagedTitle {
    private final OrderedText text;
    private final int xOffset;
    private final int centerOffset;
    private final int bottomPadding;

    public CenteredTextTooltipComponent(OrderedText text, int xOffset, int centerOffset, int bottomPadding) {
        super(text);
        this.text = text;
        this.xOffset = xOffset;
        this.centerOffset = centerOffset;
        this.bottomPadding = bottomPadding;
    }

    public OrderedText getText() {
        return this.text;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return textRenderer.getWidth(this.text) + this.xOffset;
    }

    @Override
    public int getHeight() {
        return super.getHeight() + this.bottomPadding;
    }

    @Override
    public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix, VertexConsumerProvider.Immediate vertexConsumers) {
        super.drawText(textRenderer, x + this.xOffset + this.centerOffset, y, matrix, vertexConsumers);
    }
}
