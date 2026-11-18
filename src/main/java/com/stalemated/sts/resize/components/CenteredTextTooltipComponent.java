package com.stalemated.sts.resize.components;

import com.stalemated.sts.util.StsManagedTitle;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;

public class CenteredTextTooltipComponent extends ClientTextTooltip implements ClientTooltipComponent, StsManagedTitle {
    private final FormattedCharSequence text;
    private final int xOffset;
    private final int centerOffset;
    private final int bottomPadding;

    public CenteredTextTooltipComponent(FormattedCharSequence text, int xOffset, int centerOffset, int bottomPadding) {
        super(text);
        this.text = text;
        this.xOffset = xOffset;
        this.centerOffset = centerOffset;
        this.bottomPadding = bottomPadding;
    }

    public FormattedCharSequence getText() {
        return this.text;
    }

    @Override
    public int getWidth(Font textRenderer) {
        return textRenderer.width(this.text) + this.xOffset;
    }

    @Override
    public int getHeight() {
        return super.getHeight() + this.bottomPadding;
    }

    @Override
    public void renderText(Font textRenderer, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource vertexConsumers) {
        super.renderText(textRenderer, x + this.xOffset + this.centerOffset, y, matrix, vertexConsumers);
    }
}
