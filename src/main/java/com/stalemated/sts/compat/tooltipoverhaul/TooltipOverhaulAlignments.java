package com.stalemated.sts.compat.tooltipoverhaul;

import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;

public class TooltipOverhaulAlignments {

    public static String getForcedRatingAlignment() {
        if (TooltipOverhaulStateManager.isHandlingTOTooltip() && SmartTooltipScrollConfigRegistry.CONFIG.get().title_centering) {
            return "middle";
        }
        return null;
    }

    public static String getForcedTitleAlignment() {
        if (TooltipOverhaulStateManager.isHandlingTOTooltip() && SmartTooltipScrollConfigRegistry.CONFIG.get().title_centering) {
            return "left";
        }
        return null;
    }
}
