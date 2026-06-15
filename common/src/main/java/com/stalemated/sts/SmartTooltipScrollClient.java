package com.stalemated.sts;

import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.resize.TooltipDimensionManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public final class SmartTooltipScrollClient {
    public static final String MOD_ID = "resized_scrollable_tooltips";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        ConfigManager.register();
    }

    public static void onItemTooltip(ItemStack stack) {
        if (stack.isEmpty()) return;
        TooltipDimensionManager.nextTooltipIsItem = true;
        TooltipDimensionManager.setCurrentStack(stack);
    }
}
