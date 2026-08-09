package com.stalemated.sts;

import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.scroll.ScrollResetterManager;
import com.stalemated.sts.state.TooltipContextManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public final class SmartTooltipScrollClient {
    public static final String MOD_ID = "smart_tooltip_scroll";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        ConfigManager.register();
        ScrollResetterManager.register();
    }

    public static void onItemTooltip(ItemStack stack) {
        if (stack.isEmpty()) return;
        TooltipContextManager.push(stack);
    }
}
