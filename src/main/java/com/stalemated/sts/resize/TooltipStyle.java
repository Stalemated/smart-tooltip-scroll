package com.stalemated.sts.resize;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface TooltipStyle {
    int titleLength(List<ClientTooltipComponent> components);

    int modelOffset(ItemStack stack, List<ClientTooltipComponent> components);

    int extraTitleWidth(Font font);

    boolean drawsModelOffset();

    boolean titleReservesModelWidth(TitleOverflowMode mode);

    int wrappedTitlePadding(List<FormattedCharSequence> lines, int line);

    FormattedCharSequence centeredTitle(FormattedCharSequence title);
}
