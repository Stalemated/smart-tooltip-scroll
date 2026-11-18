package com.stalemated.sts;

import com.stalemated.lib.client.SLibConfigScreens;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.gui.screen.TooltipDimensionsScreen;
import com.stalemated.sts.keybind.StsKeybinds;
import com.stalemated.sts.scroll.resetter.ScrollResetterManager;
import com.stalemated.sts.state.TooltipContextManager;
import net.minecraft.world.item.ItemStack;

//? if fabric {
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
//?} elif forge {
/*import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
*///?}

public final class SmartTooltipScrollClient //? if fabric
implements ClientModInitializer
{
    //? if fabric {
    @Override
    public void onInitializeClient() {
        init();
    }
    //?} else {
    /*private SmartTooltipScrollClient() {
    }
    *///?}

    static void init() {
        SmartTooltipScrollConfigRegistry.register();
        ScrollResetterManager.register();
        StsKeybinds.register();
        SLibConfigScreens.register(SmartTooltipScroll.MOD_ID, () -> TooltipDimensionsScreen::create);
        registerTooltipEvent();
    }

    //? if fabric {
    private static void registerTooltipEvent() {
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> onItemTooltip(stack));
    }
    //?} elif forge {
    /*private static void registerTooltipEvent() {
        MinecraftForge.EVENT_BUS.addListener((ItemTooltipEvent event) -> onItemTooltip(event.getItemStack()));
    }
    *///?}

    private static void onItemTooltip(ItemStack stack) {
        if (stack.isEmpty()) return;
        TooltipContextManager.push(stack);
    }
}
