package com.stalemated.sts.forge;

import com.stalemated.sts.SmartTooltipScrollClient;
import com.stalemated.sts.gui.screen.TooltipDimensionsScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(SmartTooltipScrollClient.MOD_ID)
@SuppressWarnings("removal")
public class SmartTooltipScrollForge {
    public SmartTooltipScrollForge() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MinecraftForge.EVENT_BUS.addListener(this::onItemTooltip);

            initClient();
        }
    }
    
    private void initClient() {
        SmartTooltipScrollClient.init();

        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> TooltipDimensionsScreen.create(parent)));
    }

    private void onItemTooltip(ItemTooltipEvent event) {
        SmartTooltipScrollClient.onItemTooltip(event.getItemStack());
    }
}
