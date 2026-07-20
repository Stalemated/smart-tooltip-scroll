package com.stalemated.sts;

import com.stalemated.lib.helper.PlatformHelper;
import com.stalemated.sts.compat.emi.EmiCompat;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulCompatImpl;
import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.scroll.TooltipScrollManager;
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
        
        if (PlatformHelper.INSTANCE.isModLoaded("emi")) {
            EmiCompat.init();
        }
        if (PlatformHelper.INSTANCE.isModLoaded("tooltipoverhaul")) {
            TooltipScrollManager.registerResetter(new TooltipOverhaulCompatImpl());
        }
    }

    public static void onItemTooltip(ItemStack stack) {
        if (stack.isEmpty()) return;
        TooltipContextManager.push(stack);
    }
}
