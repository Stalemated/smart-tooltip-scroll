package com.stalemated.sts.compat.tierify;

import com.stalemated.sts.compat.tierify.component.TOTieredWrapper;
import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.resize.TooltipDimensionManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TierifyTOBridge {

    public static DrawContext currentContext;
    public static TextRenderer currentTextRenderer;
    public static int currentX;
    public static int currentY;
    public static TooltipPositioner currentPositioner;

    public static void captureState(DrawContext context, TextRenderer textRenderer, int x, int y, TooltipPositioner positioner) {
        currentContext = context;
        currentTextRenderer = textRenderer;
        currentX = x;
        currentY = y;
        currentPositioner = positioner;
    }

    public static List<TooltipComponent> wrapComponents(List<TooltipComponent> components) {
        if (components == null || components.isEmpty()) return components;
        if (!ConfigManager.getConfig().custom_tooltip_dimensions) return components;

        ItemStack currentStack = TooltipDimensionManager.getCurrentStack();
        if (currentStack == null || currentStack.isEmpty()) return components;

        TOTieredWrapper wrapper = new TOTieredWrapper(new ArrayList<>(components), currentContext, currentTextRenderer, currentX, currentY, currentPositioner, currentStack);

        List<TooltipComponent> newList = new ArrayList<>();
        newList.add(wrapper);
        return newList;
    }

    public static void clearState() {
        currentContext = null;
        currentTextRenderer = null;
        currentPositioner = null;
    }
}
