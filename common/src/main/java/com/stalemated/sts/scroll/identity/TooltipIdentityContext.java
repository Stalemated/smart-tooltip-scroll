package com.stalemated.sts.scroll.identity;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public record TooltipIdentityContext(ItemStack itemStack, List<TooltipComponent> bodyComponents, TextRenderer textRenderer) {
    public TooltipIdentityContext(ItemStack itemStack, List<TooltipComponent> bodyComponents, TextRenderer textRenderer) {
        this.itemStack = itemStack;
        this.bodyComponents = bodyComponents != null ? bodyComponents : Collections.emptyList();
        this.textRenderer = textRenderer;
    }
}
