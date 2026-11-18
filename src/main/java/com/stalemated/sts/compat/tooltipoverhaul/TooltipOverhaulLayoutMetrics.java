package com.stalemated.sts.compat.tooltipoverhaul;

import dev.xylonity.tooltipoverhaul.client.render.TooltipContext;
import dev.xylonity.tooltipoverhaul.client.util.Constants;
import dev.xylonity.tooltipoverhaul.client.util.RenderUtils;
import com.stalemated.sts.resize.components.WrappedTitleTooltipComponent;

import static com.stalemated.sts.scroll.ScrollState.PIXELS_PER_LINE;

public record TooltipOverhaulLayoutMetrics(int requiredDividerY, int defaultDividerY, int originalIconSize, int currentYAdded) {
    private static final int EXTRA_PADDING = 2;

    public static TooltipOverhaulLayoutMetrics calculate(TooltipContext context, WrappedTitleTooltipComponent wrapped) {
        int originalIconSize = Constants.getIconSize(context);
        int extraY = originalIconSize / 2;
        int wrappedHeight = wrapped.getHeight();
        
        int contentEnd;
        int currentYAdded;

        if (!RenderUtils.hasRating(context)) {
            int titleTop = extraY - (originalIconSize / 4);
            contentEnd = titleTop + wrappedHeight;
            currentYAdded = wrappedHeight * 2;
        } else {
            contentEnd = extraY + wrappedHeight;
            currentYAdded = PIXELS_PER_LINE + wrappedHeight;
        }

        int requiredDividerY = contentEnd + EXTRA_PADDING;
        int defaultDividerY = originalIconSize + Constants.getDividerLineTopPadding(context);

        return new TooltipOverhaulLayoutMetrics(requiredDividerY, defaultDividerY, originalIconSize, currentYAdded);
    }

    public int getTargetDividerY() {
        return Math.max(requiredDividerY, defaultDividerY);
    }
}
