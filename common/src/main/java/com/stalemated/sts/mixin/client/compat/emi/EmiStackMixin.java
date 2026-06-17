package com.stalemated.sts.mixin.client.compat.emi;

import com.stalemated.sts.compat.emi.EmiItemMarkerComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(targets = "dev.emi.emi.api.stack.ItemEmiStack", remap = false)
public class EmiStackMixin {

    @Inject(method = "getTooltip", at = @At("RETURN"), cancellable = true, remap = false)
    private void sts$injectMarker(CallbackInfoReturnable<List<TooltipComponent>> cir) {
        List<TooltipComponent> list = cir.getReturnValue();

        if (list != null && !list.isEmpty()) {
            List<TooltipComponent> modifiableList = new ArrayList<>(list);
            modifiableList.add(new EmiItemMarkerComponent());
            cir.setReturnValue(modifiableList);
        }
    }
}
