package com.stalemated.sts.resize.overflow.strategies;

import com.stalemated.sts.resize.components.ScrollingTitleTooltipComponent;
import com.stalemated.sts.resize.overflow.TitleOverflowStrategy;
import com.stalemated.lib.util.style.TooltipStyleUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



public class ScrollOverflowStrategy implements TitleOverflowStrategy {

    @Override
    public List<TooltipComponent> processComponentPhase(List<TooltipComponent> components, TextRenderer textRenderer, int maxTitleWidth) {
        List<TooltipComponent> modified = new ArrayList<>();
        for (TooltipComponent component : components) {
            Optional<OrderedText> extractedText = TooltipStyleUtils.getExtractedTextValue(component);

            if (extractedText.isPresent() && component.getWidth(textRenderer) > maxTitleWidth) {
                modified.add(new ScrollingTitleTooltipComponent(extractedText.get(), maxTitleWidth));
            } else {
                modified.add(component);
            }
        }
        return modified;
    }
}
