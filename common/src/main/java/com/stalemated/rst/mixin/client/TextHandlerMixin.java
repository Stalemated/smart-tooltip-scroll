package com.stalemated.rst.mixin.client;


import com.stalemated.rst.config.ConfigManager;
import com.stalemated.rst.resize.TooltipDimensionManager;
import com.stalemated.rst.util.TooltipTextUtil;
import net.minecraft.client.font.TextHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TextHandler.class)
public abstract class TextHandlerMixin {

    @ModifyVariable(method = "wrapLines(Lnet/minecraft/text/StringVisitable;ILnet/minecraft/text/Style;)Ljava/util/List;", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int rst$disableAutoWrapVisitable(int maxWidth) {
        if (rst$wrappingConditions()) {
            return Integer.MAX_VALUE;
        }
        return maxWidth;
    }

    @ModifyVariable(method = "wrapLines(Ljava/lang/String;ILnet/minecraft/text/Style;)Ljava/util/List;", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int rst$disableAutoWrapString(int maxWidth) {
        if (rst$wrappingConditions()) {
            return Integer.MAX_VALUE;
        }
        return maxWidth;
    }

    @Unique
    private boolean rst$wrappingConditions() {
        return TooltipDimensionManager.isCurrentTooltipItemTooltip && ConfigManager.getConfig().custom_tooltip_dimensions && !TooltipTextUtil.isHandlingCustomWrap;
    }
}
