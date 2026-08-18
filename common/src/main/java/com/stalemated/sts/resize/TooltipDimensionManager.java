package com.stalemated.sts.resize;

import com.stalemated.lib.util.math.MathUtils;
import com.stalemated.sts.compat.legendarytooltips.LegendaryTooltipsCompat;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulLayoutFixer;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulStateManager;
import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.resize.centering.TitleCenteringProcessor;
import com.stalemated.sts.resize.overflow.TitleOverflowStrategyFactory;
import com.stalemated.sts.scroll.identity.TooltipIdentityContext;
import com.stalemated.sts.scroll.TooltipScrollManager;
import com.stalemated.sts.scroll.components.ScrollableTooltipComponent;
import com.stalemated.sts.state.StateManager;
import com.stalemated.sts.state.TooltipContext;
import com.stalemated.sts.state.TooltipContextManager;
import com.stalemated.sts.util.TooltipWrapUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static com.stalemated.sts.state.StateManager.IS_LT_LOADED;
import static com.stalemated.sts.state.StateManager.IS_TO_LOADED;

public class TooltipDimensionManager {

    public static DrawContext currentContext = null;
    public static TextRenderer currentTextRenderer = null;
    public static final int MIN_TOOLTIP_HEIGHT = 32;
    public static final int MIN_TOOLTIP_WIDTH = 64;
    public static final int TOOLTIP_PADDING_X = 8;
    private static final int TOOLTIP_PADDING_Y = 4;
    public static final int TITLE_BODY_VERTICAL_GAP = 2;
    public static List<TooltipComponent> processedTitleComponentList = Collections.emptyList();
    public static List<TooltipComponent> bodyComponentList = Collections.emptyList();
    public static Function<List<TooltipComponent>, Integer> pinnedHeightPredictor = null;

    private static final DimensionCache widthCache = new DimensionCache(TOOLTIP_PADDING_X, MIN_TOOLTIP_WIDTH);
    private static final DimensionCache heightCache = new DimensionCache(TOOLTIP_PADDING_Y, MIN_TOOLTIP_HEIGHT);

    private static class DimensionCache {
        private final int padding;
        private int lastWindowSize = -1;
        private int lastConfigPercent = -1;
        private int cachedSize = -1;
        private final int minSideLength;

        DimensionCache(int padding, int minSideLength) {
            this.padding = padding;
            this.minSideLength = minSideLength;
        }

        int get(int currentWindowSize, int currentConfigPercent) {
            if (currentWindowSize != lastWindowSize || currentConfigPercent != lastConfigPercent) {
                this.lastWindowSize = currentWindowSize;
                this.lastConfigPercent = currentConfigPercent;

                int maxAllowedSize = currentWindowSize - padding;
                int safePercent = MathUtils.clamp(currentConfigPercent, 1, 100);
                this.cachedSize = MathUtils.clamp(maxAllowedSize * safePercent / 100, minSideLength, maxAllowedSize);
            }
            return this.cachedSize;
        }
    }

    public static int calculateComponentListHeight(List<TooltipComponent> components) {
        if (components.isEmpty()) return 0;
        int totalHeight = 0;

        for (int i = 0; i < components.size(); i++) {
            totalHeight += components.get(i).getHeight() + getPaddingOffset(i);
        }
        return totalHeight;
    }

    public static int calculateTooltipWidth(List<TooltipComponent> components, TextRenderer textRenderer) {
        if (components.isEmpty() || textRenderer == null) return 0;
        int maxWidth = 0;

        for (TooltipComponent comp : components) {
            maxWidth = Math.max(maxWidth, comp.getWidth(textRenderer));
        }
        if (StateManager.isTierifyTooltip) {
            maxWidth = Math.max(maxWidth, MIN_TOOLTIP_WIDTH);
        }
        return maxWidth;
    }

    public static List<TooltipComponent> enforceHeightLimit(List<TooltipComponent> components, TooltipContext ctx) {
        if (components.isEmpty()) return components;

        int splitIndex = getSplitIndex(components);

        int scaledTooltipWidth = getScaledTooltipWidth();
        int scaledTooltipHeight = getScaledTooltipHeight();
        int componentWidth = getModelOffset(ctx != null ? ctx.itemStack() : null, components);
        int titleMaxWidth = getTitleMaxWidth(scaledTooltipWidth, componentWidth);

        List<TooltipComponent> pinned = new ArrayList<>(components.subList(0, splitIndex));
        List<TooltipComponent> scrollableContentRaw = new ArrayList<>(components.subList(splitIndex, components.size()));

        if (currentTextRenderer != null) {
            processedTitleComponentList = pinned;
            pinned = TitleOverflowStrategyFactory.getStrategy().processComponentPhase(pinned, currentTextRenderer, titleMaxWidth);
            processedTitleComponentList = pinned;
            
            int pinnedHeight = (pinnedHeightPredictor != null) ? pinnedHeightPredictor.apply(pinned) : calculateComponentListHeight(pinned);
            
            List<TooltipComponent> result = buildScrollableLayout(pinned, scrollableContentRaw, scaledTooltipWidth, scaledTooltipHeight, pinnedHeight, currentTextRenderer);

            bodyComponentList = new ArrayList<>(result.subList(pinned.size(), result.size()));

            if (ConfigManager.getConfig().title_centering) {
                int cleanPinnedWidth = TitleCenteringProcessor.getCleanPinnedWidth(pinned, currentTextRenderer, componentWidth);
                int bodyWidth = calculateTooltipWidth(bodyComponentList, currentTextRenderer);
                int totalTooltipWidth = Math.max(cleanPinnedWidth, bodyWidth);

                if (StateManager.isTierifyTooltip) {
                    totalTooltipWidth = Math.max(totalTooltipWidth, MIN_TOOLTIP_WIDTH);
                }

                pinned = TitleCenteringProcessor.applyCentering(pinned, currentTextRenderer, totalTooltipWidth, componentWidth);
                processedTitleComponentList = pinned;
                
                for (int i = 0; i < pinned.size(); i++) {
                    result.set(i, pinned.get(i));
                }
            }
            return result;
        }

        processedTitleComponentList = pinned;
        bodyComponentList = scrollableContentRaw;
        return components;
    }

    public static int getTitleMaxWidth(int scaledTooltipWidth, int componentWidth) {
        int titleMaxWidth;
        TitleOverflowMode overflowMode = ConfigManager.getConfig().title_overflow_mode;
        boolean isTOActive = IS_TO_LOADED && TooltipOverhaulStateManager.isStsActive();

        if (StateManager.isTierifyTooltip || !isTOActive || overflowMode == TitleOverflowMode.WRAP) {
            titleMaxWidth = Math.max(scaledTooltipWidth, MIN_TOOLTIP_WIDTH) - componentWidth;
        } else {
            titleMaxWidth = Math.max(scaledTooltipWidth, MIN_TOOLTIP_WIDTH);
        }
        return titleMaxWidth;
    }

    public static List<TooltipComponent> buildScrollableLayout(List<TooltipComponent> pinnedComponents, List<TooltipComponent> scrollableContentRaw, int bodyMaxAllowedWidth, int maxAllowedHeight, int pinnedHeight, TextRenderer textRenderer) {
        if (scrollableContentRaw.isEmpty()) {
            TooltipIdentityContext context = new TooltipIdentityContext(getCurrentStack(), scrollableContentRaw, textRenderer);
            TooltipScrollManager.INSTANCE.onTooltipRendered(0, context);
            return new ArrayList<>(pinnedComponents);
        }

        List<TooltipComponent> scrollableContent = TooltipWrapUtil.wrapComponents(scrollableContentRaw, bodyMaxAllowedWidth, textRenderer, false);
        
        int bodyHeight = calculateComponentListHeight(scrollableContent);
        int totalHeight = pinnedHeight + bodyHeight;

        if (totalHeight > maxAllowedHeight) {
            int scrollbarDiscountWidth = bodyMaxAllowedWidth - ScrollableTooltipComponent.SCROLLBAR_WIDTH;
            scrollableContent = TooltipWrapUtil.wrapComponents(scrollableContentRaw, scrollbarDiscountWidth, textRenderer, false);

            int availableHeight = Math.max(maxAllowedHeight - pinnedHeight, MIN_TOOLTIP_HEIGHT);

            ScrollableTooltipComponent scrollableComponent = new ScrollableTooltipComponent(scrollableContent, pinnedComponents, availableHeight, bodyMaxAllowedWidth, textRenderer);

            List<TooltipComponent> result = new ArrayList<>(pinnedComponents);
            result.add(scrollableComponent);
            return result;
        }

        TooltipIdentityContext context = new TooltipIdentityContext(getCurrentStack(), scrollableContent, textRenderer);
        TooltipScrollManager.INSTANCE.onTooltipRendered(0, context);
        List<TooltipComponent> result = new ArrayList<>(pinnedComponents);
        result.addAll(scrollableContent);
        return result;
    }

    public static int getSplitIndex(List<TooltipComponent> components) {
        int splitIndex = 1;
        if (IS_LT_LOADED) {
            splitIndex = LegendaryTooltipsCompat.getSplitIndex(components, splitIndex);
        }
        return Math.min(splitIndex, components.size());
    }

    public static ItemStack getCurrentStack() {
        TooltipContext ctx = TooltipContextManager.peek();
        return ctx != null ? ctx.itemStack() : null;
    }

    public static void setState(DrawContext context, TextRenderer textRenderer) {
        currentContext = context;
        currentTextRenderer = textRenderer;
    }

    public static void clearState() {
        currentContext = null;
        currentTextRenderer = null;
        TooltipContextManager.clear();
        processedTitleComponentList = Collections.emptyList();
        bodyComponentList = Collections.emptyList();
    }

    public static int getScaledTooltipHeight() {
        return heightCache.get(MinecraftClient.getInstance().getWindow().getScaledHeight(), ConfigManager.getConfig().max_height_percentage);
    }

    public static int getScaledTooltipWidth() {
        return widthCache.get(MinecraftClient.getInstance().getWindow().getScaledWidth(), ConfigManager.getConfig().max_width_percentage);
    }

    public static int getModelOffset() {
        return getModelOffset(getCurrentStack(), null);
    }

    public static int getModelOffset(ItemStack currentStack, List<TooltipComponent> currentComponents) {
        if (IS_LT_LOADED) {
            return LegendaryTooltipsCompat.getItemModelComponentWidth(currentStack, currentComponents);
        }
        if (IS_TO_LOADED) {
            return TooltipOverhaulLayoutFixer.getIconOffset();
        }
        return 0;
    }

    public static int getPaddingOffset(int i) {
        return (i == 0) ? TITLE_BODY_VERTICAL_GAP : 0;
    }

    public static int getExtraWidth(TextRenderer textRenderer) {
        if (IS_TO_LOADED) {
            return TooltipOverhaulLayoutFixer.getExtraWidth(textRenderer);
        }
        return 0;
    }

    public static boolean handlesModelOffsetNatively() {
        if (StateManager.isTierifyTooltip) return true;
        return IS_TO_LOADED && TooltipOverhaulStateManager.isHandlingTOTooltip();
    }
}