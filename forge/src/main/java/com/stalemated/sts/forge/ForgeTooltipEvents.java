package com.stalemated.sts.forge;


import com.stalemated.sts.SmartTooltipScrollClient;
import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.resize.TooltipDimensionManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = SmartTooltipScrollClient.MOD_ID)
public class ForgeTooltipEvents {

    @SubscribeEvent
    public static void onGatherComponents(RenderTooltipEvent.GatherComponents event) {
        if (ConfigManager.getConfig().custom_tooltip_dimensions) {
            event.setMaxWidth(-1);
        }
    }
}