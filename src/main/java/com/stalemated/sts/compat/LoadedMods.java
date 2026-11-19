package com.stalemated.sts.compat;

import com.stalemated.lib.helper.PlatformHelper;

public final class LoadedMods {
    public static final boolean LEGENDARY_TOOLTIPS = PlatformHelper.isModLoaded("legendarytooltips");
    public static final boolean TOOLTIP_OVERHAUL = PlatformHelper.isModLoaded("tooltipoverhaul");
    public static final boolean OBSCURE_TOOLTIPS = PlatformHelper.isModLoaded("obscure_tooltips");

    private LoadedMods() {
    }
}
