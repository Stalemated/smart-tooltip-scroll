package com.stalemated.sts.compat.obscure;

import com.stalemated.sts.compat.obscure.component.StsObscureHeaderComponent;
import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.resize.TooltipDimensionManager;
import com.stalemated.sts.resize.overflow.TitleOverflowStrategyFactory;
import com.stalemated.sts.scroll.TooltipScrollManager;
import com.stalemated.sts.scroll.components.ScrollableTooltipComponent;
import com.stalemated.sts.util.TooltipWrapUtil;
import dev.obscuria.tooltips.client.component.HeaderComponent;
import dev.obscuria.tooltips.client.component.SplitComponent;
import dev.obscuria.tooltips.client.component.StackBuffer;
import dev.obscuria.tooltips.config.ClientConfig;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.stalemated.sts.resize.TooltipDimensionManager.MIN_TOOLTIP_HEIGHT;
import static com.stalemated.sts.resize.TooltipDimensionManager.MIN_TOOLTIP_WIDTH;
import static com.stalemated.sts.scroll.components.ScrollableTooltipComponent.SCROLLBAR_WIDTH;
import static com.stalemated.sts.state.StateManager.IS_OT_LOADED;

public class ObscureTooltipsCompat {
    public static final int OT_ICON_SIZE = 20;
    public static final int OT_ICON_MARGIN = 2;
    public static final int OT_ITEM_MODEL_OFFSET = OT_ICON_SIZE + OT_ICON_MARGIN;
    public static final int SEPARATOR_HEIGHT = 3;
    public static final int SPLIT_GAP = 3;

    private static int getContentMargin() {
        return IS_OT_LOADED ? ClientConfig.CONTENT_MARGIN.get() : 2;
    }

    public static boolean isCompatActive() {
        return ConfigManager.getConfig().custom_tooltip_dimensions && ConfigManager.getConfig().obscure_compat;
    }

    public static boolean isObscureHandling(List<TooltipComponent> components) {
        if (!IS_OT_LOADED) return false;
        if (!ClientConfig.ENABLED.get()) return false;

        return containsStackBuffer(components);
    }

    private static boolean containsStackBuffer(List<TooltipComponent> components) {
        if (components == null || components.isEmpty()) return false;

        for (TooltipComponent comp : components) {
            if (comp instanceof StackBuffer) {
                return true;
            }
            if (comp != null && comp.getClass().getName().contains("ClientGroupTooltip")) {
                return true;
            }
        }
        return false;
    }

    public static List<TooltipComponent> processComponents(List<TooltipComponent> components, TextRenderer textRenderer) {
        if (components == null || components.isEmpty() || textRenderer == null) {
            return components;
        }

        if (components.size() == 1 && components.get(0) instanceof SplitComponent split) {
            return processSplitComponent(split, textRenderer);
        }

        if (components.get(0) instanceof HeaderComponent || components.get(0) instanceof StsObscureHeaderComponent) {
            return processStandardComponents(components, textRenderer);
        }

        return components;
    }

    private static TooltipComponent processHeader(TooltipComponent headerComp, int availableTextWidth, TextRenderer textRenderer) {
        if (headerComp instanceof HeaderComponent header) {
            TooltipComponent titleComp = header.title();

            List<TooltipComponent> processedTitleList = TitleOverflowStrategyFactory.getStrategy().processComponentPhase(Collections.singletonList(titleComp), textRenderer, availableTextWidth);
            TooltipComponent processedTitle = processedTitleList.isEmpty() ? titleComp : processedTitleList.get(0);

            return new StsObscureHeaderComponent(header, processedTitle);
        }
        return headerComp;
    }

    private static List<TooltipComponent> processStandardComponents(List<TooltipComponent> components, TextRenderer textRenderer) {
        int scaledWidth = TooltipDimensionManager.getScaledTooltipWidth();
        int scaledHeight = TooltipDimensionManager.getScaledTooltipHeight();

        int maxAllowedWidth = Math.max(scaledWidth - (getContentMargin() * 2), MIN_TOOLTIP_WIDTH);
        int maxAllowedHeight = Math.max(scaledHeight - (getContentMargin() * 2), MIN_TOOLTIP_HEIGHT);
        int availableTextWidth = Math.max(1, maxAllowedWidth - OT_ITEM_MODEL_OFFSET);

        TooltipComponent processedHeader = processHeader(components.get(0), availableTextWidth, textRenderer);

        if (components.size() <= 1) {
            TooltipScrollManager.updateMaxScroll(0);
            return Collections.singletonList(processedHeader);
        }

        List<TooltipComponent> bodyRaw = new ArrayList<>(components.subList(1, components.size()));

        // 1st pass: wrap body without discounting scrollbar width
        List<TooltipComponent> wrappedBody = TooltipWrapUtil.wrapComponents(bodyRaw, maxAllowedWidth, textRenderer, false);

        int headerHeight = processedHeader.getHeight();
        int bodyHeight = TooltipDimensionManager.calculateComponentListHeight(wrappedBody);
        int totalHeight = headerHeight + bodyHeight;

        if (totalHeight > maxAllowedHeight) {
            // 2nd pass: wrap body discounting scrollbar width
            ScrollableTooltipComponent scrollable = createScrollableTooltipComponent(textRenderer, maxAllowedWidth, maxAllowedHeight, processedHeader, bodyRaw, headerHeight);

            int totalTooltipWidth = Math.max(processedHeader.getWidth(textRenderer), scrollable.getWidth(textRenderer));
            if (processedHeader instanceof StsObscureHeaderComponent stsHeader) {
                stsHeader.setTotalWidth(totalTooltipWidth);
            }

            List<TooltipComponent> result = new ArrayList<>(2);
            result.add(processedHeader);
            result.add(scrollable);
            return result;
        }

        TooltipScrollManager.updateMaxScroll(0);
        int bodyWidth = TooltipDimensionManager.calculateTooltipWidth(wrappedBody, textRenderer);
        int totalTooltipWidth = Math.max(processedHeader.getWidth(textRenderer), bodyWidth);
        if (processedHeader instanceof StsObscureHeaderComponent stsHeader) {
            stsHeader.setTotalWidth(totalTooltipWidth);
        }

        List<TooltipComponent> result = new ArrayList<>(1 + wrappedBody.size());
        result.add(processedHeader);
        result.addAll(wrappedBody);
        return result;
    }

    public static ScrollableTooltipComponent createScrollableTooltipComponent(TextRenderer textRenderer, int maxAllowedWidth, int maxAllowedHeight, TooltipComponent processedHeader, List<TooltipComponent> bodyRaw, int headerHeight) {
        int scrollbarDiscountWidth = maxAllowedWidth - SCROLLBAR_WIDTH;
        int availableBodyHeight = Math.max(maxAllowedHeight - headerHeight, MIN_TOOLTIP_HEIGHT);

        List<TooltipComponent> wrappedBody = TooltipWrapUtil.wrapComponents(bodyRaw, scrollbarDiscountWidth, textRenderer, false);
        return new ScrollableTooltipComponent(
                wrappedBody,
                Collections.singletonList(processedHeader),
                availableBodyHeight,
                maxAllowedWidth,
                textRenderer
        );
    }

    private static List<TooltipComponent> processSplitComponent(SplitComponent splitComponent, TextRenderer textRenderer) {
        TooltipComponent left = splitComponent.left();
        List<TooltipComponent> right = splitComponent.right();

        if (right == null || right.isEmpty()) {
            TooltipScrollManager.updateMaxScroll(0);
            return Collections.singletonList(splitComponent);
        }

        int leftWidth = left.getWidth(textRenderer);
        int scaledWidth = TooltipDimensionManager.getScaledTooltipWidth();
        int scaledHeight = TooltipDimensionManager.getScaledTooltipHeight();

        int maxAllowedWidth = Math.max(scaledWidth - (getContentMargin() * 2), MIN_TOOLTIP_WIDTH);
        int maxAllowedHeight = Math.max(scaledHeight - (getContentMargin() * 2), MIN_TOOLTIP_HEIGHT);

        int rightAllowedWidth = Math.max(maxAllowedWidth - leftWidth - SPLIT_GAP, MIN_TOOLTIP_WIDTH);
        int availableTextWidth = Math.max(1, rightAllowedWidth - OT_ITEM_MODEL_OFFSET);

        TooltipComponent processedHeader = processHeader(right.get(0), availableTextWidth, textRenderer);
        List<TooltipComponent> bodyRaw = right.size() > 1
                ? new ArrayList<>(right.subList(1, right.size()))
                : Collections.emptyList();

        List<TooltipComponent> newRight;

        if (bodyRaw.isEmpty()) {
            TooltipScrollManager.updateMaxScroll(0);
            newRight = Collections.singletonList(processedHeader);
        } else {
            // 1st pass wrapping
            List<TooltipComponent> wrappedBody = TooltipWrapUtil.wrapComponents(bodyRaw, rightAllowedWidth, textRenderer, false);

            int headerHeight = processedHeader.getHeight();
            int bodyHeight = TooltipDimensionManager.calculateComponentListHeight(wrappedBody);
            int totalRightHeight = headerHeight + bodyHeight;
            int totalHeight = Math.max(left.getHeight(), totalRightHeight);

            if (totalHeight > maxAllowedHeight) {
                // 2nd pass: wrap body discounting scrollbar width
                ScrollableTooltipComponent scrollable = createScrollableTooltipComponent(textRenderer, rightAllowedWidth, maxAllowedHeight, processedHeader, bodyRaw, headerHeight);

                int totalRightWidth = Math.max(processedHeader.getWidth(textRenderer), scrollable.getWidth(textRenderer));
                if (processedHeader instanceof StsObscureHeaderComponent stsHeader) {
                    stsHeader.setTotalWidth(totalRightWidth);
                }

                newRight = new ArrayList<>(2);
                newRight.add(processedHeader);
                newRight.add(scrollable);
            } else {
                TooltipScrollManager.updateMaxScroll(0);
                int bodyWidth = TooltipDimensionManager.calculateTooltipWidth(wrappedBody, textRenderer);
                int totalRightWidth = Math.max(processedHeader.getWidth(textRenderer), bodyWidth);
                if (processedHeader instanceof StsObscureHeaderComponent stsHeader) {
                    stsHeader.setTotalWidth(totalRightWidth);
                }

                newRight = new ArrayList<>(1 + wrappedBody.size());
                newRight.add(processedHeader);
                newRight.addAll(wrappedBody);
            }
        }

        return Collections.singletonList(new SplitComponent(left, newRight));
    }
}
