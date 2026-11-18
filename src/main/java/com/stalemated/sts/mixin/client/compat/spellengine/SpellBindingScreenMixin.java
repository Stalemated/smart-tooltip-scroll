package com.stalemated.sts.mixin.client.compat.spellengine;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.sts.state.TooltipDraw;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.spell_engine.spellbinding.SpellBindingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@RequiresMod("spell_engine")
@Pseudo
@Mixin(SpellBindingScreen.class)
public abstract class SpellBindingScreenMixin {

    @WrapOperation(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderComponentTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V"))
    private void smart_tooltip_scroll$drawWithCustomDimensions(GuiGraphics graphics, Font font, List<Component> list, int i, int j, Operation<Void> original) {
        TooltipDraw.withCustomDimensions(() -> original.call(graphics, font, list, i, j));
    }
}
