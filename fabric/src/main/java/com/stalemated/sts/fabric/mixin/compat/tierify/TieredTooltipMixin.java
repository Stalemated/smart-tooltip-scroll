package com.stalemated.sts.fabric.mixin.compat.tierify;


import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.fabric.compat.TierifyLegendaryBridge;
import com.stalemated.sts.resize.TitleOverflowMode;
import com.stalemated.sts.resize.TooltipDimensionManager;
import com.stalemated.sts.resize.components.ScrollingTitleTooltipComponent;
import draylar.tiered.api.BorderTemplate;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Pseudo
@Mixin(targets = "elocindev.tierify.util.TieredTooltip")
public abstract class TieredTooltipMixin {

    @Inject(method = "renderTieredTooltipFromComponents", at = @At("HEAD"))
    private static void customtooltips$captureContext(DrawContext context, TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, BorderTemplate borderTemplate, CallbackInfo ci) {
        TooltipDimensionManager.setState(context, textRenderer);
        TooltipDimensionManager.isCurrentTooltipItemTooltip = true;
    }

    @ModifyVariable(method = "renderTieredTooltipFromComponents", at = @At("HEAD"), index = 2, argsOnly = true)
    private static List<TooltipComponent> customtooltips$applyDimensions(List<TooltipComponent> components) {
        List<TooltipComponent> processedList = components;
        boolean hasLT = FabricLoader.getInstance().isModLoaded("legendarytooltips");

        if (hasLT) {
            TooltipDimensionManager.pinnedHeightPredictor = TierifyLegendaryBridge::getPredictedPinnedHeight;
        }

        if (ConfigManager.getConfig().custom_tooltip_dimensions) {
            if (ConfigManager.getConfig().title_overflow_mode == TitleOverflowMode.SCROLL) ScrollingTitleTooltipComponent.isTierifyTooltip = true;
            processedList = TooltipDimensionManager.enforceHeightLimit(components);
        }

        if (hasLT) {
            TooltipDimensionManager.pinnedHeightPredictor = null;
            processedList = TierifyLegendaryBridge.wrapComponents(processedList);
        }

        return processedList;
    }

    @Inject(method = "renderTooltipBackground", at = @At("HEAD"))
    private static void customtooltips$overrideTierifyBackground(DrawContext context, int x, int y, int width, int height, int z, int backgroundColor, int colorStart, int colorEnd, CallbackInfo ci) {
        if (FabricLoader.getInstance().isModLoaded("legendarytooltips")) {
            TierifyLegendaryBridge.setTooltipPosition(x, y, width);
        }
    }

    @Inject(method = "renderTieredTooltipFromComponents", at = @At("TAIL"))
    private static void customtooltips$clearContext(DrawContext context, TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, BorderTemplate borderTemplate, CallbackInfo ci) {
        if (FabricLoader.getInstance().isModLoaded("legendarytooltips")) {
            TierifyLegendaryBridge.drawSeparator(context, components);
        }
        if (ConfigManager.getConfig().title_overflow_mode == TitleOverflowMode.SCROLL) ScrollingTitleTooltipComponent.isTierifyTooltip = false;

        TooltipDimensionManager.clearState();
    }
}
