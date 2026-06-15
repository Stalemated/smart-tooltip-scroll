package com.stalemated.rst.forge;


import com.stalemated.rst.ResizedScrollableTooltipsClient;
import com.stalemated.rst.config.ConfigManager;
import com.stalemated.rst.resize.TooltipDimensionManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ResizedScrollableTooltipsClient.MOD_ID)
public class ForgeTooltipEvents {

    @SubscribeEvent
    public static void onGatherComponents(RenderTooltipEvent.GatherComponents event) {
        if (ConfigManager.getConfig().custom_tooltip_dimensions && TooltipDimensionManager.isCurrentTooltipItemTooltip) {
            event.setMaxWidth(-1);

            if (!ModList.get().isLoaded("iceberg")) {
                if (!event.getTooltipElements().isEmpty()) {
                    event.getTooltipElements().get(0).ifLeft(visitable ->
                            TooltipDimensionManager.expectedTitleString = visitable.getString().replace(" ", "")
                    );
                }
            }
        }
    }
}