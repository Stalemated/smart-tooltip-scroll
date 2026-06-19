package com.stalemated.sts.neoforge;

import com.stalemated.sts.SmartTooltipScrollClient;
import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.resize.TooltipDimensionManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;


@EventBusSubscriber(value = Dist.CLIENT, modid = SmartTooltipScrollClient.MOD_ID)
public class NeoForgeTooltipEvents {

    @SubscribeEvent
    public static void onGatherComponents(RenderTooltipEvent.GatherComponents event) {
        if (ConfigManager.getConfig().custom_tooltip_dimensions && TooltipDimensionManager.isCurrentTooltipItemTooltip) {
            event.setMaxWidth(-1);

            if (!ModList.get().isLoaded("iceberg")) {
                if (!event.getTooltipElements().isEmpty()) {
                    event.getTooltipElements().getFirst().ifLeft(visitable ->
                            TooltipDimensionManager.expectedTitleString = visitable.getString().replace(" ", "")
                    );
                }
            }
        }
    }
}