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

import static com.stalemated.sts.resize.TooltipDimensionManager.TITLE_BODY_VERTICAL_GAP;


public class LegendaryTooltipsCompat {

    public static int getItemModelComponentWidth(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;

        int offset = LegendaryTooltipsConfig.showModelForItem(stack) ? 24 : 0;
        if (StateManager.isTierifyTooltip) return offset;

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
        for (int i = 0; i < components.size(); i++) {

            if (components.get(i) instanceof PaddingComponent ||
                    components.get(i) instanceof Tooltips.TitleBreakComponent) {
                splitIndex = i + 1;
                break;
            }
        }
        return Math.min(splitIndex, components.size());
    }

    public static int getLTOffset(int i, int componentSize) {
        return i == 0 && componentSize > 1 ? TITLE_BODY_VERTICAL_GAP : 0;
    }
}
