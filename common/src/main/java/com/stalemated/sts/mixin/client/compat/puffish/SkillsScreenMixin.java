package com.stalemated.sts.mixin.client.compat.puffish;

import com.stalemated.sts.compat.puffish.PuffishSkillsCompat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Pseudo
@Mixin(targets = "net.puffish.skillsmod.client.gui.SkillsScreen")
public abstract class SkillsScreenMixin {

    @Redirect(method = "lambda$drawContentWithCategory$21", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/tooltip/Tooltip;wrapLines(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/text/Text;)Ljava/util/List;"))
    private List<OrderedText> sts$redirectWrapLines(MinecraftClient client, Text text) {
        return PuffishSkillsCompat.wrapLines(client, text);
    }
}
