package com.stalemated.sts.compat.tooltipoverhaul;

import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;

public class TooltipOverhaulAlignments {

    public static String ratingAlignment(String alignment) {
        return centersTitle() ? "middle" : alignment;
    }

    public static String titleAlignment(String alignment) {
        return centersTitle() ? "left" : alignment;
    }

    private static boolean centersTitle() {
        return TooltipOverhaulStateManager.isHandlingTOTooltip() && SmartTooltipScrollConfigRegistry.CONFIG.get().title_centering;
    }
}
