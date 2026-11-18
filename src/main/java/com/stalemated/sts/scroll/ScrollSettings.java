package com.stalemated.sts.scroll;

import com.stalemated.sts.config.SmartTooltipScrollConfig;
import com.stalemated.sts.scroll.easing.ScrollEasingStyle;

public record ScrollSettings(float smoothness, float linesPerScroll, ScrollEasingStyle easing) {
    public static ScrollSettings of(SmartTooltipScrollConfig config) {
        return new ScrollSettings(config.scroll_smoothness, config.lines_per_scroll, config.scroll_easing_style);
    }
}
