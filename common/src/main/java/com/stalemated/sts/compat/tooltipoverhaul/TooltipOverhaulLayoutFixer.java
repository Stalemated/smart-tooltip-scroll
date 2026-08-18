package com.stalemated.sts.compat.tooltipoverhaul;

import dev.xylonity.tooltipoverhaul.client.render.TooltipContext;
import dev.xylonity.tooltipoverhaul.client.util.Constants;
import dev.xylonity.tooltipoverhaul.client.util.RenderUtils;
import dev.xylonity.tooltipoverhaul.client.util.TextUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import com.stalemated.sts.resize.components.WrappedTitleTooltipComponent;

import java.util.List;
import java.util.Optional;

import static com.stalemated.sts.scroll.ScrollState.PIXELS_PER_LINE;

public class TooltipOverhaulLayoutFixer {

    public static Optional<WrappedTitleTooltipComponent> getWrappedTitle(TooltipContext context) {
        if (context == null || !context.hasIcon()) return Optional.empty();
        
        List<TooltipComponent> components = context.getComponents();
        if (components == null || components.isEmpty()) return Optional.empty();
        
        TooltipComponent first = components.get(0);
        if (first instanceof WrappedTitleTooltipComponent wrapped) {
            return Optional.of(wrapped);
        }
        return Optional.empty();
    }

    public static int getIconOffset() {
        TooltipContext context = TooltipOverhaulStateManager.getCurrentTOContext();

        if (context != null && context.hasIcon()) {
            return Constants.getIconSize(context) + Constants.getIconTitleSeparation(context);
        }
        return 0;
    }

    public static int getExtraWidth(TextRenderer textRenderer) {
        if (!TooltipOverhaulStateManager.isHandlingTOTooltip()) return 0;
        
        TooltipContext ctx = TooltipOverhaulStateManager.getCurrentTOContext();
        if (ctx == null || !RenderUtils.hasRating(ctx)) return 0;
        
        Text ratingText = TextUtils.getRatingText(ctx);
        if (ratingText != null) {
            return textRenderer.getWidth(ratingText);
        }
        return 0;
    }

    public static Vec2f adjustDividerLineY(Vec2f position, TooltipContext context) {
        if (!TooltipOverhaulStateManager.isHandlingTOTooltip()) return position;

        return getWrappedTitle(context).map(wrapped -> {
            TooltipOverhaulLayoutMetrics metrics = TooltipOverhaulLayoutMetrics.calculate(context, wrapped);

            if (metrics.requiredDividerY() > metrics.defaultDividerY()) {
                float newY = position.y + (metrics.requiredDividerY() - metrics.defaultDividerY());
                return new Vec2f(position.x, newY);
            }
            return position;
        }).orElse(position);
    }

    public static int getTitleYAdjustment(TooltipContext context, TooltipComponent instance) {
        if (context == null) return 0;

        if (instance instanceof WrappedTitleTooltipComponent wrapped) {
            if (context.hasIcon() && RenderUtils.hasRating(context)) {
                return wrapped.getHeight() - wrapped.getFirstLineHeight();
            }
        }
        return 0;
    }

    public static int adjustDrawStringY(int y, TooltipContext context) {
        return y + getWrappedTitle(context)
                .map(wrapped -> wrapped.getHeight() - wrapped.getFirstLineHeight())
                .orElse(0);
    }

    public static int fixIconTitleSeparation(TooltipContext context) {
        int separation = Constants.getIconTitleSeparation(context);

        return getWrappedTitle(context).map(wrapped -> {
            TooltipOverhaulLayoutMetrics metrics = TooltipOverhaulLayoutMetrics.calculate(context, wrapped);
            return metrics.getTargetDividerY() - metrics.currentYAdded();
        }).orElse(separation);
    }

    public static float fixBoxHeight(float originalHeight, TooltipContext context) {
        if (!TooltipOverhaulStateManager.isHandlingTOTooltip()) return originalHeight;
        
        return getWrappedTitle(context).map(wrapped -> {
            TooltipOverhaulLayoutMetrics metrics = TooltipOverhaulLayoutMetrics.calculate(context, wrapped);

            int allocatedTop = wrapped.getHeight() + (metrics.originalIconSize() - PIXELS_PER_LINE);
            int error = allocatedTop - metrics.getTargetDividerY();
            return originalHeight - error;
        }).orElse(originalHeight);
    }
}
