package com.stalemated.sts.util;

import com.stalemated.lib.component.IndentedTextTooltipComponent;
import com.stalemated.lib.util.style.TooltipStyleUtils;
import com.stalemated.sts.resize.TooltipStyles;
import com.stalemated.sts.resize.components.StsIndentedTextTooltipComponent;
import com.stalemated.sts.resize.components.WrappedTitleTooltipComponent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TooltipWrap {

    public static List<ClientTooltipComponent> wrapComponents(List<ClientTooltipComponent> components, int targetWidth, Font textRenderer, boolean isTitle) {
        if (textRenderer == null) return new ArrayList<>(components);

        List<ClientTooltipComponent> wrappedComponents = new ArrayList<>();

        for (ClientTooltipComponent comp : components) {
            Optional<FormattedCharSequence> text = TooltipStyleUtils.getExtractedTextValue(comp);

            if (text.isPresent() && textRenderer.width(text.get()) > targetWidth) wrapText(TooltipStyleUtils.convertOrderedTextToMutable(text.get()), targetWidth, textRenderer, wrappedComponents, isTitle);
            else wrappedComponents.add(comp);
        }
        return wrappedComponents;
    }

    private static void wrapText(FormattedText visitable, int targetWidth, Font textRenderer, List<ClientTooltipComponent> wrappedComponents, boolean isTitle) {
        List<FormattedCharSequence> wrapped = new ArrayList<>(textRenderer.split(visitable, targetWidth));

        List<ClientTooltipComponent> titleLines = new ArrayList<>();

        for (int i = 0; i < wrapped.size(); i++) {
            FormattedCharSequence w = wrapped.get(i);

            int currentOffset = 0;
            if (isTitle) {
                if (i == 0) {
                    if (TooltipStyleUtils.convertOrderedTextToMutable(w).getString().isBlank()) continue;
                } else {
                    if (!TooltipStyles.drawsModelOffsetNatively()) currentOffset = TooltipStyles.currentModelOffset();
                }

                int padding = TooltipStyles.loaded().wrappedTitlePadding(wrapped, i);

                if (padding > 0) titleLines.add(new StsIndentedTextTooltipComponent(w, currentOffset, padding));
                else titleLines.add(new IndentedTextTooltipComponent(w, currentOffset));
            } else {
                wrappedComponents.add(new IndentedTextTooltipComponent(w, currentOffset));
            }
        }

        if (isTitle && !titleLines.isEmpty()) {
            if (titleLines.size() == 1) wrappedComponents.add(titleLines.get(0));
            else wrappedComponents.add(new WrappedTitleTooltipComponent(titleLines));
        }
    }
}
