package com.stalemated.sts.config;

import com.stalemated.lib.config.LocalConfigManager;
import com.stalemated.lib.config.SLibConfig;
import com.stalemated.sts.SmartTooltipScroll;

public final class SmartTooltipScrollConfigRegistry {
    public static final LocalConfigManager<SmartTooltipScrollConfig> CONFIG = SLibConfig.local(SmartTooltipScroll.MOD_ID, SmartTooltipScrollConfig.class)
            .logger(SmartTooltipScroll.LOGGER)
            .build();

    private SmartTooltipScrollConfigRegistry() {
    }

    public static void register() {
        CONFIG.register();
    }
}
