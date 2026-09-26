package com.stalemated.sts.config;

import com.stalemated.lib.config.SLibConfig;
import com.stalemated.lib.config.manager.LocalConfigManager;

import static com.stalemated.sts.SmartTooltipScrollClient.LOGGER;
import static com.stalemated.sts.SmartTooltipScrollClient.MOD_ID;

public class ConfigManager {

    public static final LocalConfigManager<STSConfig> MANAGER = SLibConfig.localBuilder(STSConfig.class)
            .modId(MOD_ID)
            .logger(LOGGER)
            .register();

    public static void register() {
    }

    public static STSConfig getConfig() {
        return MANAGER.getActiveConfig();
    }
}
