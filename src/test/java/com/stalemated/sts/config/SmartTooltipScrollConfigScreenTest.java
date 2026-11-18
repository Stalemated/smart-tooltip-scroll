package com.stalemated.sts.config;

import com.stalemated.lib.compat.yacl.SLibTesting;
import com.stalemated.lib.config.InMemoryConfigManager;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class SmartTooltipScrollConfigScreenTest {
    @Test
    void theConfigScreenBuilds() throws Exception {
        SLibTesting.assertScreenBuilds(
                SmartTooltipScrollConfigScreen.builder(null, new InMemoryConfigManager<>(new SmartTooltipScrollConfig(), SmartTooltipScrollConfig::new, null)),
                Path.of(SmartTooltipScrollConfigScreenTest.class.getResource("/assets/smart_tooltip_scroll/lang/en_us.json").toURI())
        );
    }
}
