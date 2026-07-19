package com.stalemated.sts.resize.overflow.strategies;

import com.stalemated.sts.resize.overflow.TitleOverflowStrategy;
import com.stalemated.sts.util.TooltipWrapUtil;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;

import java.util.List;

public class WrapOverflowStrategy implements TitleOverflowStrategy {

    @Override
    public List<TooltipComponent> processComponentPhase(List<TooltipComponent> components, TextRenderer textRenderer, int maxTitleWidth) {
        if (components.isEmpty() || textRenderer == null) {
            return components;
        }
        return TooltipWrapUtil.wrapComponents(components, maxTitleWidth, textRenderer, true);
    }
}
