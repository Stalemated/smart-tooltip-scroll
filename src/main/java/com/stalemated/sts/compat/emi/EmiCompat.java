package com.stalemated.sts.compat.emi;

import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

import java.util.List;

public final class EmiCompat {
    private EmiCompat() {
    }

    public static int tooltipMaxWidth(int maxWidth, List<ClientTooltipComponent> components) {
        if (!SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions) return maxWidth;
        if (components == null || components.isEmpty()) return maxWidth;
        return Integer.MAX_VALUE;
    }
}
