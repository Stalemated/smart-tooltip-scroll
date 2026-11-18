package com.stalemated.sts.resize;

import com.stalemated.sts.compat.legendarytooltips.LegendaryTooltipsStyle;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulStyle;
import com.stalemated.sts.state.StateManager;
import com.stalemated.sts.state.TooltipContextManager;

public final class TooltipStyles {
    private static final TooltipStyle LOADED = forLoadedMods();

    private TooltipStyles() {
    }

    public static TooltipStyle loaded() {
        return LOADED;
    }

    public static int currentModelOffset() {
        return LOADED.modelOffset(TooltipContextManager.currentStack(), null);
    }

    public static boolean drawsModelOffsetNatively() {
        return StateManager.isTierifyTooltip || LOADED.drawsModelOffset();
    }

    private static TooltipStyle forLoadedMods() {
        TooltipStyle style = new VanillaTooltipStyle();
        if (StateManager.IS_TO_LOADED) style = new TooltipOverhaulStyle(style);
        if (StateManager.IS_LT_LOADED) style = new LegendaryTooltipsStyle(style);
        return style;
    }
}
