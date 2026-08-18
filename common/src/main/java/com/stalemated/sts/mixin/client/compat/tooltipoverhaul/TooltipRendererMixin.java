package com.stalemated.sts.mixin.client.compat.tooltipoverhaul;

import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulCompat;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulLayoutFixer;
import com.stalemated.sts.compat.tooltipoverhaul.TooltipOverhaulStateManager;
import dev.xylonity.tooltipoverhaul.client.render.TooltipContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Redirect;
import dev.xylonity.tooltipoverhaul.client.layout.TooltipSizeCalculator;
import net.minecraft.util.math.Vec2f;

@Pseudo
@Mixin(targets = "dev.xylonity.tooltipoverhaul.client.render.TooltipRenderer")
public class TooltipRendererMixin {

    @Final
    @Shadow
    private TooltipContext context;

    @Inject(method = "init", at = @At("HEAD"))
    private void sts$applyDimensionsToTO(CallbackInfo ci) {
        TooltipOverhaulCompat.applyDimensions(this.context);
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Ldev/xylonity/tooltipoverhaul/client/layout/TooltipSizeCalculator;calculate()Lnet/minecraft/util/math/Vec2f;"))
    private Vec2f sts$redirectCalculate(TooltipSizeCalculator instance) {
        Vec2f original = instance.calculate();
        float newHeight = TooltipOverhaulLayoutFixer.fixBoxHeight(original.y, this.context);
        if (newHeight != original.y) {
            return new Vec2f(original.x, newHeight);
        }
        return original;
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void sts$ensureContextRender(CallbackInfoReturnable<Boolean> cir) {
        TooltipOverhaulStateManager.ensureContext(this.context);
    }

    @Inject(method = "renderOut", at = @At("HEAD"))
    private void sts$ensureContextRenderOut(float progress, CallbackInfo ci) {
        TooltipOverhaulStateManager.ensureContext(this.context);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void sts$clearContextRender(CallbackInfoReturnable<Boolean> cir) {
        TooltipOverhaulStateManager.clearContext();
    }

    @Inject(method = "renderOut", at = @At("TAIL"))
    private void sts$clearContextRenderOut(float progress, CallbackInfo ci) {
        TooltipOverhaulStateManager.clearContext();
    }
}
