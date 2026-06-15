package com.stalemated.rst.forge;

import com.stalemated.rst.ResizedScrollableTooltipsClient;
import com.stalemated.rst.gui.screen.TooltipDimensionsScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(ResizedScrollableTooltipsClient.MOD_ID)
@SuppressWarnings("removal")
public class ResizedScrollableTooltipsForge {
    public ResizedScrollableTooltipsForge() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MinecraftForge.EVENT_BUS.addListener(this::onItemTooltip);

            initClient();
        }
    }
    
    private void initClient() {
        ResizedScrollableTooltipsClient.init();

        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> TooltipDimensionsScreen.create(parent)));
    }

    private void onItemTooltip(ItemTooltipEvent event) {
        ResizedScrollableTooltipsClient.onItemTooltip(event.getItemStack());
    }
}
