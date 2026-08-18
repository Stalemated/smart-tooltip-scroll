package com.stalemated.sts.compat.tooltipoverhaul;

import com.stalemated.sts.config.ConfigManager;

public class TooltipOverhaulAlignments {

    public static String getForcedRatingAlignment() {
        if (TooltipOverhaulStateManager.isHandlingTOTooltip() && ConfigManager.getConfig().title_centering) {
            return "middle";
        }
        return null;
    }

    public static String getForcedTitleAlignment() {
        if (TooltipOverhaulStateManager.isHandlingTOTooltip() && ConfigManager.getConfig().title_centering) {
            return "left";
        }
        return null;
    }
}
