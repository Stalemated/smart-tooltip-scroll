package com.stalemated.sts.resize.overflow;

import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.resize.TitleOverflowMode;
import com.stalemated.sts.resize.overflow.strategies.*;

public class TitleOverflowStrategyFactory {

    private static final TitleOverflowStrategy TRUNCATE_STRATEGY = new TruncateOverflowStrategy();
    private static final TitleOverflowStrategy WRAP_STRATEGY = new WrapOverflowStrategy();
    private static final TitleOverflowStrategy SCROLL_STRATEGY = new ScrollOverflowStrategy();

    public static TitleOverflowStrategy getStrategy() {
        TitleOverflowMode mode = ConfigManager.getConfig().title_overflow_mode;
        
        if (mode == null) {
            return SCROLL_STRATEGY; // Fallback
        }

        return switch (mode) {
            case TRUNCATE -> TRUNCATE_STRATEGY;
            case WRAP -> WRAP_STRATEGY;
            default -> SCROLL_STRATEGY;
        };
    }
}
