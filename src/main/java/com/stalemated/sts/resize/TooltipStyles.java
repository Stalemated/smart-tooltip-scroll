package com.stalemated.sts.resize;

import com.stalemated.sts.compat.LoadedMods;
import com.stalemated.sts.compat.legendarytooltips.LegendaryTooltipsStyle;
import com.stalemated.sts.compat.tierify.TierifyCompat;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulStyle;
import com.stalemated.sts.state.TooltipContexts;

public final class TooltipStyles {
    private static final TooltipStyle LOADED = forLoadedMods();

    private TooltipStyles() {
    }

    public static TooltipStyle loaded() {
        return LOADED;
    }

    public static int currentModelOffset() {
        return LOADED.modelOffset(TooltipContexts.currentStack(), null);
    }

    public static boolean drawsModelOffsetNatively() {
        return TierifyCompat.isDrawing() || LOADED.drawsModelOffset();
    }

    private static TooltipStyle forLoadedMods() {
        TooltipStyle style = new VanillaTooltipStyle();
        if (LoadedMods.TOOLTIP_OVERHAUL) style = new TooltipOverhaulStyle(style);
        if (LoadedMods.LEGENDARY_TOOLTIPS) style = new LegendaryTooltipsStyle(style);
        return style;
    }
}
