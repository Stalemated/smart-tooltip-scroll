package com.stalemated.sts.neoforge;

import com.stalemated.sts.SmartTooltipScrollClient;
import com.stalemated.sts.gui.screen.TooltipDimensionsScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;


@Mod(SmartTooltipScrollClient.MOD_ID)
public class SmartTooltipScrollNeoForge {
    public SmartTooltipScrollNeoForge(ModContainer modContainer) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            NeoForge.EVENT_BUS.addListener(this::onItemTooltip);

            initClient(modContainer);
        }
    }
    
    private void initClient(ModContainer modContainer) {
        SmartTooltipScrollClient.init();

        modContainer.registerExtensionPoint(IConfigScreenFactory.class,
                (client, parent) -> TooltipDimensionsScreen.create(parent));
    }

    private void onItemTooltip(ItemTooltipEvent event) {
        SmartTooltipScrollClient.onItemTooltip(event.getItemStack());
    }
}
