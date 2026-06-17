package com.stalemated.sts.resize.enforce;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TooltipEnforcerRegistry {
    private static final List<Predicate<List<TooltipComponent>>> ENFORCERS = new ArrayList<>();

    public static void register(Predicate<List<TooltipComponent>> enforcerPredicate) {
        ENFORCERS.add(enforcerPredicate);
    }

    public static boolean isEnforced(List<TooltipComponent> components) {
        for (Predicate<List<TooltipComponent>> enforcer : ENFORCERS) {
            if (enforcer.test(components)) {
                return true;
            }
        }
        return false;
    }
}
