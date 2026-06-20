package com.stalemated.sts.compat.legendarytooltips;

import com.anthonyhilyard.legendarytooltips.config.LegendaryTooltipsConfig;
import com.anthonyhilyard.legendarytooltips.tooltip.ItemModelComponent;
import com.anthonyhilyard.iceberg.component.TitleBreakComponent;
import com.stalemated.sts.resize.TooltipDimensionManager;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;

import java.util.List;

public class LegendaryTooltipsCompat {

    public static int getItemModelComponentWidth(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;

        int offset = LegendaryTooltipsConfig.showModelForItem(stack) ? 24 : 0;

        List<TooltipComponent> currentComponents = TooltipDimensionManager.currentComponents;
        if (currentComponents != null) {
            for (TooltipComponent component : currentComponents) {
                if (component instanceof ItemModelComponent) {
                    return offset;
                }
            }
            return 0;
        } else {
            return offset;
        }
    }

    public static int getSplitIndex(List<TooltipComponent> components, int splitIndex) {
        boolean hasTitleBreak = false;
        for (int i = 0; i < components.size(); i++) {

            if (components.get(i) instanceof TitleBreakComponent) {
                splitIndex = i + 1;
                hasTitleBreak = true;
                break;
            }
        }
        if (!hasTitleBreak) {
            return components.size();
        }
        return Math.min(splitIndex, components.size());
    }

    public static int getExtraTitlePadding() {
        return 9;
    }
}
