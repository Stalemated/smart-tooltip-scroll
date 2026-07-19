package com.stalemated.sts.resize.overflow;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;

import java.util.List;

public interface TitleOverflowStrategy {
    List<TooltipComponent> processComponentPhase(List<TooltipComponent> components, TextRenderer textRenderer, int maxTitleWidth);
}
