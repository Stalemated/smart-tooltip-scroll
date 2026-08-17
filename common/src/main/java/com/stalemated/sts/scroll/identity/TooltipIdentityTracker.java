package com.stalemated.sts.scroll.identity;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;

public class TooltipIdentityTracker {
    private ItemStack lastRenderedStack = null;
    private int lastTooltipIdentityHash = 0;

    public boolean hasTooltipChanged(TooltipIdentityContext context) {
        ItemStack currentStack = context.itemStack();
        int identityHash = calculateIdentityHash(context);

        boolean isDifferentTooltip = false;

        if (currentStack != null && lastRenderedStack != null) {
            if (!ItemStack.areEqual(currentStack, lastRenderedStack)) {
                isDifferentTooltip = true;
            }
        } else if (currentStack != lastRenderedStack) {
            isDifferentTooltip = true;
        } else if (identityHash != lastTooltipIdentityHash) {
            isDifferentTooltip = true; // Fallback for non-item tooltips
        }

        return isDifferentTooltip;
    }

    public void updateIdentity(TooltipIdentityContext context) {
        this.lastRenderedStack = context.itemStack();
        this.lastTooltipIdentityHash = calculateIdentityHash(context);
    }

    private int calculateIdentityHash(TooltipIdentityContext context) {
        int hash = 0;
        for (TooltipComponent comp : context.bodyComponents()) {
            if (comp != null) {
                hash = 31 * hash + comp.getHeight();
                if (context.textRenderer() != null) {
                    hash = 31 * hash + comp.getWidth(context.textRenderer());
                }
            }
        }
        return hash;
    }
}
