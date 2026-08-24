package com.stalemated.sts.config;

import com.stalemated.lib.config.BaseConfigManager;
import com.stalemated.lib.config.ConfigProvider;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.util.Identifier;

import java.nio.file.Path;

import static com.stalemated.sts.SmartTooltipScrollClient.LOGGER;

public class ConfigManager {

    private static final Path CONFIG_PATH = BaseConfigManager.buildPath("smart_tooltip_scroll.json5");
    
    public static final ConfigClassHandler<STSConfig> HANDLER = ConfigClassHandler.createBuilder(STSConfig.class)
            .id(new Identifier("sts", "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(CONFIG_PATH)
                    .setJson5(true)
                    .build())
            .build();

    private static final BaseConfigManager<STSConfig> CONFIG = new BaseConfigManager<STSConfig>(
            new ConfigProvider<STSConfig>() {
                @Override
                public boolean load() {
                    return HANDLER.load();
                }

                @Override
                public void save() {
                    HANDLER.save();
                }

                @Override
                public STSConfig instance() {
                    return HANDLER.instance();
                }
            },
            CONFIG_PATH,
            LOGGER
    ) {};

    public static boolean configLoadFailed = false;

    public static void register() {
        CONFIG.register();
        configLoadFailed = CONFIG.configLoadFailed;
    }

    public static STSConfig getConfig() {
        return CONFIG.getConfig();
    }

    public static void save() {
        CONFIG.save();
    }
}
