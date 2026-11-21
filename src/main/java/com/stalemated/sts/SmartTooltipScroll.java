package com.stalemated.sts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? if forge {
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
*///?} elif neoforge {
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
*///?}

//? if forge || neoforge {
/*@Mod(SmartTooltipScroll.MOD_ID)
*///?}
public final class SmartTooltipScroll {
    public static final String MOD_ID = "smart_tooltip_scroll";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    //? if forge {
    /*public SmartTooltipScroll() {
        if (FMLEnvironment.dist == Dist.CLIENT) SmartTooltipScrollClient.init();
    }
    *///?} elif neoforge {
    /*public SmartTooltipScroll(Dist dist) {
        if (dist == Dist.CLIENT) SmartTooltipScrollClient.init();
    }
    *///?} else {
    private SmartTooltipScroll() {
    }
    //?}
}
