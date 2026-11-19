package com.stalemated.sts.resize;

import com.mojang.blaze3d.platform.Window;
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
        requirePercentage("max_width_percentage", widthPercent);
        requirePercentage("max_height_percentage", heightPercent);
        return new TooltipBounds(maxSide(scaledWidth, widthPercent, PADDING_X, MIN_WIDTH), maxSide(scaledHeight, heightPercent, PADDING_Y, MIN_HEIGHT));
    }

    private static void requirePercentage(String option, int percent) {
        if (percent < 1 || percent > 100) throw new IllegalArgumentException(option + " must be 1-100, got " + percent);
    }

    private static int maxSide(int windowSide, int percent, int padding, int minSide) {
        int usableSide = windowSide - padding;
        return Math.max(usableSide * percent / 100, minSide);
    }
}
