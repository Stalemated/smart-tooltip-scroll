package com.stalemated.sts.mixin.client.compat.tooltipoverhaul;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

@Pseudo
@Mixin(targets = "dev.xylonity.tooltipoverhaul.client.render.TooltipContext")
public interface TooltipContextAccessor {
    @Mutable
    @Accessor(value = "components")
    void sts$setComponents(List<ClientTooltipComponent> components);
}
