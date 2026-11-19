package com.stalemated.sts.resize.overflow.strategies;

import com.stalemated.sts.resize.overflow.TitleOverflowStrategy;
import com.stalemated.sts.util.TooltipWrap;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public class WrapOverflowStrategy implements TitleOverflowStrategy {

    @Override
    public List<ClientTooltipComponent> processComponentPhase(List<ClientTooltipComponent> components, Font textRenderer, int maxTitleWidth) {
        if (components.isEmpty() || textRenderer == null) {
            return components;
        }
        return TooltipWrap.wrapComponents(components, maxTitleWidth, textRenderer, true);
    }
}
