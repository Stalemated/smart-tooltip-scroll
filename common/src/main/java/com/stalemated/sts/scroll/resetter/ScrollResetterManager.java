package com.stalemated.sts.scroll.resetter;

import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulCompat;
import com.stalemated.sts.scroll.TooltipScrollManager;

import static com.stalemated.sts.state.StateManager.IS_TO_LOADED;

public class ScrollResetterManager {
    public static void register() {
        if (IS_TO_LOADED) {
            TooltipScrollManager.registerResetter(new TooltipOverhaulCompat());
        }
    }
}
