package com.stalemated.sts.util;

import com.stalemated.lib.component.IndentedTextTooltipComponent;
import com.stalemated.lib.helper.PlatformHelper;
import com.stalemated.lib.util.style.TooltipStyleUtils;
import com.stalemated.sts.compat.LegendaryTooltipsCompat;
import com.stalemated.sts.resize.TooltipDimensionManager;
import com.stalemated.sts.resize.components.WrappedTitleTooltipComponent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TooltipWrapUtil {
    public static boolean isHandlingCustomWrap = false;

    public static List<TooltipComponent> wrapComponents(List<TooltipComponent> components, int targetWidth, TextRenderer textRenderer, boolean isTitle) {
        List<TooltipComponent> wrappedComponents = new ArrayList<>();

        for (TooltipComponent comp : components) {
            boolean wrappedFallback = false;

            if (textRenderer != null) {
                Optional<OrderedText> extracted = TooltipStyleUtils.getExtractedTextValue(comp);

                if (extracted.isPresent()) {
                    OrderedText value = extracted.get();

                    if (textRenderer.getWidth(value) > targetWidth) {
                        MutableText mutable = TooltipStyleUtils.convertOrderedTextToMutable(value);
                        wrappedFallback = handleCustomWrap(targetWidth, textRenderer, wrappedComponents, mutable, isTitle);
                    }
                }
            }
            if (wrappedFallback) continue;
            wrappedComponents.add(comp);
        }
        return wrappedComponents;
    }

    public static boolean handleCustomWrap(int targetWidth, TextRenderer textRenderer, List<TooltipComponent> wrappedComponents, StringVisitable visitable, boolean isTitle) {
        isHandlingCustomWrap = true;
        List<OrderedText> wrapped = new ArrayList<>(textRenderer.wrapLines(visitable, targetWidth));
        isHandlingCustomWrap = false;

        List<TooltipComponent> titleLines = new ArrayList<>();

        for (int i = 0; i < wrapped.size(); i++) {
            OrderedText w = wrapped.get(i);

            int currentOffset = 0;
            if (isTitle) {
                if (i == 0) {
                    if (TooltipStyleUtils.convertOrderedTextToMutable(w).getString().isBlank()) {
                        continue;
                    }
                } else {
                    currentOffset = LegendaryTooltipsCompat.getItemModelComponentWidth(TooltipDimensionManager.getCurrentStack());
                }
                titleLines.add(new IndentedTextTooltipComponent(w, currentOffset));
            } else {
                wrappedComponents.add(new IndentedTextTooltipComponent(w, currentOffset));
            }
        }

        if (isTitle && !titleLines.isEmpty()) {
            if (titleLines.size() == 1) {
                wrappedComponents.add(titleLines.get(0));
            } else {
                if (PlatformHelper.INSTANCE.isModLoaded("legendarytooltips")) {
                    if (!TooltipDimensionManager.bodyComponentList.isEmpty()) {
                        wrappedComponents.addAll(titleLines);
                    } else {
                        wrappedComponents.add(new WrappedTitleTooltipComponent(titleLines));
                    }
                } else {
                    wrappedComponents.add(new WrappedTitleTooltipComponent(titleLines));
                }
            }
        }
        return true;
    }
}
