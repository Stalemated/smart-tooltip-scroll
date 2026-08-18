package com.stalemated.sts.resize.centering;

import com.stalemated.lib.util.style.TooltipStyleUtils;
import com.stalemated.sts.compat.legendarytooltips.LegendaryTooltipsCompat;
import com.stalemated.sts.resize.TooltipDimensionManager;
import com.stalemated.sts.resize.components.CenteredTextTooltipComponent;
import com.stalemated.sts.resize.components.ScrollingTitleTooltipComponent;
import com.stalemated.sts.resize.components.StsIndentedTextTooltipComponent;
import com.stalemated.sts.resize.components.TruncatedTitleTooltipComponent;
import com.stalemated.sts.resize.components.WrappedTitleTooltipComponent;
import com.stalemated.sts.util.OrderedTextUtil;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.OrderedText;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.stalemated.sts.scroll.ScrollState.PIXELS_PER_LINE;
import static com.stalemated.sts.state.StateManager.IS_LT_LOADED;

public class TitleCenteringProcessor {

    public static List<TooltipComponent> applyCentering(List<TooltipComponent> pinned, TextRenderer font, int totalTooltipWidth, int modelOffset) {
        if (pinned == null || pinned.isEmpty() || font == null) {
            return pinned;
        }

        int availableWidth = Math.max(0, totalTooltipWidth - modelOffset);
        List<TooltipComponent> centeredList = new ArrayList<>(pinned.size());

        for (TooltipComponent comp : pinned) {
            if (comp instanceof ScrollingTitleTooltipComponent || comp instanceof TruncatedTitleTooltipComponent) {
                centeredList.add(comp);
            } else if (comp instanceof WrappedTitleTooltipComponent wrapped) {
                List<TooltipComponent> lines = wrapped.getWrappedLines();
                List<TooltipComponent> centeredLines = new ArrayList<>(lines.size());

                for (TooltipComponent line : lines) {
                    centeredLines.add(centerSingleLine(line, font, availableWidth, modelOffset));
                }
                centeredList.add(new WrappedTitleTooltipComponent(centeredLines));
            } else {
                centeredList.add(centerSingleLine(comp, font, availableWidth, modelOffset));
            }
        }

        return centeredList;
    }

    public static int getCleanPinnedWidth(List<TooltipComponent> pinned, TextRenderer font, int modelOffset) {
        if (pinned == null || pinned.isEmpty() || font == null) return 0;
        int maxWidth = 0;
        for (TooltipComponent comp : pinned) {
            if (comp instanceof WrappedTitleTooltipComponent wrapped) {
                for (TooltipComponent line : wrapped.getWrappedLines()) {
                    maxWidth = Math.max(maxWidth, getSingleLineWidth(line, font, modelOffset));
                }
            } else {
                maxWidth = Math.max(maxWidth, getSingleLineWidth(comp, font, modelOffset));
            }
        }
        
        maxWidth = Math.max(maxWidth, TooltipDimensionManager.getExtraWidth(font) + modelOffset);
        
        return maxWidth;
    }

    private static int getSingleLineWidth(TooltipComponent comp, TextRenderer font, int modelOffset) {
        Optional<OrderedText> textOpt = TooltipStyleUtils.getExtractedTextValue(comp);
        if (textOpt.isEmpty()) return comp.getWidth(font);

        OrderedText text = stripCenteredTextSpaces(textOpt.get());
        return font.getWidth(text) + modelOffset;
    }

    private static TooltipComponent centerSingleLine(TooltipComponent comp, TextRenderer font, int availableWidth, int itemModelOffset) {
        Optional<OrderedText> textOpt = TooltipStyleUtils.getExtractedTextValue(comp);
        if (textOpt.isEmpty()) return comp;

        OrderedText text = stripCenteredTextSpaces(textOpt.get());

        int textWidth = font.getWidth(text);
        int centerOffset = Math.max(0, (availableWidth - textWidth) / 2);

        int bottomPadding = 0;

        if (comp instanceof StsIndentedTextTooltipComponent stsInd) {
            bottomPadding = Math.max(0, stsInd.getHeight() - PIXELS_PER_LINE);
        }
        
        if (TooltipDimensionManager.handlesModelOffsetNatively()) {
            itemModelOffset = 0;
        }

        return new CenteredTextTooltipComponent(text, itemModelOffset, centerOffset, bottomPadding);
    }

    private static OrderedText stripCenteredTextSpaces(OrderedText text) {
        if (IS_LT_LOADED) {
            boolean hasItemModel = LegendaryTooltipsCompat.getItemModelComponentWidth() > 0;
            if (hasItemModel) text = OrderedTextUtil.stripSpaces(text);
        }
        return text;
    }
}
