package com.stalemated.sts.compat.obscure;

import com.stalemated.sts.compat.obscure.component.StsObscureHeaderComponent;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.resize.TooltipBounds;
import com.stalemated.sts.resize.TooltipLayout;
import com.stalemated.sts.resize.overflow.TitleOverflowStrategyFactory;
import com.stalemated.sts.scroll.TooltipScroll;
import com.stalemated.sts.state.TooltipContexts;
import com.stalemated.sts.state.TooltipDraw;
import dev.obscuria.tooltips.client.component.HeaderComponent;
import dev.obscuria.tooltips.client.component.SplitComponent;
import dev.obscuria.tooltips.client.component.StackBuffer;
import dev.obscuria.tooltips.config.ClientConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

import static com.stalemated.sts.resize.TooltipBounds.MIN_HEIGHT;
import static com.stalemated.sts.resize.TooltipBounds.MIN_WIDTH;

public class ObscureTooltipsCompat {
    public static final int OT_ICON_SIZE = 20;
    public static final int OT_ICON_MARGIN = 2;
    public static final int OT_ITEM_MODEL_OFFSET = OT_ICON_SIZE + OT_ICON_MARGIN;
    public static final int SEPARATOR_HEIGHT = 3;
    public static final int SPLIT_GAP = 3;

    private static int getContentMargin() {
        return ClientConfig.CONTENT_MARGIN.get();
    }

    public static boolean isCompatActive() {
        return SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions && SmartTooltipScrollConfigRegistry.CONFIG.get().obscure_compat;
    }

    public static void beginDraw(GuiGraphics graphics, Font font) {
        if (isCompatActive()) TooltipDraw.begin(graphics, font);
    }

    public static List<ClientTooltipComponent> layOut(List<ClientTooltipComponent> postWrapped, Font font) {
        if (!isCompatActive()) return postWrapped;
        return processComponents(postWrapped, font);
    }

    public static float scroll(float obscureScroll) {
        return isCompatActive() ? 0.0F : obscureScroll;
    }

    public static void endDraw(boolean drawn) {
        if (isCompatActive() && drawn) TooltipDraw.end();
    }

    public static boolean isObscureHandling(List<ClientTooltipComponent> components) {
        if (!ClientConfig.ENABLED.get()) return false;
        ItemStack stack = TooltipContexts.currentStack();
        boolean isIgnored = stack != null && ClientConfig.isIgnored(stack.getItem());
        if (isIgnored) return false;

        return containsStackBuffer(components);
    }

    private static boolean containsStackBuffer(List<ClientTooltipComponent> components) {
        if (components == null || components.isEmpty()) return false;

        for (ClientTooltipComponent comp : components) {
            if (comp instanceof StackBuffer) {
                return true;
            }
            if (comp != null && comp.getClass().getName().contains("ClientGroupTooltip")) {
                return true;
            }
        }
        return false;
    }

    private static List<ClientTooltipComponent> processComponents(List<ClientTooltipComponent> components, Font textRenderer) {
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

    private static ClientTooltipComponent processHeader(ClientTooltipComponent headerComp, int availableTextWidth, Font textRenderer) {
        if (headerComp instanceof HeaderComponent header) {
            ClientTooltipComponent titleComp = header.title();

            List<ClientTooltipComponent> processedTitleList = TitleOverflowStrategyFactory.getStrategy().processComponentPhase(Collections.singletonList(titleComp), textRenderer, availableTextWidth);
            ClientTooltipComponent processedTitle = processedTitleList.isEmpty() ? titleComp : processedTitleList.get(0);

            return new StsObscureHeaderComponent(header, processedTitle);
        }
        return headerComp;
    }

    private static List<ClientTooltipComponent> processStandardComponents(List<ClientTooltipComponent> components, Font textRenderer) {
        int scaledWidth = TooltipBounds.current().maxWidth();
        int scaledHeight = TooltipBounds.current().maxHeight();

        int maxAllowedWidth = Math.max(scaledWidth - (getContentMargin() * 2), MIN_WIDTH);
        int maxAllowedHeight = Math.max(scaledHeight - (getContentMargin() * 2), MIN_HEIGHT);
        int availableTextWidth = Math.max(1, maxAllowedWidth - OT_ITEM_MODEL_OFFSET);

        ClientTooltipComponent processedHeader = processHeader(components.get(0), availableTextWidth, textRenderer);

        if (components.size() <= 1) {
            TooltipScroll.INSTANCE.track(Collections.emptyList(), textRenderer, 0);
            return Collections.singletonList(processedHeader);
        }

        List<ClientTooltipComponent> bodyRaw = new ArrayList<>(components.subList(1, components.size()));

        List<ClientTooltipComponent> result = TooltipLayout.withScrollableBody(Collections.singletonList(processedHeader), bodyRaw, maxAllowedWidth, maxAllowedHeight, processedHeader.getHeight(), textRenderer);

        int totalTooltipWidth = TooltipLayout.width(result, textRenderer);
        if (processedHeader instanceof StsObscureHeaderComponent stsHeader) {
            stsHeader.setTotalWidth(totalTooltipWidth);
        }

        return result;
    }

    private static List<ClientTooltipComponent> processSplitComponent(SplitComponent splitComponent, Font textRenderer) {
        ClientTooltipComponent left = splitComponent.left();
        List<ClientTooltipComponent> right = splitComponent.right();

        if (right == null || right.isEmpty()) {
            TooltipScroll.INSTANCE.track(Collections.emptyList(), textRenderer, 0);
            return Collections.singletonList(splitComponent);
        }

        int leftWidth = left.getWidth(textRenderer);
        int scaledWidth = TooltipBounds.current().maxWidth();
        int scaledHeight = TooltipBounds.current().maxHeight();

        int maxAllowedWidth = Math.max(scaledWidth - (getContentMargin() * 2), MIN_WIDTH);
        int maxAllowedHeight = Math.max(scaledHeight - (getContentMargin() * 2), MIN_HEIGHT);

        int rightAllowedWidth = Math.max(maxAllowedWidth - leftWidth - SPLIT_GAP, MIN_WIDTH);
        int availableTextWidth = Math.max(1, rightAllowedWidth - OT_ITEM_MODEL_OFFSET);

        ClientTooltipComponent processedHeader = processHeader(right.get(0), availableTextWidth, textRenderer);
        List<ClientTooltipComponent> bodyRaw = right.size() > 1 ? new ArrayList<>(right.subList(1, right.size())) : Collections.emptyList();
        List<ClientTooltipComponent> newRight;

        if (bodyRaw.isEmpty()) {
            TooltipScroll.INSTANCE.track(Collections.emptyList(), textRenderer, 0);
            newRight = Collections.singletonList(processedHeader);
        } else {
            newRight = TooltipLayout.withScrollableBody(Collections.singletonList(processedHeader), bodyRaw, rightAllowedWidth, maxAllowedHeight, processedHeader.getHeight(), textRenderer);
            int totalRightWidth = TooltipLayout.width(newRight, textRenderer);

            if (processedHeader instanceof StsObscureHeaderComponent stsHeader) {
                stsHeader.setTotalWidth(totalRightWidth);
            }
        }

        return Collections.singletonList(new SplitComponent(left, newRight));
    }
}
