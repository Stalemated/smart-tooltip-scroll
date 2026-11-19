package com.stalemated.sts.mixin.client.compat.tierify;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.sts.compat.tierify.TierifyCompat;
import draylar.tiered.api.BorderTemplate;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;

@RequiresMod("tiered")
@Pseudo
@Mixin(targets = "elocindev.tierify.util.TieredTooltip")
public abstract class TieredTooltipMixin {

    @Inject(method = "renderTieredTooltipFromComponents", at = @At("HEAD"))
    private static void smart_tooltip_scroll$beginDraw(GuiGraphics context, Font textRenderer, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, BorderTemplate borderTemplate, CallbackInfo ci) {
        TierifyCompat.beginDraw(context, textRenderer, x, y, positioner);
    }

    @ModifyVariable(method = "renderTieredTooltipFromComponents", at = @At("HEAD"), argsOnly = true)
    private static List<ClientTooltipComponent> smart_tooltip_scroll$layOut(List<ClientTooltipComponent> components) {
        return TierifyCompat.layOut(components);
    }

    @ModifyExpressionValue(method = "renderTieredTooltipFromComponents", at = @At(value = "FIELD", target = "Lelocindev/tierify/config/ClientConfig;centerName:Z", opcode = Opcodes.GETFIELD))
    private static boolean smart_tooltip_scroll$centerName(boolean tierifyCentersName) {
        return TierifyCompat.centersName(tierifyCentersName);
    }

    //? if fabric {
    @Inject(method = "renderTooltipBackground", at = @At("HEAD"), order = 900)
    //?} else {
    /*@Inject(method = "renderTooltipBackground", at = @At("HEAD"))
    *///?}
    private static void smart_tooltip_scroll$recordBackground(GuiGraphics context, int x, int y, int width, int height, int z, int backgroundColor, int colorStart, int colorEnd, CallbackInfo ci) {
        TierifyCompat.recordBackground(x, y, width);
    }

    //? if fabric {
    @Inject(method = "renderTieredTooltipFromComponents", at = @At("TAIL"), order = 900)
    //?} else {
    /*@Inject(method = "renderTieredTooltipFromComponents", at = @At("TAIL"))
    *///?}
    private static void smart_tooltip_scroll$endDraw(GuiGraphics context, Font textRenderer, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, BorderTemplate borderTemplate, CallbackInfo ci) {
        TierifyCompat.endDraw(context, components);
    }
}
