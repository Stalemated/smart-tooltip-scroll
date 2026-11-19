package com.stalemated.sts.mixin.client.compat.tooltipoverhaul;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulCompat;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulLayoutFixer;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulDraw;
import dev.xylonity.tooltipoverhaul.client.render.TooltipContext;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@RequiresMod("tooltipoverhaul")
@Pseudo
@Mixin(targets = "dev.xylonity.tooltipoverhaul.client.render.TooltipRenderer")
public abstract class TooltipRendererMixin {

    @Final
    @Shadow
    private TooltipContext context;

    @Inject(method = "init", at = @At("HEAD"))
    private void smart_tooltip_scroll$applyDimensions(CallbackInfo ci) {
        TooltipOverhaulCompat.applyDimensions(this.context);
    }

    @ModifyExpressionValue(method = "init", at = @At(value = "INVOKE", target = "Ldev/xylonity/tooltipoverhaul/client/layout/TooltipSizeCalculator;calculate()Lnet/minecraft/world/phys/Vec2;"))
    private Vec2 smart_tooltip_scroll$fixBoxSize(Vec2 size) {
        return TooltipOverhaulLayoutFixer.fixBoxSize(size, this.context);
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void smart_tooltip_scroll$beginRender(CallbackInfoReturnable<Boolean> cir) {
        TooltipOverhaulDraw.ensureContext(this.context);
    }

    @Inject(method = "renderOut", at = @At("HEAD"))
    private void smart_tooltip_scroll$beginRenderOut(float progress, CallbackInfo ci) {
        TooltipOverhaulDraw.ensureContext(this.context);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void smart_tooltip_scroll$endRender(CallbackInfoReturnable<Boolean> cir) {
        TooltipOverhaulDraw.clearContext();
    }

    @Inject(method = "renderOut", at = @At("TAIL"))
    private void smart_tooltip_scroll$endRenderOut(float progress, CallbackInfo ci) {
        TooltipOverhaulDraw.clearContext();
    }
}
