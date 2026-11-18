package com.stalemated.sts.config;

import com.stalemated.lib.compat.yacl.builder.YaclScreenBuilder;
import com.stalemated.lib.config.ConfigManager;
import com.stalemated.sts.SmartTooltipScroll;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class SmartTooltipScrollConfigScreen {
    private SmartTooltipScrollConfigScreen() {
    }

    public static Screen create(Screen parent) {
        return builder(parent, SmartTooltipScrollConfigRegistry.CONFIG).generateScreen();
    }

    static YaclScreenBuilder builder(Screen parent, ConfigManager<SmartTooltipScrollConfig> config) {
        return YaclScreenBuilder.create(parent, Component.translatable("smart_tooltip_scroll.config.title"), SmartTooltipScroll.MOD_ID)
                .category(config, "general", category -> category
                        .group("custom_dimensions", group -> group.include("custom_tooltip_dimensions", "max_height_percentage", "max_width_percentage"))
                        .group("title_options", group -> group.include("title_overflow_mode", "title_centering", "title_scroll_speed", "title_scroll_pause_time_ms"))
                        .group("scrolling", group -> group.include("scroll_smoothness", "lines_per_scroll", "scroll_easing_style", "lock_container_scrolling"))
                        .group("compatibility", group -> group.include("obscure_compat", "tooltip_overhaul_compat")));
    }
}
