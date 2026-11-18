package com.stalemated.sts.compat.legendarytooltips;

import com.anthonyhilyard.iceberg.util.Tooltips;
import com.anthonyhilyard.legendarytooltips.config.LegendaryTooltipsConfig;
import com.anthonyhilyard.legendarytooltips.tooltip.ItemModelComponent;
import com.anthonyhilyard.legendarytooltips.tooltip.PaddingComponent;
import com.stalemated.sts.resize.TitleOverflowMode;
import com.stalemated.sts.resize.TooltipStyle;
import com.stalemated.sts.state.StateManager;
import com.stalemated.sts.state.TooltipContextManager;
import com.stalemated.sts.state.TooltipDraw;
import com.stalemated.sts.util.OrderedTextUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class LegendaryTooltipsStyle implements TooltipStyle {
    private static final int ITEM_MODEL_WIDTH = 24;

    private final TooltipStyle next;

    public LegendaryTooltipsStyle(TooltipStyle next) {
        this.next = next;
    }

    @Override
    public int titleLength(List<ClientTooltipComponent> components) {
        for (int i = 0; i < components.size(); i++) {
            ClientTooltipComponent component = components.get(i);
            if (component instanceof PaddingComponent || component instanceof Tooltips.TitleBreakComponent) return i + 1;
        }
        return 1;
    }

    @Override
    public int modelOffset(ItemStack stack, List<ClientTooltipComponent> components) {
        if (stack == null || stack.isEmpty()) return 0;
        if (!LegendaryTooltipsConfig.showModelForItem(stack)) return 0;
        if (StateManager.isTierifyTooltip) return ITEM_MODEL_WIDTH;

        List<ClientTooltipComponent> title = TooltipDraw.titleComponents();
        if (!title.isEmpty()) return containsItemModel(title) ? ITEM_MODEL_WIDTH : 0;
        if (components != null && !components.isEmpty()) return containsItemModel(components) ? ITEM_MODEL_WIDTH : 0;
        return ITEM_MODEL_WIDTH;
    }

    @Override
    public int extraTitleWidth(Font font) {
        return next.extraTitleWidth(font);
    }

    @Override
    public boolean drawsModelOffset() {
        return next.drawsModelOffset();
    }

    @Override
    public boolean titleReservesModelWidth(TitleOverflowMode mode) {
        return next.titleReservesModelWidth(mode);
    }

    @Override
    public int wrappedTitlePadding(List<FormattedCharSequence> lines, int line) {
        if (lines.size() <= 1 || line != lines.size() - 1) return 0;

        boolean hasModel = currentModelOffset() > 0;
        if (StateManager.isTierifyTooltip) return hasModel ? 2 : 1;
        return hasModel ? 0 : 1;
    }

    @Override
    public FormattedCharSequence centeredTitle(FormattedCharSequence title) {
        return currentModelOffset() > 0 ? OrderedTextUtil.stripSpaces(title) : title;
    }

    private int currentModelOffset() {
        return modelOffset(TooltipContextManager.currentStack(), TooltipDraw.titleComponents());
    }

    private static boolean containsItemModel(List<ClientTooltipComponent> components) {
        for (ClientTooltipComponent component : components) {
            if (component instanceof ItemModelComponent) return true;
        }
        return false;
    }
}
