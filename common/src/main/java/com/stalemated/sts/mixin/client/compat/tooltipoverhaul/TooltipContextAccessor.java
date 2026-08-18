package com.stalemated.sts.mixin.client.compat.tooltipoverhaul;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Pseudo
@Mixin(targets = "dev.xylonity.tooltipoverhaul.client.render.TooltipContext")
public interface TooltipContextAccessor {
    @Mutable
    @Accessor(value = "components")
    void sts$setComponents(List<TooltipComponent> components);
}
