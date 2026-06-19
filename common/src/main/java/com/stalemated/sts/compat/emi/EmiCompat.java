package com.stalemated.sts.compat.emi;

import com.stalemated.sts.resize.TooltipDimensionManager;
import com.stalemated.sts.resize.enforce.TooltipEnforcerRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
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
                    TooltipContext context = TooltipContext.DEFAULT;
                    TooltipType tooltipType = client.options.advancedItemTooltips ? TooltipType.Default.ADVANCED : TooltipType.Default.BASIC;
                    
                    try {
                        List<Text> texts = stack.getTooltip(context, player, tooltipType);
                        if (!texts.isEmpty()) {
                            TooltipDimensionManager.expectedTitleString = texts.getFirst().getString().replace(" ", "");
                        }
                    } catch (Exception ignored) {}
                }
                return true;
            }
            return false;
        });
    }
}
