package com.stalemated.sts.scroll.identity;

import net.minecraft.world.item.ItemStack;

public class TooltipIdentityTracker {
    private ItemStack lastRenderedStack = null;
    private TooltipLines lastLines = TooltipLines.NONE;

    public boolean hasTooltipChanged(TooltipIdentityContext context) {
        ItemStack currentStack = context.itemStack();
        TooltipLines lines = context.lines();

        if (currentStack != null && lastRenderedStack != null) return !ItemStack.matches(currentStack, lastRenderedStack) || !lines.sameHeights(lastLines);
        if (currentStack != lastRenderedStack) return true;
        return !lines.equals(lastLines); // Fallback for non-item tooltips
    }

    public void updateIdentity(TooltipIdentityContext context) {
        this.lastRenderedStack = context.itemStack();
        this.lastLines = context.lines();
    }
}
