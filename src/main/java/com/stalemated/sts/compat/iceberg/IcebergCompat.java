package com.stalemated.sts.compat.iceberg;

import com.stalemated.sts.config.SmartTooltipScrollConfig;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.util.StsManagedTitle;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

import java.util.List;

public final class IcebergCompat {
    private IcebergCompat() {
    }

    public static int lineWidth(int width) {
        return SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions ? Integer.MAX_VALUE : width;
    }

    public static boolean skipsTitleCentering(List<ClientTooltipComponent> components) {
        SmartTooltipScrollConfig config = SmartTooltipScrollConfigRegistry.CONFIG.get();
        if (!config.custom_tooltip_dimensions) return false;
        if (config.title_centering) return true;

        for (ClientTooltipComponent component : components) {
            if (component instanceof StsManagedTitle) return true;
        }
        return false;
    }
}
