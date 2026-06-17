package com.stalemated.sts.compat.emi;

import com.stalemated.sts.resize.enforce.TooltipEnforcerRegistry;
import dev.emi.emi.screen.tooltip.EmiTextTooltipWrapper;
import net.minecraft.client.gui.tooltip.TooltipComponent;

public class EmiCompat {
    public static void init() {
        TooltipEnforcerRegistry.register(components -> {
            for (TooltipComponent component : components) {
                if (component instanceof EmiTextTooltipWrapper) {
                    return true;
                }
            }
            return false;
        });
    }
}
