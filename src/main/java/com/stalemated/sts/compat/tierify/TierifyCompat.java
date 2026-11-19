package com.stalemated.sts.compat.tierify;

import com.stalemated.sts.config.SmartTooltipScrollConfig;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.resize.TooltipLayout;
import com.stalemated.sts.state.StateManager;
import com.stalemated.sts.state.TooltipContextManager;
import com.stalemated.sts.state.TooltipDraw;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;

import java.util.List;

import static com.stalemated.sts.state.StateManager.IS_LT_LOADED;
import static com.stalemated.sts.state.StateManager.IS_TO_LOADED;

public final class TierifyCompat {
    private TierifyCompat() {
    }

    public static void beginDraw(GuiGraphics graphics, Font font, int x, int y, ClientTooltipPositioner positioner) {
        TooltipDraw.begin(graphics, font);
        StateManager.isTierifyTooltip = true;
        if (IS_TO_LOADED) TierifyTOBridge.captureState(graphics, font, x, y, positioner);
    }

    public static List<ClientTooltipComponent> layOut(List<ClientTooltipComponent> components) {
        List<ClientTooltipComponent> laidOut = components;

        if (SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions) {
            if (IS_LT_LOADED) laidOut = TooltipLayout.apply(components, TooltipContextManager.peek(), TierifyLegendaryBridge::getPredictedPinnedHeight);
            else if (!IS_TO_LOADED) laidOut = TooltipLayout.apply(components, TooltipContextManager.peek());
        }

        if (IS_LT_LOADED) return TierifyLegendaryBridge.wrapComponents(laidOut);
        if (IS_TO_LOADED) return TierifyTOBridge.wrapComponents(laidOut);
        return laidOut;
    }

    public static boolean centersName(boolean tierifyCentersName) {
        SmartTooltipScrollConfig config = SmartTooltipScrollConfigRegistry.CONFIG.get();
        if (config.custom_tooltip_dimensions && config.title_centering) return false;
        return tierifyCentersName;
    }

    public static void recordBackground(int x, int y, int width) {
        if (IS_LT_LOADED) TierifyLegendaryBridge.setTooltipPosition(x, y, width);
    }

    public static void endDraw(GuiGraphics graphics, List<ClientTooltipComponent> components) {
        if (IS_LT_LOADED) TierifyLegendaryBridge.drawSeparator(graphics, components);

        StateManager.isTierifyTooltip = false;
        TooltipDraw.end();

        if (IS_TO_LOADED) TierifyTOBridge.clearState();
    }
}
