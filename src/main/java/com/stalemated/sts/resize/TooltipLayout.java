package com.stalemated.sts.resize;

import com.stalemated.sts.compat.tierify.TierifyCompat;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.resize.centering.TitleCenteringProcessor;
import com.stalemated.sts.resize.overflow.TitleOverflowStrategyFactory;
import com.stalemated.sts.scroll.TooltipScroll;
import com.stalemated.sts.scroll.components.ScrollableTooltipComponent;
import com.stalemated.sts.state.TooltipContext;
import com.stalemated.sts.state.TooltipDraw;
import com.stalemated.sts.util.TooltipWrap;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

public final class TooltipLayout {
    public static final int TITLE_BODY_GAP = 2;

    private TooltipLayout() {
    }

    public static List<ClientTooltipComponent> apply(List<ClientTooltipComponent> components, TooltipContext ctx) {
        return apply(components, ctx, TooltipLayout::height);
    }

    public static List<ClientTooltipComponent> apply(List<ClientTooltipComponent> components, TooltipContext ctx, ToIntFunction<List<ClientTooltipComponent>> titleHeight) {
        if (components.isEmpty()) return components;

        TooltipStyle style = TooltipStyles.loaded();
        int titleLength = Math.min(style.titleLength(components), components.size());
        TooltipBounds bounds = TooltipBounds.current();
        int modelOffset = style.modelOffset(ctx != null ? ctx.itemStack() : null, components);
        int titleMaxWidth = titleMaxWidth(style, bounds.maxWidth(), modelOffset);

        List<ClientTooltipComponent> title = new ArrayList<>(components.subList(0, titleLength));
        List<ClientTooltipComponent> rawBody = new ArrayList<>(components.subList(titleLength, components.size()));

        Font font = TooltipDraw.font();
        if (font == null) {
            TooltipDraw.setTitleComponents(title);
            TooltipDraw.setBodyComponents(rawBody);
            return components;
        }

        TooltipDraw.setTitleComponents(title);
        title = TitleOverflowStrategyFactory.getStrategy().processComponentPhase(title, font, titleMaxWidth);
        TooltipDraw.setTitleComponents(title);

        List<ClientTooltipComponent> laidOut = withScrollableBody(title, rawBody, bounds.maxWidth(), bounds.maxHeight(), titleHeight.applyAsInt(title), font);
        List<ClientTooltipComponent> body = new ArrayList<>(laidOut.subList(title.size(), laidOut.size()));
        TooltipDraw.setBodyComponents(body);

        if (!SmartTooltipScrollConfigRegistry.CONFIG.get().title_centering) return laidOut;

        int titleWidth = TitleCenteringProcessor.getCleanPinnedWidth(title, font, modelOffset);
        int tooltipWidth = Math.max(titleWidth, width(body, font));
        if (TierifyCompat.isDrawing()) tooltipWidth = Math.max(tooltipWidth, TooltipBounds.MIN_WIDTH);

        title = TitleCenteringProcessor.applyCentering(title, font, tooltipWidth, modelOffset);
        TooltipDraw.setTitleComponents(title);
        for (int i = 0; i < title.size(); i++) {
            laidOut.set(i, title.get(i));
        }
        return laidOut;
    }

    public static List<ClientTooltipComponent> withScrollableBody(List<ClientTooltipComponent> title, List<ClientTooltipComponent> rawBody, int maxWidth, int maxHeight, int titleHeight, Font font) {
        if (rawBody.isEmpty()) {
            TooltipScroll.INSTANCE.track(rawBody, font, 0);
            return new ArrayList<>(title);
        }

        List<ClientTooltipComponent> body = TooltipWrap.wrapComponents(rawBody, maxWidth, font, false);
        List<ClientTooltipComponent> laidOut = new ArrayList<>(title);

        if (titleHeight + height(body) > maxHeight) {
            body = TooltipWrap.wrapComponents(rawBody, maxWidth - ScrollableTooltipComponent.SCROLLBAR_WIDTH, font, false);
            int visibleHeight = Math.max(maxHeight - titleHeight, TooltipBounds.MIN_HEIGHT);
            ScrollableTooltipComponent scrollableBody = new ScrollableTooltipComponent(body, title, visibleHeight, maxWidth, font);
            TooltipScroll.INSTANCE.track(body, font, scrollableBody.maxScroll());
            laidOut.add(scrollableBody);
            return laidOut;
        }

        TooltipScroll.INSTANCE.track(body, font, 0);
        laidOut.addAll(body);
        return laidOut;
    }

    public static int height(List<ClientTooltipComponent> components) {
        int height = 0;
        for (int i = 0; i < components.size(); i++) {
            height += components.get(i).getHeight() + lineGap(i);
        }
        return height;
    }

    public static int width(List<ClientTooltipComponent> components, Font font) {
        if (components.isEmpty() || font == null) return 0;

        int width = 0;
        for (ClientTooltipComponent component : components) {
            width = Math.max(width, component.getWidth(font));
        }
        if (TierifyCompat.isDrawing()) width = Math.max(width, TooltipBounds.MIN_WIDTH);
        return width;
    }

    public static int lineGap(int line) {
        return line == 0 ? TITLE_BODY_GAP : 0;
    }

    private static int titleMaxWidth(TooltipStyle style, int maxWidth, int modelOffset) {
        int width = Math.max(maxWidth, TooltipBounds.MIN_WIDTH);
        boolean reservesModelWidth = TierifyCompat.isDrawing() || style.titleReservesModelWidth(SmartTooltipScrollConfigRegistry.CONFIG.get().title_overflow_mode);
        return reservesModelWidth ? width - modelOffset : width;
    }
}
