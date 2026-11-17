package com.stalemated.sts.util;

import com.stalemated.lib.component.IndentedTextTooltipComponent;
import com.stalemated.lib.util.style.TooltipStyleUtils;
import com.stalemated.sts.compat.legendarytooltips.LegendaryTooltipsCompat;
import com.stalemated.sts.resize.TooltipDimensionManager;
import com.stalemated.sts.resize.components.StsIndentedTextTooltipComponent;
import com.stalemated.sts.resize.components.WrappedTitleTooltipComponent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.stalemated.sts.state.StateManager.IS_LT_LOADED;

public class TooltipWrapUtil {
    public static boolean isHandlingCustomWrap = false;

    public static List<ClientTooltipComponent> wrapComponents(List<ClientTooltipComponent> components, int targetWidth, Font textRenderer, boolean isTitle) {
        List<ClientTooltipComponent> wrappedComponents = new ArrayList<>();

        for (ClientTooltipComponent comp : components) {
            boolean wrappedFallback = false;

            if (textRenderer != null) {
                Optional<FormattedCharSequence> extracted = TooltipStyleUtils.getExtractedTextValue(comp);

                if (extracted.isPresent()) {
                    FormattedCharSequence value = extracted.get();

                    if (textRenderer.width(value) > targetWidth) {
                        MutableComponent mutable = TooltipStyleUtils.convertOrderedTextToMutable(value);
                        wrappedFallback = handleCustomWrap(targetWidth, textRenderer, wrappedComponents, mutable, isTitle);
                    }
                }
            }
            if (wrappedFallback) continue;
            wrappedComponents.add(comp);
        }
        return wrappedComponents;
    }

    public static boolean handleCustomWrap(int targetWidth, Font textRenderer, List<ClientTooltipComponent> wrappedComponents, FormattedText visitable, boolean isTitle) {
        isHandlingCustomWrap = true;
        List<FormattedCharSequence> wrapped = new ArrayList<>(textRenderer.split(visitable, targetWidth));
        isHandlingCustomWrap = false;

        List<ClientTooltipComponent> titleLines = new ArrayList<>();

        for (int i = 0; i < wrapped.size(); i++) {
            FormattedCharSequence w = wrapped.get(i);

            int currentOffset = 0;
            if (isTitle) {
                if (i == 0) {
                    if (TooltipStyleUtils.convertOrderedTextToMutable(w).getString().isBlank()) continue;
                } else {
                    if (!TooltipDimensionManager.handlesModelOffsetNatively()) currentOffset = TooltipDimensionManager.getModelOffset();
                }

                int padding = getPadding(wrapped, i);

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
        return true;
    }

    public static int getPadding(List<FormattedCharSequence> wrapped, int i) {
        int padding = 0;
        if (IS_LT_LOADED) {
            padding = LegendaryTooltipsCompat.getPadding(wrapped, i, padding);
        }
        return padding;
    }
}
