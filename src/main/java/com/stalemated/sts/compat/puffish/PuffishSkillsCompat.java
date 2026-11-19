package com.stalemated.sts.compat.puffish;

import com.stalemated.lib.util.state.SharedTooltipState;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.resize.TooltipBounds;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class PuffishSkillsCompat {

    private static boolean drawingSkillTree;

    public static void beginSkillTree() {
        drawingSkillTree = true;
    }

    public static void endSkillTree() {
        drawingSkillTree = false;
    }

    public static boolean wrapsSkillTooltips() {
        return drawingSkillTree && SmartTooltipScrollConfigRegistry.CONFIG.get().custom_tooltip_dimensions;
    }

    public static List<FormattedCharSequence> splitSkillTooltip(Minecraft client, Component text) {
        SharedTooltipState.requestCustomDimensions();
        return client.font.split(text, TooltipBounds.current().maxWidth());
    }
}
