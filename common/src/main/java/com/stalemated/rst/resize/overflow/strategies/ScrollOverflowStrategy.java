package com.stalemated.rst.resize.overflow.strategies;

import com.stalemated.rst.resize.components.ScrollingTitleTooltipComponent;
import com.stalemated.rst.resize.overflow.TitleOverflowStrategy;
import com.stalemated.rst.util.TooltipTextUtil;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScrollOverflowStrategy implements TitleOverflowStrategy {

    @Override
    public List<Text> processTextPhase(List<Text> textList, TextRenderer textRenderer, int maxTitleWidth) {
        return textList;
    }

    @Override
    public List<TooltipComponent> processComponentPhase(List<TooltipComponent> components, TextRenderer textRenderer, int maxTitleWidth) {
        List<TooltipComponent> modified = new ArrayList<>();
        for (TooltipComponent component : components) {
            Optional<OrderedText> extractedText = TooltipTextUtil.getExtractedTextValue(component);

            if (extractedText.isPresent() && component.getWidth(textRenderer) > maxTitleWidth) {
                modified.add(new ScrollingTitleTooltipComponent(extractedText.get(), maxTitleWidth));
            } else {
                modified.add(component);
            }
        }
        return modified;
    }
}
