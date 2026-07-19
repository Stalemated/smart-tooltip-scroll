package com.stalemated.sts.compat.emi;

import com.stalemated.sts.resize.enforce.TooltipEnforcerRegistry;
import net.minecraft.client.gui.tooltip.TooltipComponent;

public class EmiCompat {
    public static void init() {
        TooltipEnforcerRegistry.register(components -> {

            for (TooltipComponent component : components) {
                if (component instanceof EmiItemMarkerComponent) {
                    return true;
                }
            }
            return false;
        });
    }
}
