package com.stalemated.sts.forge;

//? if forge {
/*import com.stalemated.lib.util.state.SharedTooltipState;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.state.TooltipContextManager;
import net.minecraft.world.item.ItemStack;

public final class ForgeTooltipWrap {
    private static boolean keepsLinesWhole;

    private ForgeTooltipWrap() {
    }

    public static void beginGather(ItemStack stack) {
        if (!SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions) {
            keepsLinesWhole = false;
            return;
        }

        boolean isItem = (stack != null && !stack.isEmpty()) || TooltipContextManager.peek() != null;
        keepsLinesWhole = isItem || SharedTooltipState.isCustomDimensionsRequested();
    }

    public static void endGather() {
        keepsLinesWhole = false;
    }

    public static int lineWidth(int width) {
        return keepsLinesWhole ? Integer.MAX_VALUE : width;
    }
}
*///?}
