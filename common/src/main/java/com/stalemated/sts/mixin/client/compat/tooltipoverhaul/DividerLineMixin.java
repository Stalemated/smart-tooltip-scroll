package com.stalemated.sts.mixin.client.compat.tooltipoverhaul;

import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulLayoutFixer;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulStateManager;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Pseudo
@Mixin(targets = {
        "dev.xylonity.tooltipoverhaul.client.style.divider.GradientDividerLine",
        "dev.xylonity.tooltipoverhaul.client.style.divider.LinearDividerLine",
        "dev.xylonity.tooltipoverhaul.client.style.divider.StaticDividerLine"
})
public abstract class DividerLineMixin {

    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true)
    private Vec2f sts$modifyDividerPosition(Vec2f position) {
        return TooltipOverhaulLayoutFixer.adjustDividerLineY(position, TooltipOverhaulStateManager.getCurrentTOContext());
    }
}
