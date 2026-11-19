package com.stalemated.sts.mixin.client;

import com.stalemated.sts.resize.VanillaTooltip;
import com.stalemated.sts.state.TooltipDraw;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiGraphics.class)
public abstract class DrawContextMixin {

    @Inject(method = "renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V", at = @At("HEAD"))
    private void smart_tooltip_scroll$beginDraw(Font font, List<ClientTooltipComponent> list, int i, int j, ClientTooltipPositioner clientTooltipPositioner, CallbackInfo ci) {
        VanillaTooltip.beginDraw((GuiGraphics) (Object) this, font);
    }

    @ModifyVariable(method = "renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V", at = @At("HEAD"), argsOnly = true)
    private List<ClientTooltipComponent> smart_tooltip_scroll$layOut(List<ClientTooltipComponent> list) {
        return VanillaTooltip.layOut(list);
    }

    @Inject(method = "renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V", at = @At("TAIL"))
    private void smart_tooltip_scroll$endDraw(CallbackInfo ci) {
        TooltipDraw.end();
    }
}
