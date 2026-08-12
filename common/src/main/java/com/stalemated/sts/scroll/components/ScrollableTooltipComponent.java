package com.stalemated.sts.scroll.components;

import com.stalemated.lib.util.math.MathUtils;
import com.stalemated.sts.resize.TooltipDimensionManager;
import com.stalemated.sts.resize.centering.TitleCenteringProcessor;
import com.stalemated.sts.scroll.TooltipIdentityContext;
import com.stalemated.sts.scroll.TooltipScrollManager;
import com.stalemated.sts.state.StateManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.VertexConsumerProvider;
import org.joml.Matrix4f;

import java.util.List;

public class ScrollableTooltipComponent implements TooltipComponent {
    private final List<TooltipComponent> components;
    private final int maxHeight;
    private final int totalHeight;
    private final int maxWidth;
    private final int maxTextWidth;
    public static final int SCROLLBAR_WIDTH = 6;
    private static final int SCROLLBAR_PADDING_Y = 4;
    private final int scrollbarHeight;

    public ScrollableTooltipComponent(List<TooltipComponent> components, List<TooltipComponent> pinned, int maxHeight, int maxWidth, TextRenderer textRenderer) {
        this.components = components;
        this.maxHeight = maxHeight;

        int height = 0;
        int maxComponentWidth = 0;
        for (TooltipComponent component : components) {
            int width = component.getWidth(textRenderer);
            if (width > maxComponentWidth) maxComponentWidth = width;
            height += component.getHeight();
        }
        
        int maxPinnedWidth = TitleCenteringProcessor.getCleanPinnedWidth(pinned, textRenderer, TooltipDimensionManager.getModelOffset());
        int minBound = StateManager.isTierifyTooltip ? TooltipDimensionManager.MIN_TOOLTIP_WIDTH : 0;

        int contentWidth = Math.max(maxComponentWidth + SCROLLBAR_WIDTH, maxPinnedWidth);
        this.maxWidth = MathUtils.clamp(contentWidth, minBound, maxWidth);
        this.maxTextWidth = Math.max(0, this.maxWidth - SCROLLBAR_WIDTH);
        this.totalHeight = height;
        this.scrollbarHeight = this.maxHeight - SCROLLBAR_PADDING_Y;

        TooltipIdentityContext context = new TooltipIdentityContext(TooltipDimensionManager.getCurrentStack(), this.components, textRenderer);
        TooltipScrollManager.INSTANCE.onTooltipRendered(this.totalHeight - this.scrollbarHeight, context);
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return this.maxWidth;
    }

    @Override
    public int getHeight() {
        return Math.min(this.totalHeight, this.maxHeight);
    }

    @Override
    public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix, VertexConsumerProvider.Immediate vertexConsumers) {
        DrawContext context = TooltipDimensionManager.currentContext;
        if (context == null) return;

        int scroll = TooltipScrollManager.INSTANCE.getScrollOffset();
        vertexConsumers.draw();
        context.enableScissor(x, y, x + this.maxTextWidth, y + this.maxHeight);
        int currentY = y - scroll;

        for (TooltipComponent component : components) {
            component.drawText(textRenderer, x, currentY, matrix, vertexConsumers);
            currentY += component.getHeight();
        }

        vertexConsumers.draw();
        context.disableScissor();
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        int scroll = TooltipScrollManager.INSTANCE.getScrollOffset();

        context.enableScissor(x, y, x + this.maxTextWidth, y + this.maxHeight);
        int currentY = y - scroll;

        for (TooltipComponent component : components) {
            component.drawItems(textRenderer, x, currentY, context);
            currentY += component.getHeight();
        }

        context.disableScissor();
        
        drawScrollbar(context, x, y);
    }

    private void drawScrollbar(DrawContext context, int x, int y) {
        int maxScroll = this.totalHeight - this.scrollbarHeight;
        if (maxScroll <= 0) return;

        int scroll = TooltipScrollManager.INSTANCE.getScrollOffset();
        int scrollbarX = x + this.maxWidth - SCROLLBAR_WIDTH / 2;

        int minThumbHeight = 2;
        float visibleRatio = (float) this.scrollbarHeight / this.totalHeight;

        int thumbHeight = Math.max((int) (this.scrollbarHeight * visibleRatio), minThumbHeight);
        float scrollRatio = (float) scroll / maxScroll;
        int thumbY = y + (int) ((this.scrollbarHeight - thumbHeight) * scrollRatio);

        // Background
        context.fill(scrollbarX, y, scrollbarX + 2, y + this.scrollbarHeight, 0xFF444444);
        // Scroll Bar
        context.fill(scrollbarX, thumbY, scrollbarX + 2, thumbY + thumbHeight, 0xFFBBBBBB);
    }
}