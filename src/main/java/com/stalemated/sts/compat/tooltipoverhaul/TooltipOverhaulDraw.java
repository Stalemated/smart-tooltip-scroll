package com.stalemated.sts.compat.tooltipoverhaul;

import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.state.TooltipDraw;
import dev.xylonity.tooltipoverhaul.client.render.TooltipContext;
import dev.xylonity.tooltipoverhaul.client.util.TooltipScrollState;

public class TooltipOverhaulDraw {
    private static boolean stsActiveForCurrentTooltip = false;
    private static TooltipContext currentTOContext = null;

    public static boolean isHandlingTOTooltip() {
        return currentTOContext != null || stsActiveForCurrentTooltip;
    }

    public static TooltipContext getCurrentTOContext() {
        return currentTOContext;
    }

    public static void setContext(TooltipContext context) {
        currentTOContext = context;
    }

    public static void setStsActive(boolean active) {
        stsActiveForCurrentTooltip = active;
    }

    public static boolean isStsActive() {
        return stsActiveForCurrentTooltip;
    }

    public static void ensureContext(Object contextObj) {
        if (!SmartTooltipScrollConfigRegistry.CONFIG.get().tooltip_overhaul_compat) return;

        if (contextObj instanceof TooltipContext context) {
            TooltipDraw.begin(context.getGraphics(), context.getFont());
            if (stsActiveForCurrentTooltip) {
                currentTOContext = context;
            }
        }
    }

    public static void clearContext() {
        if (!SmartTooltipScrollConfigRegistry.CONFIG.get().tooltip_overhaul_compat) return;

        TooltipDraw.end();
        stsActiveForCurrentTooltip = false;
        currentTOContext = null;
    }

    public static boolean takeOverToScroll() {
        if (!stsActiveForCurrentTooltip || !SmartTooltipScrollConfigRegistry.CONFIG.get().tooltip_overhaul_compat) return false;

        TooltipScrollState.reset();
        return true;
    }
}
