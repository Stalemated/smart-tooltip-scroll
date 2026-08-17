package com.stalemated.sts.compat.tooltipoverhaul;

import com.stalemated.sts.scroll.resetter.ExternalScrollStateResetter;
import dev.xylonity.tooltipoverhaul.client.util.TooltipScrollState;

public class TooltipOverhaulCompatImpl implements ExternalScrollStateResetter {
    @Override
    public void resetState() {
        try {
            TooltipScrollState.reset();
        } catch (Exception ignored) {}
    }
}
