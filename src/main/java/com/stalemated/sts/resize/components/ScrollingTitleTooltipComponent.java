package com.stalemated.sts.resize.components;

import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.resize.TooltipDimensionManager;
import com.stalemated.sts.util.OrderedTextUtil;
import com.stalemated.sts.util.StsManagedTitle;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import com.stalemated.lib.util.math.ScrollClock;
import com.stalemated.lib.util.math.ScrollMathUtil;

public class ScrollingTitleTooltipComponent extends ClientTextTooltip implements ClientTooltipComponent, StsManagedTitle {
    private static final ScrollClock TITLE_SCROLL_CLOCK = new ScrollClock();

    private final FormattedCharSequence text;
    private final int maxTitleWidth;

    public ScrollingTitleTooltipComponent(FormattedCharSequence text, int maxTitleWidth) {
        super(text);
        this.text = text;
        this.maxTitleWidth = maxTitleWidth;
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public int getWidth(Font textRenderer) {
        int textWidth = textRenderer.width(this.text);
        return Math.min(textWidth, this.maxTitleWidth);
    }

    private int getOffset() {
        return TooltipDimensionManager.getModelOffset(
            TooltipDimensionManager.getCurrentStack(),
            TooltipDimensionManager.processedTitleComponentList
        );
    }

    @Override
    public void renderText(Font textRenderer, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource vertexConsumers) {
        int textWidth = textRenderer.width(this.text);
        if (textWidth <= this.maxTitleWidth) {
            textRenderer.drawInBatch(this.text, (float) x, (float) y, -1, true, matrix, vertexConsumers, Font.DisplayMode.NORMAL, 0, 15728880);
            return;
        }

        int offset = getOffset();
        int overflowWidth = textWidth - this.maxTitleWidth;
        int identity = OrderedTextUtil.getStringFromOrderedText(this.text).hashCode();
        long elapsedTime = TITLE_SCROLL_CLOCK.elapsedMillis(identity);
        int scrollOffset = ScrollMathUtil.calculateScrollOffset(overflowWidth, SmartTooltipScrollConfigRegistry.CONFIG.get().title_scroll_speed, SmartTooltipScrollConfigRegistry.CONFIG.get().title_scroll_pause_time_ms, elapsedTime);
        int startX = x;
        if (!TooltipDimensionManager.handlesModelOffsetNatively()) startX += offset;
        int endX = x + this.maxTitleWidth;

        vertexConsumers.endBatch();

        Matrix4f translatedMatrix = new Matrix4f(matrix);
        translatedMatrix.translate(0, 0, 400);

        GuiGraphics context = TooltipDimensionManager.currentContext;

        if (context != null) {
            context.enableScissor(startX, y, endX, y + getHeight());
        }

        textRenderer.drawInBatch(this.text, (float) (x - scrollOffset), (float) y, -1, true, translatedMatrix, vertexConsumers, Font.DisplayMode.NORMAL, 0, 15728880);
        vertexConsumers.endBatch();

        if (context != null) {
            context.disableScissor();
        }
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
    }
}
