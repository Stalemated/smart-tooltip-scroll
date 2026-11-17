package com.stalemated.sts.resize.overflow;

import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public interface TitleOverflowStrategy {
    List<ClientTooltipComponent> processComponentPhase(List<ClientTooltipComponent> components, Font textRenderer, int maxTitleWidth);
}
