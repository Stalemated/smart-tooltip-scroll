package com.stalemated.sts.resize;

import com.stalemated.lib.util.math.MathUtils;
import com.stalemated.sts.compat.legendarytooltips.LegendaryTooltipsCompat;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulLayoutFixer;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulStateManager;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.resize.centering.TitleCenteringProcessor;
import com.stalemated.sts.resize.overflow.TitleOverflowStrategyFactory;
import com.stalemated.sts.scroll.identity.TooltipIdentityContext;
import com.stalemated.sts.scroll.TooltipScrollManager;
import com.stalemated.sts.scroll.components.ScrollableTooltipComponent;
import com.stalemated.sts.state.StateManager;
import com.stalemated.sts.state.TooltipContext;
import com.stalemated.sts.state.TooltipContextManager;
import com.stalemated.sts.state.TooltipDraw;
import com.stalemated.sts.util.TooltipWrapUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

import static com.stalemated.sts.state.StateManager.IS_LT_LOADED;
import static com.stalemated.sts.state.StateManager.IS_TO_LOADED;

public class TooltipDimensionManager {

    public static final int MIN_TOOLTIP_HEIGHT = 32;
    public static final int MIN_TOOLTIP_WIDTH = 64;
    public static final int TOOLTIP_PADDING_X = 8;
    private static final int TOOLTIP_PADDING_Y = 4;
    public static final int TITLE_BODY_VERTICAL_GAP = 2;
    public static Function<List<ClientTooltipComponent>, Integer> pinnedHeightPredictor = null;

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

    public static int calculateComponentListHeight(List<ClientTooltipComponent> components) {
        if (components.isEmpty()) return 0;
        int totalHeight = 0;

        for (int i = 0; i < components.size(); i++) {
            totalHeight += components.get(i).getHeight() + getPaddingOffset(i);
        }
        return totalHeight;
    }

    public static int calculateTooltipWidth(List<ClientTooltipComponent> components, Font textRenderer) {
        if (components.isEmpty() || textRenderer == null) return 0;
        int maxWidth = 0;

        for (ClientTooltipComponent comp : components) {
            maxWidth = Math.max(maxWidth, comp.getWidth(textRenderer));
        }
        if (StateManager.isTierifyTooltip) {
            maxWidth = Math.max(maxWidth, MIN_TOOLTIP_WIDTH);
        }
        return maxWidth;
    }

    public static List<ClientTooltipComponent> enforceHeightLimit(List<ClientTooltipComponent> components, TooltipContext ctx) {
        if (components.isEmpty()) return components;

        int splitIndex = getSplitIndex(components);

        int scaledTooltipWidth = getScaledTooltipWidth();
        int scaledTooltipHeight = getScaledTooltipHeight();
        int componentWidth = getModelOffset(ctx != null ? ctx.itemStack() : null, components);
        int titleMaxWidth = getTitleMaxWidth(scaledTooltipWidth, componentWidth);

        List<ClientTooltipComponent> pinned = new ArrayList<>(components.subList(0, splitIndex));
        List<ClientTooltipComponent> scrollableContentRaw = new ArrayList<>(components.subList(splitIndex, components.size()));

        Font font = TooltipDraw.font();
        if (font != null) {
            TooltipDraw.setTitleComponents(pinned);
            pinned = TitleOverflowStrategyFactory.getStrategy().processComponentPhase(pinned, font, titleMaxWidth);
            TooltipDraw.setTitleComponents(pinned);
            
            int pinnedHeight = (pinnedHeightPredictor != null) ? pinnedHeightPredictor.apply(pinned) : calculateComponentListHeight(pinned);
            
            List<ClientTooltipComponent> result = buildScrollableLayout(pinned, scrollableContentRaw, scaledTooltipWidth, scaledTooltipHeight, pinnedHeight, font);

            List<ClientTooltipComponent> body = new ArrayList<>(result.subList(pinned.size(), result.size()));
            TooltipDraw.setBodyComponents(body);

            if (SmartTooltipScrollConfigRegistry.CONFIG.get().title_centering) {
                int cleanPinnedWidth = TitleCenteringProcessor.getCleanPinnedWidth(pinned, font, componentWidth);
                int bodyWidth = calculateTooltipWidth(body, font);
                int totalTooltipWidth = Math.max(cleanPinnedWidth, bodyWidth);

                if (StateManager.isTierifyTooltip) {
                    totalTooltipWidth = Math.max(totalTooltipWidth, MIN_TOOLTIP_WIDTH);
                }

                pinned = TitleCenteringProcessor.applyCentering(pinned, font, totalTooltipWidth, componentWidth);
                TooltipDraw.setTitleComponents(pinned);
                
                for (int i = 0; i < pinned.size(); i++) {
                    result.set(i, pinned.get(i));
                }
            }
            return result;
        }

        TooltipDraw.setTitleComponents(pinned);
        TooltipDraw.setBodyComponents(scrollableContentRaw);
        return components;
    }

    public static int getTitleMaxWidth(int scaledTooltipWidth, int componentWidth) {
        int titleMaxWidth;
        TitleOverflowMode overflowMode = SmartTooltipScrollConfigRegistry.CONFIG.get().title_overflow_mode;
        boolean isTOActive = IS_TO_LOADED && TooltipOverhaulStateManager.isStsActive();

        if (StateManager.isTierifyTooltip || !isTOActive || overflowMode == TitleOverflowMode.WRAP) {
            titleMaxWidth = Math.max(scaledTooltipWidth, MIN_TOOLTIP_WIDTH) - componentWidth;
        } else {
            titleMaxWidth = Math.max(scaledTooltipWidth, MIN_TOOLTIP_WIDTH);
        }
        return titleMaxWidth;
    }

    public static List<ClientTooltipComponent> buildScrollableLayout(List<ClientTooltipComponent> pinnedComponents, List<ClientTooltipComponent> scrollableContentRaw, int bodyMaxAllowedWidth, int maxAllowedHeight, int pinnedHeight, Font textRenderer) {
        if (scrollableContentRaw.isEmpty()) {
            TooltipIdentityContext context = new TooltipIdentityContext(getCurrentStack(), scrollableContentRaw, textRenderer);
            TooltipScrollManager.INSTANCE.onTooltipRendered(0, context);
            return new ArrayList<>(pinnedComponents);
        }

        List<ClientTooltipComponent> scrollableContent = TooltipWrapUtil.wrapComponents(scrollableContentRaw, bodyMaxAllowedWidth, textRenderer, false);
        
        int bodyHeight = calculateComponentListHeight(scrollableContent);
        int totalHeight = pinnedHeight + bodyHeight;

        if (totalHeight > maxAllowedHeight) {
            int scrollbarDiscountWidth = bodyMaxAllowedWidth - ScrollableTooltipComponent.SCROLLBAR_WIDTH;
            scrollableContent = TooltipWrapUtil.wrapComponents(scrollableContentRaw, scrollbarDiscountWidth, textRenderer, false);

            int availableHeight = Math.max(maxAllowedHeight - pinnedHeight, MIN_TOOLTIP_HEIGHT);

            ScrollableTooltipComponent scrollableComponent = new ScrollableTooltipComponent(scrollableContent, pinnedComponents, availableHeight, bodyMaxAllowedWidth, textRenderer);

            List<ClientTooltipComponent> result = new ArrayList<>(pinnedComponents);
            result.add(scrollableComponent);
            return result;
        }

        TooltipIdentityContext context = new TooltipIdentityContext(getCurrentStack(), scrollableContent, textRenderer);
        TooltipScrollManager.INSTANCE.onTooltipRendered(0, context);
        List<ClientTooltipComponent> result = new ArrayList<>(pinnedComponents);
        result.addAll(scrollableContent);
        return result;
    }

    public static int getSplitIndex(List<ClientTooltipComponent> components) {
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

    public static int getScaledTooltipHeight() {
        return heightCache.get(Minecraft.getInstance().getWindow().getGuiScaledHeight(), SmartTooltipScrollConfigRegistry.CONFIG.get().max_height_percentage);
    }

    public static int getScaledTooltipWidth() {
        return widthCache.get(Minecraft.getInstance().getWindow().getGuiScaledWidth(), SmartTooltipScrollConfigRegistry.CONFIG.get().max_width_percentage);
    }

    public static int getModelOffset() {
        return getModelOffset(getCurrentStack(), null);
    }

    public static int getModelOffset(ItemStack currentStack, List<ClientTooltipComponent> currentComponents) {
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

    public static int getExtraWidth(Font textRenderer) {
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