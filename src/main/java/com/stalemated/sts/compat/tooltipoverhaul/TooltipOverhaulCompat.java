package com.stalemated.sts.compat.tooltipoverhaul;

import com.stalemated.lib.util.state.SharedTooltipState;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.resize.TooltipLayout;
import com.stalemated.sts.resize.components.WrappedTitleTooltipComponent;
import com.stalemated.sts.scroll.resetter.ExternalScrollStateResetter;
import com.stalemated.sts.state.TooltipContextManager;
import com.stalemated.sts.mixin.client.compat.tooltipoverhaul.TooltipContextAccessor;
import com.stalemated.sts.state.TooltipContext;
import com.stalemated.sts.state.TooltipDraw;
import dev.xylonity.tooltipoverhaul.client.util.TooltipScrollState;
import java.util.List;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

public class TooltipOverhaulCompat implements ExternalScrollStateResetter {

    @Override
    public void resetState() {
        if (!SmartTooltipScrollConfigRegistry.CONFIG.get().tooltip_overhaul_compat) return;
        try {
            TooltipScrollState.reset();
        } catch (Exception ignored) {}
    }

    public static void applyDimensions(Object contextObj) {
        TooltipOverhaulStateManager.setStsActive(false);

        if (!SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions || !SmartTooltipScrollConfigRegistry.CONFIG.get().tooltip_overhaul_compat) return;
        if (!(contextObj instanceof dev.xylonity.tooltipoverhaul.client.render.TooltipContext context)) return;

        List<ClientTooltipComponent> componentsListRaw = context.getComponents();
        if (componentsListRaw == null || componentsListRaw.isEmpty()) return;

        ItemStack stack = context.getStack();
        TooltipContext stsCtx = TooltipContextManager.peek();

        boolean isItem = stsCtx != null || !stack.isEmpty();
        boolean isForced = SharedTooltipState.isCustomDimensionsRequested();

        if (isItem || isForced) {
            TooltipOverhaulStateManager.setContext(context);
            TooltipDraw.begin(context.getGraphics(), context.getFont());

            if (stsCtx == null) {
                stsCtx = new TooltipContext(stack);
            }

            List<ClientTooltipComponent> processed = TooltipLayout.apply(componentsListRaw, stsCtx, pinned -> {
                if (pinned.isEmpty()) return 0;

                if (pinned.get(0) instanceof WrappedTitleTooltipComponent wrapped) {
                    TooltipOverhaulLayoutMetrics metrics = TooltipOverhaulLayoutMetrics.calculate(context, wrapped);
                    return metrics.getTargetDividerY();
                }
                return TooltipLayout.height(pinned);
            });
            
            ((TooltipContextAccessor) context).sts$setComponents(processed);

            TooltipOverhaulStateManager.setStsActive(true);
        }
    }
}
