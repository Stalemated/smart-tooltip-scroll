package com.stalemated.sts.compat.puffish;

import com.stalemated.lib.util.state.SharedTooltipState;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.resize.TooltipDimensionManager;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class PuffishSkillsCompat {

    public static boolean isSkillTooltipContext = false;

    public static List<FormattedCharSequence> wrapLines(Minecraft client, Component text) {
        if (isSkillTooltipContext && SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions && client.font != null) {
            SharedTooltipState.requestCustomDimensions();
            int targetWidth = TooltipDimensionManager.getScaledTooltipWidth();
            return client.font.split(text, targetWidth);
        }
        return Tooltip.splitTooltip(client, text);
    }
}
