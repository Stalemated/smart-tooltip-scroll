package com.stalemated.sts.compat.emi;

import com.stalemated.sts.resize.TooltipDimensionManager;
import com.stalemated.sts.resize.enforce.TooltipEnforcerRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class EmiCompat {
    public static void init() {
        TooltipEnforcerRegistry.register(components -> {
            boolean hasMarker = false;

            for (TooltipComponent component : components) {
                if (component instanceof EmiItemMarkerComponent) {
                    hasMarker = true;
                    break;
                }
            }

            if (hasMarker) {
                TooltipDimensionManager.isPreWrappedTitle = true;
                ItemStack stack = TooltipDimensionManager.getCurrentStack();

                if (stack != null && !stack.isEmpty()) {
                    MinecraftClient client = MinecraftClient.getInstance();
                    PlayerEntity player = client.player;
                    TooltipContext context = client.options.advancedItemTooltips ? TooltipContext.Default.ADVANCED : TooltipContext.Default.BASIC;
                    
                    try {
                        List<Text> texts = stack.getTooltip(player, context);
                        if (!texts.isEmpty()) {
                            TooltipDimensionManager.expectedTitleString = texts.get(0).getString().replace(" ", "");
                        }
                    } catch (Exception ignored) {}
                }
                return true;
            }
            return false;
        });
    }
}
