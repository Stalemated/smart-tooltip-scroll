package com.stalemated.sts.resize.overflow.strategies;

import com.stalemated.lib.util.style.TooltipStyleUtils;
import com.stalemated.sts.resize.components.TruncatedTitleTooltipComponent;
import com.stalemated.sts.resize.overflow.TitleOverflowStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;

public class TruncateOverflowStrategy implements TitleOverflowStrategy {

    @Override
    public List<ClientTooltipComponent> processComponentPhase(List<ClientTooltipComponent> components, Font textRenderer, int maxTitleWidth) {
        if (components.isEmpty() || textRenderer == null) {
            return components;
        }

        for (int i = 0; i < components.size(); i++) {
            ClientTooltipComponent comp = components.get(i);
            Optional<FormattedCharSequence> extracted = TooltipStyleUtils.getExtractedTextValue(comp);

            if (extracted.isPresent()) {
                FormattedCharSequence value = extracted.get();

                if (textRenderer.width(value) > maxTitleWidth) {
                    MutableComponent mutable = TooltipStyleUtils.convertOrderedTextToMutable(value);
                    List<ClientTooltipComponent> mutableComponents = new ArrayList<>(components);

                    mutableComponents.set(i, new TruncatedTitleTooltipComponent(truncateTitle(mutable, textRenderer, maxTitleWidth).getVisualOrderText()));
                    return mutableComponents;
                }
                break;
            }
        }

        return components;
    }

    private MutableComponent truncateTitle(Component title, Font textRenderer, int maxWidth) {
        String truncatedIndicator = "...";
        int indicatorWidth = textRenderer.width(truncatedIndicator);
        int availableWidth = Math.max(indicatorWidth, maxWidth - indicatorWidth);

        FormattedText truncated = textRenderer.substrByWidth(title, availableWidth);
        MutableComponent rebuilt = TooltipStyleUtils.preserveStyles(truncated);

        rebuilt.append(Component.literal(truncatedIndicator).setStyle(title.getStyle()));
        return rebuilt;
    }
}
