package com.stalemated.sts.config;

import com.stalemated.lib.config.LocalConfigManager;
import com.stalemated.lib.config.SLibConfig;
import com.stalemated.sts.resize.TitleOverflowMode;
import com.stalemated.sts.scroll.easing.ScrollEasingStyle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SmartTooltipScrollConfigLoadTest {
    @Test
    void nullModesInTheConfigFileLoadAsTheDefaults(@TempDir Path dir) throws Exception {
        Path file = dir.resolve("smart_tooltip_scroll.json5");
        Files.writeString(file, "{\n  scroll_easing_style: null,\n  title_overflow_mode: null\n}\n");

        LocalConfigManager<SmartTooltipScrollConfig> manager = SLibConfig.local("smart_tooltip_scroll_load_test", SmartTooltipScrollConfig.class).configPath(file).build();
        manager.register();

        assertEquals(ScrollEasingStyle.SMOOTH, manager.get().scroll_easing_style);
        assertEquals(TitleOverflowMode.SCROLL, manager.get().title_overflow_mode);
    }

    @Test
    void zeroPercentagesInTheConfigFileLoadAsOne(@TempDir Path dir) throws Exception {
        Path file = dir.resolve("smart_tooltip_scroll.json5");
        Files.writeString(file, "{\n  max_width_percentage: 0,\n  max_height_percentage: 0\n}\n");

        LocalConfigManager<SmartTooltipScrollConfig> manager = SLibConfig.local("smart_tooltip_scroll_percentage_test", SmartTooltipScrollConfig.class).configPath(file).build();
        manager.register();

        assertEquals(1, manager.get().max_width_percentage);
        assertEquals(1, manager.get().max_height_percentage);
    }
}
