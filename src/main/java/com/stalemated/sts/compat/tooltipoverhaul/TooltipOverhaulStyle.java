package com.stalemated.sts.compat.tooltipoverhaul;

import com.stalemated.sts.resize.TitleOverflowMode;
import com.stalemated.sts.resize.TooltipStyle;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class TooltipOverhaulStyle implements TooltipStyle {
    private final TooltipStyle next;

    public TooltipOverhaulStyle(TooltipStyle next) {
        this.next = next;
    }

    @Override
    public int titleLength(List<ClientTooltipComponent> components) {
        return next.titleLength(components);
    }

    @Override
    public int modelOffset(ItemStack stack, List<ClientTooltipComponent> components) {
        return TooltipOverhaulLayoutFixer.getIconOffset();
    }

    @Override
    public int extraTitleWidth(Font font) {
        return TooltipOverhaulLayoutFixer.getExtraWidth(font);
    }

    @Override
    public boolean drawsModelOffset() {
        return TooltipOverhaulDraw.isHandlingTOTooltip();
    }

    @Override
    public boolean titleReservesModelWidth(TitleOverflowMode mode) {
        return mode == TitleOverflowMode.WRAP || !TooltipOverhaulDraw.isStsActive();
    }

    @Override
    public int wrappedTitlePadding(List<FormattedCharSequence> lines, int line) {
        return next.wrappedTitlePadding(lines, line);
    }

    @Override
    public FormattedCharSequence centeredTitle(FormattedCharSequence title) {
        return next.centeredTitle(title);
    }

    @Override
    public void resetExternalScroll() {
        TooltipOverhaulCompat.resetScrollState();
    }
}
