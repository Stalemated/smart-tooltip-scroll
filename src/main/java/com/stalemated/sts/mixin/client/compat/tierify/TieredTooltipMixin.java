package com.stalemated.sts.mixin.client.compat.tierify;

import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.sts.compat.tierify.TierifyLegendaryBridge;
import com.stalemated.sts.compat.tierify.TierifyTOBridge;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.resize.TooltipDimensionManager;
import com.stalemated.sts.state.StateManager;
import com.stalemated.sts.state.TooltipContextManager;
import com.stalemated.sts.state.TooltipDraw;
import draylar.tiered.api.BorderTemplate;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import elocindev.tierify.config.ClientConfig;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;

import static com.stalemated.sts.state.StateManager.IS_LT_LOADED;
import static com.stalemated.sts.state.StateManager.IS_TO_LOADED;

@RequiresMod("tiered")
@Pseudo
@Mixin(targets = "elocindev.tierify.util.TieredTooltip")
public abstract class TieredTooltipMixin {

    @Inject(method = "renderTieredTooltipFromComponents", at = @At("HEAD"))
    private static void sts$captureContext(GuiGraphics context, Font textRenderer, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, BorderTemplate borderTemplate, CallbackInfo ci) {
        TooltipDraw.begin(context, textRenderer);
        StateManager.isTierifyTooltip = true;
        if (IS_TO_LOADED) {
            TierifyTOBridge.captureState(context, textRenderer, x, y, positioner);
        }
    }

    @ModifyVariable(method = "renderTieredTooltipFromComponents", at = @At("HEAD"), index = 2, argsOnly = true)
    private static List<ClientTooltipComponent> sts$applyDimensions(List<ClientTooltipComponent> components) {
        List<ClientTooltipComponent> processedList = components;

        if (IS_LT_LOADED) {
            TooltipDimensionManager.pinnedHeightPredictor = TierifyLegendaryBridge::getPredictedPinnedHeight;
        }

        if (SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions) {
            if (!IS_TO_LOADED || IS_LT_LOADED) {
                processedList = TooltipDimensionManager.enforceHeightLimit(components, TooltipContextManager.peek());
            }
        }

        if (IS_LT_LOADED) {
            TooltipDimensionManager.pinnedHeightPredictor = null;
            processedList = TierifyLegendaryBridge.wrapComponents(processedList);
        } else if (IS_TO_LOADED) {
            processedList = TierifyTOBridge.wrapComponents(processedList);
        }

        return processedList;
    }

    @Redirect(method = "renderTieredTooltipFromComponents", at = @At(value = "FIELD", target = "Lelocindev/tierify/config/ClientConfig;centerName:Z", opcode = Opcodes.GETFIELD))
    private static boolean sts$redirectCenterName(ClientConfig config) {
        if (SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions && SmartTooltipScrollConfigRegistry.CONFIG.get().title_centering) return false;
        return config.centerName;
    }

    //? if fabric {
    @Inject(method = "renderTooltipBackground", at = @At("HEAD"), order = 900)
    //?} else {
    /*@Inject(method = "renderTooltipBackground", at = @At("HEAD"))
    *///?}
    private static void sts$getTooltipPosition(GuiGraphics context, int x, int y, int width, int height, int z, int backgroundColor, int colorStart, int colorEnd, CallbackInfo ci) {
        if (IS_LT_LOADED) TierifyLegendaryBridge.setTooltipPosition(x, y, width);
    }

    //? if fabric {
    @Inject(method = "renderTieredTooltipFromComponents", at = @At("TAIL"), order = 900)
    //?} else {
    /*@Inject(method = "renderTieredTooltipFromComponents", at = @At("TAIL"))
    *///?}
    private static void sts$clearContext(GuiGraphics context, Font textRenderer, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, BorderTemplate borderTemplate, CallbackInfo ci) {
        if (IS_LT_LOADED) TierifyLegendaryBridge.drawSeparator(context, components);

        StateManager.isTierifyTooltip = false;
        TooltipDraw.end();

        if (IS_TO_LOADED) TierifyTOBridge.clearState();
    }
}
