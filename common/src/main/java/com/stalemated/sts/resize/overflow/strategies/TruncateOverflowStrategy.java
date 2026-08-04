package com.stalemated.sts.resize.overflow.strategies;

import com.stalemated.lib.util.style.TooltipStyleUtils;
import com.stalemated.sts.resize.components.TruncatedTitleTooltipComponent;
import com.stalemated.sts.resize.overflow.TitleOverflowStrategy;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TruncateOverflowStrategy implements TitleOverflowStrategy {

    @Override
    public List<TooltipComponent> processComponentPhase(List<TooltipComponent> components, TextRenderer textRenderer, int maxTitleWidth) {
        if (components.isEmpty() || textRenderer == null) {
            return components;
        }

        for (int i = 0; i < components.size(); i++) {
            TooltipComponent comp = components.get(i);
            Optional<OrderedText> extracted = TooltipStyleUtils.getExtractedTextValue(comp);

            if (extracted.isPresent()) {
                OrderedText value = extracted.get();

                if (textRenderer.getWidth(value) > maxTitleWidth) {
                    MutableText mutable = TooltipStyleUtils.convertOrderedTextToMutable(value);
                    List<TooltipComponent> mutableComponents = new ArrayList<>(components);

                    mutableComponents.set(i, new TruncatedTitleTooltipComponent(truncateTitle(mutable, textRenderer, maxTitleWidth).asOrderedText()));
                    return mutableComponents;
                }
                break;
            }
        }

        return components;
    }

    private MutableText truncateTitle(Text title, TextRenderer textRenderer, int maxWidth) {
        String truncatedIndicator = "...";
        int indicatorWidth = textRenderer.getWidth(truncatedIndicator);
        int availableWidth = Math.max(indicatorWidth, maxWidth - indicatorWidth);

        StringVisitable truncated = textRenderer.trimToWidth(title, availableWidth);
        MutableText rebuilt = TooltipStyleUtils.preserveStyles(truncated);

        rebuilt.append(Text.literal(truncatedIndicator).setStyle(title.getStyle()));
        return rebuilt;
    }
}
