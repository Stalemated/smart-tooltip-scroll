package com.stalemated.sts.compat.tierify;

import com.stalemated.sts.compat.LoadedMods;
import com.stalemated.sts.config.SmartTooltipScrollConfig;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.resize.TooltipLayout;
import com.stalemated.sts.state.TooltipContexts;
import com.stalemated.sts.state.TooltipDraw;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;

import java.util.List;

public final class TierifyCompat {
    private static boolean drawing;

    private TierifyCompat() {
    }

    public static boolean isDrawing() {
        return drawing;
    }

    public static void beginDraw(GuiGraphics graphics, Font font, int x, int y, ClientTooltipPositioner positioner) {
        TooltipDraw.begin(graphics, font);
        drawing = true;
        if (LoadedMods.TOOLTIP_OVERHAUL) TierifyTOBridge.captureState(graphics, font, x, y, positioner);
    }

    public static List<ClientTooltipComponent> layOut(List<ClientTooltipComponent> components) {
        List<ClientTooltipComponent> laidOut = components;

        if (SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions) {
            if (LoadedMods.LEGENDARY_TOOLTIPS) laidOut = TooltipLayout.apply(components, TooltipContexts.peek(), TierifyLegendaryBridge::getPredictedPinnedHeight);
            else if (!LoadedMods.TOOLTIP_OVERHAUL) laidOut = TooltipLayout.apply(components, TooltipContexts.peek());
        }

        if (LoadedMods.LEGENDARY_TOOLTIPS) return TierifyLegendaryBridge.wrapComponents(laidOut);
        if (LoadedMods.TOOLTIP_OVERHAUL) return TierifyTOBridge.wrapComponents(laidOut);
        return laidOut;
    }

    public static boolean centersName(boolean tierifyCentersName) {
        SmartTooltipScrollConfig config = SmartTooltipScrollConfigRegistry.CONFIG.get();
        if (config.custom_tooltip_dimensions && config.title_centering) return false;
        return tierifyCentersName;
    }

    public static void recordBackground(int x, int y, int width) {
        if (LoadedMods.LEGENDARY_TOOLTIPS) TierifyLegendaryBridge.setTooltipPosition(x, y, width);
    }

    public static void endDraw(GuiGraphics graphics, List<ClientTooltipComponent> components) {
        if (LoadedMods.LEGENDARY_TOOLTIPS) TierifyLegendaryBridge.drawSeparator(graphics, components);

        drawing = false;
        TooltipDraw.end();

        if (LoadedMods.TOOLTIP_OVERHAUL) TierifyTOBridge.clearState();
    }
}
