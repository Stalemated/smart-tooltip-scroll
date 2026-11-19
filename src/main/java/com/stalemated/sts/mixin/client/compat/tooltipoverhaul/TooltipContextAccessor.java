package com.stalemated.sts.mixin.client.compat.tooltipoverhaul;

import com.stalemated.lib.mixin.RequiresMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

@RequiresMod("tooltipoverhaul")
@Pseudo
@Mixin(targets = "dev.xylonity.tooltipoverhaul.client.render.TooltipContext")
public interface TooltipContextAccessor {
    @Mutable
    @Accessor(value = "components")
    void smart_tooltip_scroll$setComponents(List<ClientTooltipComponent> components);
}
