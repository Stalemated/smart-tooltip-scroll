package com.stalemated.sts.compat.legendarytooltips;

import com.anthonyhilyard.iceberg.util.Tooltips;
import com.anthonyhilyard.legendarytooltips.config.LegendaryTooltipsConfig;
import com.anthonyhilyard.legendarytooltips.tooltip.ItemModelComponent;
import com.anthonyhilyard.legendarytooltips.tooltip.PaddingComponent;
import com.stalemated.sts.resize.TooltipDimensionManager;
import com.stalemated.sts.state.StateManager;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;

import java.util.List;


public class LegendaryTooltipsCompat {

    private static final int LT_ITEM_MODEL_OFFSET = 24;

    public static int getItemModelComponentWidth(ItemStack stack, List<TooltipComponent> currentComponents) {
        if (stack == null || stack.isEmpty()) return 0;
        if (!LegendaryTooltipsConfig.showModelForItem(stack)) return 0;

        if (StateManager.isTierifyTooltip) {
            return LT_ITEM_MODEL_OFFSET;
        }

        boolean hasTitleComponents = !TooltipDimensionManager.processedTitleComponentList.isEmpty();

        if (hasTitleComponents) {
            if (containsItemModel(TooltipDimensionManager.processedTitleComponentList)) {
                return LT_ITEM_MODEL_OFFSET;
            }
            return 0;
        }

        if (currentComponents != null && !currentComponents.isEmpty()) {
            return containsItemModel(currentComponents) ? LT_ITEM_MODEL_OFFSET : 0;
        }

        return LT_ITEM_MODEL_OFFSET;
    }

    private static boolean containsItemModel(List<TooltipComponent> components) {
        for (TooltipComponent component : components) {
            if (component instanceof ItemModelComponent) {
                return true;
            }
        }
        return false;
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
