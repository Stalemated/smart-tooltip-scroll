package com.stalemated.sts.resize;

import com.stalemated.lib.util.state.SharedTooltipState;
import com.stalemated.sts.compat.LoadedMods;
//? if obscure_tooltips
import com.stalemated.sts.compat.obscure.ObscureTooltipsCompat;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.state.TooltipContext;
import com.stalemated.sts.state.TooltipContexts;
import com.stalemated.sts.state.TooltipDraw;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

import java.util.List;

public final class VanillaTooltip {
    private VanillaTooltip() {
    }

    public static void beginDraw(GuiGraphics graphics, Font font) {
        if (customDimensions()) TooltipDraw.begin(graphics, font);
    }

    public static List<ClientTooltipComponent> layOut(List<ClientTooltipComponent> components) {
        if (!customDimensions()) return components;
        //? if obscure_tooltips
        if (LoadedMods.OBSCURE_TOOLTIPS && ObscureTooltipsCompat.isObscureHandling(components)) return components;

        TooltipContext context = TooltipContexts.peek();
        if (context == null && !SharedTooltipState.isCustomDimensionsRequested()) return components;
        return TooltipLayout.apply(components, context);
    }

    private static boolean customDimensions() {
        return SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions;
    }
}
