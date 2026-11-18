package com.stalemated.sts.resize;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class VanillaTooltipStyle implements TooltipStyle {
    @Override
    public int titleLength(List<ClientTooltipComponent> components) {
        return 1;
    }

    @Override
    public int modelOffset(ItemStack stack, List<ClientTooltipComponent> components) {
        return 0;
    }

    @Override
    public int extraTitleWidth(Font font) {
        return 0;
    }

    @Override
    public boolean drawsModelOffset() {
        return false;
    }

    @Override
    public boolean titleReservesModelWidth(TitleOverflowMode mode) {
        return true;
    }

    @Override
    public int wrappedTitlePadding(List<FormattedCharSequence> lines, int line) {
        return 0;
    }

    @Override
    public FormattedCharSequence centeredTitle(FormattedCharSequence title) {
        return title;
    }
}
