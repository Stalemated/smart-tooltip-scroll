package com.stalemated.sts.resize;

import com.mojang.blaze3d.platform.Window;
import com.stalemated.lib.util.math.MathUtils;
import com.stalemated.sts.config.SmartTooltipScrollConfig;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import net.minecraft.client.Minecraft;

public record TooltipBounds(int maxWidth, int maxHeight) {
    public static final int MIN_WIDTH = 64;
    public static final int MIN_HEIGHT = 32;
    private static final int PADDING_X = 8;
    private static final int PADDING_Y = 4;

    public static TooltipBounds current() {
        Window window = Minecraft.getInstance().getWindow();
        SmartTooltipScrollConfig config = SmartTooltipScrollConfigRegistry.CONFIG.get();
        return forWindow(window.getGuiScaledWidth(), window.getGuiScaledHeight(), config.max_width_percentage, config.max_height_percentage);
    }

    public static TooltipBounds forWindow(int scaledWidth, int scaledHeight, int widthPercent, int heightPercent) {
        return new TooltipBounds(maxSide(scaledWidth, widthPercent, PADDING_X, MIN_WIDTH), maxSide(scaledHeight, heightPercent, PADDING_Y, MIN_HEIGHT));
    }

    private static int maxSide(int windowSide, int percent, int padding, int minSide) {
        int usableSide = windowSide - padding;
        int safePercent = MathUtils.clamp(percent, 1, 100);
        return MathUtils.clamp(usableSide * safePercent / 100, minSide, usableSide);
    }
}
