package com.stalemated.sts.mixin.client.compat.puffish;

import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.sts.compat.puffish.PuffishSkillsCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

@RequiresMod("puffish_skills")
@Pseudo
@Mixin(targets = "net.puffish.skillsmod.client.gui.SkillsScreen")
public abstract class SkillsScreenMixin {

    @Inject(method = "drawContentWithCategory", at = @At("HEAD"))
    private void sts$onDrawContentStart(CallbackInfo ci) {
        PuffishSkillsCompat.isSkillTooltipContext = true;
    }

    @Inject(method = "drawContentWithCategory", at = @At("RETURN"))
    private void sts$onDrawContentEnd(CallbackInfo ci) {
        PuffishSkillsCompat.isSkillTooltipContext = false;
    }

    @Redirect(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Tooltip;splitTooltip(Lnet/minecraft/client/Minecraft;Lnet/minecraft/network/chat/Component;)Ljava/util/List;"), require = 0)
    private List<FormattedCharSequence> sts$redirectWrapLines(Minecraft client, Component text) {
        return PuffishSkillsCompat.wrapLines(client, text);
    }
}
