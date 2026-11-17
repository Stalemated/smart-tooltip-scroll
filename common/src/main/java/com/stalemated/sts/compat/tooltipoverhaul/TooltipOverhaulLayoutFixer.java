package com.stalemated.sts.compat.tooltipoverhaul;

import com.stalemated.sts.resize.TooltipDimensionManager;
import dev.xylonity.tooltipoverhaul.client.frame.CustomFrameData;
import dev.xylonity.tooltipoverhaul.client.frame.CustomFrameManager;
import dev.xylonity.tooltipoverhaul.client.render.TooltipContext;
import dev.xylonity.tooltipoverhaul.client.util.Constants;
import dev.xylonity.tooltipoverhaul.client.util.RenderUtils;
import dev.xylonity.tooltipoverhaul.client.util.TextUtils;
import dev.xylonity.tooltipoverhaul.config.TooltipsConfig;
import com.stalemated.sts.resize.components.WrappedTitleTooltipComponent;

import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;

import static com.stalemated.sts.scroll.ScrollState.PIXELS_PER_LINE;

public class TooltipOverhaulLayoutFixer {

    public static Optional<WrappedTitleTooltipComponent> getWrappedTitle(TooltipContext context) {
        if (context == null || !context.hasIcon()) return Optional.empty();
        
        List<ClientTooltipComponent> components = context.getComponents();
        if (components == null || components.isEmpty()) return Optional.empty();
        
        ClientTooltipComponent first = components.get(0);
        if (first instanceof WrappedTitleTooltipComponent wrapped) {
            return Optional.of(wrapped);
        }
        return Optional.empty();
    }

    public static int getIconOffset() {
        TooltipContext context = TooltipOverhaulStateManager.getCurrentTOContext();

        if (context != null && context.hasIcon()) {
            return Constants.getIconSize(context) + Constants.getIconTitleSeparation(context);
        } else if (context == null) {
            ItemStack stack = TooltipDimensionManager.getCurrentStack();

            if (stack != null && !stack.isEmpty()) {
                CustomFrameData frameData = CustomFrameManager.of(stack).orElse(null);
                boolean disableIcon = frameData != null ? frameData.shouldDisableIcon() : TooltipsConfig.DISABLE_ICON;

                if (!disableIcon) {
                    return Constants.getIconSize(null) + Constants.getIconTitleSeparation(null);
                }
            }
        }
        return 0;
    }

    public static int getExtraWidth(Font textRenderer) {
        if (!TooltipOverhaulStateManager.isHandlingTOTooltip()) return 0;
        
        TooltipContext ctx = TooltipOverhaulStateManager.getCurrentTOContext();
        if (ctx == null || !RenderUtils.hasRating(ctx)) return 0;
        
        Component ratingText = TextUtils.getRatingText(ctx);
        if (ratingText != null) {
            return textRenderer.width(ratingText);
        }
        return 0;
    }

    public static Vec2 adjustDividerLineY(Vec2 position, TooltipContext context) {
        if (!TooltipOverhaulStateManager.isHandlingTOTooltip()) return position;

        return getWrappedTitle(context).map(wrapped -> {
            TooltipOverhaulLayoutMetrics metrics = TooltipOverhaulLayoutMetrics.calculate(context, wrapped);

            if (metrics.requiredDividerY() > metrics.defaultDividerY()) {
                float newY = position.y + (metrics.requiredDividerY() - metrics.defaultDividerY());
                return new Vec2(position.x, newY);
            }
            return position;
        }).orElse(position);
    }

    public static int getTitleYAdjustment(TooltipContext context, ClientTooltipComponent instance) {
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
