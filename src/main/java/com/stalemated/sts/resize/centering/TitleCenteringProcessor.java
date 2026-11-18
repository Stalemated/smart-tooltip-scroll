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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.util.FormattedCharSequence;

import static com.stalemated.sts.scroll.ScrollState.PIXELS_PER_LINE;
import static com.stalemated.sts.state.StateManager.IS_LT_LOADED;

public class TitleCenteringProcessor {

    public static List<ClientTooltipComponent> applyCentering(List<ClientTooltipComponent> pinned, Font font, int totalTooltipWidth, int modelOffset) {
        if (pinned == null || pinned.isEmpty() || font == null) {
            return pinned;
        }

        int availableWidth = Math.max(0, totalTooltipWidth - modelOffset);
        List<ClientTooltipComponent> centeredList = new ArrayList<>(pinned.size());

        for (ClientTooltipComponent comp : pinned) {
            if (comp instanceof ScrollingTitleTooltipComponent || comp instanceof TruncatedTitleTooltipComponent) {
                centeredList.add(comp);
            } else if (comp instanceof WrappedTitleTooltipComponent wrapped) {
                List<ClientTooltipComponent> lines = wrapped.getWrappedLines();
                List<ClientTooltipComponent> centeredLines = new ArrayList<>(lines.size());

                for (ClientTooltipComponent line : lines) {
                    centeredLines.add(centerSingleLine(line, font, availableWidth, modelOffset));
                }
                centeredList.add(new WrappedTitleTooltipComponent(centeredLines));
            } else {
                centeredList.add(centerSingleLine(comp, font, availableWidth, modelOffset));
            }
        }

        return centeredList;
    }

    public static int getCleanPinnedWidth(List<ClientTooltipComponent> pinned, Font font, int modelOffset) {
        if (pinned == null || pinned.isEmpty() || font == null) return 0;
        int maxWidth = 0;
        for (ClientTooltipComponent comp : pinned) {
            if (comp instanceof WrappedTitleTooltipComponent wrapped) {
                for (ClientTooltipComponent line : wrapped.getWrappedLines()) {
                    maxWidth = Math.max(maxWidth, getSingleLineWidth(line, font, modelOffset));
                }
            } else {
                maxWidth = Math.max(maxWidth, getSingleLineWidth(comp, font, modelOffset));
            }
        }
        
        maxWidth = Math.max(maxWidth, TooltipDimensionManager.getExtraWidth(font) + modelOffset);
        
        return maxWidth;
    }

    private static int getSingleLineWidth(ClientTooltipComponent comp, Font font, int modelOffset) {
        Optional<FormattedCharSequence> textOpt = TooltipStyleUtils.getExtractedTextValue(comp);
        if (textOpt.isEmpty()) return comp.getWidth(font);

        FormattedCharSequence text = stripCenteredTextSpaces(textOpt.get());
        return font.width(text) + modelOffset;
    }

    private static ClientTooltipComponent centerSingleLine(ClientTooltipComponent comp, Font font, int availableWidth, int itemModelOffset) {
        Optional<FormattedCharSequence> textOpt = TooltipStyleUtils.getExtractedTextValue(comp);
        if (textOpt.isEmpty()) return comp;

        FormattedCharSequence text = stripCenteredTextSpaces(textOpt.get());

        int textWidth = font.width(text);
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

    private static FormattedCharSequence stripCenteredTextSpaces(FormattedCharSequence text) {
        if (IS_LT_LOADED) {
            boolean hasItemModel = LegendaryTooltipsCompat.getItemModelComponentWidth() > 0;
            if (hasItemModel) text = OrderedTextUtil.stripSpaces(text);
        }
        return text;
    }
}
