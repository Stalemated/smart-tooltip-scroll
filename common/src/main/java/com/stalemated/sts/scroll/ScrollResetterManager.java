package com.stalemated.sts.scroll;

import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulCompatImpl;

import static com.stalemated.sts.state.StateManager.IS_TO_LOADED;

public class ScrollResetterManager {
    public static void register() {
        if (IS_TO_LOADED) {
            TooltipScrollManager.registerResetter(new TooltipOverhaulCompatImpl());
        }
    }
}
