package com.stalemated.sts.state;

import com.stalemated.lib.util.state.SharedTooltipState;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

import java.util.List;

public final class TooltipDraw {
    private static GuiGraphics graphics;
    private static Font font;
    private static List<ClientTooltipComponent> titleComponents = List.of();
    private static List<ClientTooltipComponent> bodyComponents = List.of();

    private TooltipDraw() {
    }

    public static void begin(GuiGraphics drawGraphics, Font drawFont) {
        graphics = drawGraphics;
        font = drawFont;
    }

    public static void end() {
        graphics = null;
        font = null;
        titleComponents = List.of();
        bodyComponents = List.of();
        TooltipContextManager.clear();
        SharedTooltipState.clearCustomDimensionsRequest();
    }

    public static void withCustomDimensions(Runnable draw) {
        if (SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions) SharedTooltipState.withCustomDimensions(draw);
        else draw.run();
    }

    public static GuiGraphics graphics() {
        return graphics;
    }

    public static Font font() {
        return font;
    }

    public static List<ClientTooltipComponent> titleComponents() {
        return titleComponents;
    }

    public static void setTitleComponents(List<ClientTooltipComponent> components) {
        titleComponents = components;
    }

    public static List<ClientTooltipComponent> bodyComponents() {
        return bodyComponents;
    }

    public static void setBodyComponents(List<ClientTooltipComponent> components) {
        bodyComponents = components;
    }
}
