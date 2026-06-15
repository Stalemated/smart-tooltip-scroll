package com.stalemated.sts.fabric;

import net.fabricmc.api.ClientModInitializer;

import com.stalemated.sts.SmartTooltipScrollClient;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public final class SmartTooltipScrollFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SmartTooltipScrollClient.init();

        ItemTooltipCallback.EVENT.register((stack, context, lines) -> SmartTooltipScrollClient.onItemTooltip(stack));
    }
}
