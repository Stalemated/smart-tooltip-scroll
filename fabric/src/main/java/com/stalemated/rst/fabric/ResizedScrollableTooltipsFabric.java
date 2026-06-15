package com.stalemated.rst.fabric;

import net.fabricmc.api.ClientModInitializer;

import com.stalemated.rst.ResizedScrollableTooltipsClient;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public final class ResizedScrollableTooltipsFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ResizedScrollableTooltipsClient.init();

        ItemTooltipCallback.EVENT.register((stack, context, lines) -> ResizedScrollableTooltipsClient.onItemTooltip(stack));
    }
}
