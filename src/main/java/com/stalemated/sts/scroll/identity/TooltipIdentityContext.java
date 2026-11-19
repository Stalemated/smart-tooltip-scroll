package com.stalemated.sts.scroll.identity;

import net.minecraft.world.item.ItemStack;

public record TooltipIdentityContext(ItemStack itemStack, TooltipLines lines) {
}
