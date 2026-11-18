package com.stalemated.sts.mixin.client.compat.spellengine;

import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.lib.util.state.SharedTooltipState;
import net.spell_engine.spellbinding.SpellBindingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@RequiresMod("spell_engine")
@Pseudo
@Mixin(SpellBindingScreen.class)
public abstract class SpellBindingScreenMixin {

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderComponentTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V"))
    private void sts$forceCustomDimensionsForSpells(CallbackInfo ci) {
        SharedTooltipState.requestCustomDimensions();
    }
}
