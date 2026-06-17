package com.stalemated.sts.fabric.mixin.compat.tierify;


import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.fabric.compat.TierifyLegendaryBridge;
import com.stalemated.sts.resize.TooltipDimensionManager;
import com.stalemated.sts.state.StateManager;
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
    private static void rst$captureContext(DrawContext context, TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, BorderTemplate borderTemplate, CallbackInfo ci) {
        TooltipDimensionManager.setState(context, textRenderer, components);
        TooltipDimensionManager.isCurrentTooltipItemTooltip = true;
    }

    @ModifyVariable(method = "renderTieredTooltipFromComponents", at = @At("HEAD"), index = 2, argsOnly = true)
    private static List<TooltipComponent> rst$applyDimensions(List<TooltipComponent> components) {
        List<TooltipComponent> processedList = components;
        boolean hasLT = FabricLoader.getInstance().isModLoaded("legendarytooltips");

        if (hasLT) {
            TooltipDimensionManager.pinnedHeightPredictor = TierifyLegendaryBridge::getPredictedPinnedHeight;
        }

        if (ConfigManager.getConfig().custom_tooltip_dimensions) {
            StateManager.isTierifyTooltip = true;
            processedList = TooltipDimensionManager.enforceHeightLimit(components);
        }

        if (hasLT) {
            TooltipDimensionManager.pinnedHeightPredictor = null;
            processedList = TierifyLegendaryBridge.wrapComponents(processedList);
        }

        return processedList;
    }

    @Inject(method = "renderTooltipBackground", at = @At("HEAD"), order = 900)
    private static void rst$getTooltipPosition(DrawContext context, int x, int y, int width, int height, int z, int backgroundColor, int colorStart, int colorEnd, CallbackInfo ci) {
        if (FabricLoader.getInstance().isModLoaded("legendarytooltips")) {
            TierifyLegendaryBridge.setTooltipPosition(x, y, width);
        }
    }

    @Inject(method = "renderTieredTooltipFromComponents", at = @At("TAIL"), order = 900)
    private static void rst$clearContext(DrawContext context, TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, BorderTemplate borderTemplate, CallbackInfo ci) {
        if (FabricLoader.getInstance().isModLoaded("legendarytooltips")) {
            TierifyLegendaryBridge.drawSeparator(context, components);
        }
        StateManager.isTierifyTooltip = false;

        TooltipDimensionManager.clearState();
    }
}
