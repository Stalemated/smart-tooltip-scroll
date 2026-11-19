package com.stalemated.sts.scroll.components;

import com.stalemated.lib.util.math.MathUtils;
import com.stalemated.sts.compat.tierify.TierifyCompat;
import com.stalemated.sts.resize.TooltipBounds;
import com.stalemated.sts.resize.TooltipStyles;
import com.stalemated.sts.resize.centering.TitleCenteringProcessor;
import com.stalemated.sts.scroll.TooltipScroll;
import com.stalemated.sts.state.TooltipDraw;
import org.joml.Matrix4f;

import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;

public class ScrollableTooltipComponent implements ClientTooltipComponent {
    private final List<ClientTooltipComponent> components;
    private final int maxHeight;
    private final int totalHeight;
    private final int maxWidth;
    private final int maxTextWidth;
    public static final int SCROLLBAR_WIDTH = 6;
    private static final int SCROLLBAR_PADDING_Y = 4;
    private final int scrollbarHeight;

    public ScrollableTooltipComponent(List<ClientTooltipComponent> components, List<ClientTooltipComponent> pinned, int maxHeight, int maxWidth, Font textRenderer) {
        this.components = components;
        this.maxHeight = maxHeight;

        int height = 0;
        int maxComponentWidth = 0;
        for (ClientTooltipComponent component : components) {
            int width = component.getWidth(textRenderer);
            if (width > maxComponentWidth) maxComponentWidth = width;
            height += component.getHeight();
        }
        
        int maxPinnedWidth = TitleCenteringProcessor.getCleanPinnedWidth(pinned, textRenderer, TooltipStyles.currentModelOffset());
        int minBound = TierifyCompat.isDrawing() ? TooltipBounds.MIN_WIDTH : 0;

        int contentWidth = Math.max(maxComponentWidth + SCROLLBAR_WIDTH, maxPinnedWidth);
        this.maxWidth = MathUtils.clamp(contentWidth, minBound, maxWidth);
        this.maxTextWidth = Math.max(0, this.maxWidth - SCROLLBAR_WIDTH);
        this.totalHeight = height;
        this.scrollbarHeight = this.maxHeight - SCROLLBAR_PADDING_Y;
    }

    public int maxScroll() {
        return this.totalHeight - this.scrollbarHeight;
    }

    @Override
    public int getWidth(Font textRenderer) {
        return this.maxWidth;
    }

    @Override
    public int getHeight() {
        return Math.min(this.totalHeight, this.maxHeight);
    }

    @Override
    public void renderText(Font textRenderer, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource vertexConsumers) {
        GuiGraphics context = TooltipDraw.graphics();
        if (context == null) return;

        int scroll = TooltipScroll.INSTANCE.getScrollOffset();
        vertexConsumers.endBatch();
        context.enableScissor(x, y, x + this.maxTextWidth, y + this.maxHeight);
        int currentY = y - scroll;

        for (ClientTooltipComponent component : components) {
            component.renderText(textRenderer, x, currentY, matrix, vertexConsumers);
            currentY += component.getHeight();
        }

        vertexConsumers.endBatch();
        context.disableScissor();
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
        int scroll = TooltipScroll.INSTANCE.getScrollOffset();

        context.enableScissor(x, y, x + this.maxTextWidth, y + this.maxHeight);
        int currentY = y - scroll;

        for (ClientTooltipComponent component : components) {
            component.renderImage(textRenderer, x, currentY, context);
            currentY += component.getHeight();
        }

        context.disableScissor();
        
        drawScrollbar(context, x, y);
    }

    private void drawScrollbar(GuiGraphics context, int x, int y) {
        int maxScroll = maxScroll();
        if (maxScroll <= 0) return;

        int scroll = TooltipScroll.INSTANCE.getScrollOffset();
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