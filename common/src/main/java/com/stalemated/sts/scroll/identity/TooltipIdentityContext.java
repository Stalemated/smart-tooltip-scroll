package com.stalemated.sts.scroll.identity;

import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

public record TooltipIdentityContext(ItemStack itemStack, List<ClientTooltipComponent> bodyComponents, Font textRenderer) {
    public TooltipIdentityContext(ItemStack itemStack, List<ClientTooltipComponent> bodyComponents, Font textRenderer) {
        this.itemStack = itemStack;
        this.bodyComponents = bodyComponents != null ? bodyComponents : Collections.emptyList();
        this.textRenderer = textRenderer;
    }
}
