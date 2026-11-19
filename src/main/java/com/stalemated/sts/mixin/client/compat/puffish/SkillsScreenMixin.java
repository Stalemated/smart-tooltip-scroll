package com.stalemated.sts.mixin.client.compat.puffish;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.sts.compat.puffish.PuffishSkillsCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
    private void smart_tooltip_scroll$beginSkillTree(CallbackInfo ci) {
        PuffishSkillsCompat.beginSkillTree();
    }

    @Inject(method = "drawContentWithCategory", at = @At("RETURN"))
    private void smart_tooltip_scroll$endSkillTree(CallbackInfo ci) {
        PuffishSkillsCompat.endSkillTree();
    }

    @WrapOperation(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Tooltip;splitTooltip(Lnet/minecraft/client/Minecraft;Lnet/minecraft/network/chat/Component;)Ljava/util/List;"))
    private List<FormattedCharSequence> smart_tooltip_scroll$splitToCustomWidth(Minecraft minecraft, Component component, Operation<List<FormattedCharSequence>> original) {
        if (!PuffishSkillsCompat.wrapsSkillTooltips()) return original.call(minecraft, component);
        return PuffishSkillsCompat.splitSkillTooltip(minecraft, component);
    }
}
