package com.stalemated.sts.compat.legendarytooltips;

import com.anthonyhilyard.iceberg.util.Tooltips;
import com.anthonyhilyard.legendarytooltips.config.LegendaryTooltipsConfig;
import com.anthonyhilyard.legendarytooltips.tooltip.ItemModelComponent;
import com.anthonyhilyard.legendarytooltips.tooltip.PaddingComponent;
import com.stalemated.sts.resize.TooltipDimensionManager;
import com.stalemated.sts.state.StateManager;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class LegendaryTooltipsCompat {

    private static final int LT_ITEM_MODEL_OFFSET = 24;

    public static int getItemModelComponentWidth(ItemStack stack, List<TooltipComponent> currentComponents) {
        if (StateManager.isTierifyTooltip) {
            if (stack == null || stack.isEmpty()) return 0;
            return LegendaryTooltipsConfig.showModelForItem(stack) ? LT_ITEM_MODEL_OFFSET : 0;
        }

        List<TooltipComponent> combined = new ArrayList<>();
        combined.addAll(TooltipDimensionManager.processedTitleComponentList);
        combined.addAll(TooltipDimensionManager.bodyComponentList);
        if (combined.isEmpty() && currentComponents != null) {
            combined.addAll(currentComponents);
        }

        if (!combined.isEmpty()) {
            for (TooltipComponent component : combined) {
                if (component instanceof ItemModelComponent && LegendaryTooltipsConfig.showModelForItem(stack)) {
                    return LT_ITEM_MODEL_OFFSET;
                }
            }
            
            return 0;
        } else {
            if (stack == null || stack.isEmpty()) return 0;
            return LegendaryTooltipsConfig.showModelForItem(stack) ? LT_ITEM_MODEL_OFFSET : 0;
        }
    }

    public static int getSplitIndex(List<TooltipComponent> components, int splitIndex) {
        for (int i = 0; i < components.size(); i++) {

            if (components.get(i) instanceof PaddingComponent ||
                    components.get(i) instanceof Tooltips.TitleBreakComponent) {
                splitIndex = i + 1;
                break;
            }
        }
        return Math.min(splitIndex, components.size());
    }
}
