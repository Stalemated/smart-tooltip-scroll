package com.stalemated.sts.compat.tierify;

import com.stalemated.sts.compat.tierify.component.TOTieredWrapper;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.resize.TooltipDimensionManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.item.ItemStack;

public class TierifyTOBridge {

    public static GuiGraphics currentContext;
    public static Font currentTextRenderer;
    public static int currentX;
    public static int currentY;
    public static ClientTooltipPositioner currentPositioner;

    public static void captureState(GuiGraphics context, Font textRenderer, int x, int y, ClientTooltipPositioner positioner) {
        currentContext = context;
        currentTextRenderer = textRenderer;
        currentX = x;
        currentY = y;
        currentPositioner = positioner;
    }

    public static List<ClientTooltipComponent> wrapComponents(List<ClientTooltipComponent> components) {
        if (components == null || components.isEmpty()) return components;
        if (!SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions) return components;

        ItemStack currentStack = TooltipDimensionManager.getCurrentStack();
        if (currentStack == null || currentStack.isEmpty()) return components;

        TOTieredWrapper wrapper = new TOTieredWrapper(new ArrayList<>(components), currentContext, currentTextRenderer, currentX, currentY, currentPositioner, currentStack);

        List<ClientTooltipComponent> newList = new ArrayList<>();
        newList.add(wrapper);
        return newList;
    }

    public static void clearState() {
        currentContext = null;
        currentTextRenderer = null;
        currentPositioner = null;
    }
}
