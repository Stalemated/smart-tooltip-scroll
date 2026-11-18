package com.stalemated.sts.resize.overflow.strategies;

import com.stalemated.sts.resize.components.ScrollingTitleTooltipComponent;
import com.stalemated.sts.resize.overflow.TitleOverflowStrategy;
import com.stalemated.lib.util.style.TooltipStyleUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.util.FormattedCharSequence;



public class ScrollOverflowStrategy implements TitleOverflowStrategy {

    @Override
    public List<ClientTooltipComponent> processComponentPhase(List<ClientTooltipComponent> components, Font textRenderer, int maxTitleWidth) {
        List<ClientTooltipComponent> modified = new ArrayList<>();
        for (ClientTooltipComponent component : components) {
            Optional<FormattedCharSequence> extractedText = TooltipStyleUtils.getExtractedTextValue(component);

            if (extractedText.isPresent() && component.getWidth(textRenderer) > maxTitleWidth) {
                modified.add(new ScrollingTitleTooltipComponent(extractedText.get(), maxTitleWidth));
            } else {
                modified.add(component);
            }
        }
        return modified;
    }
}
