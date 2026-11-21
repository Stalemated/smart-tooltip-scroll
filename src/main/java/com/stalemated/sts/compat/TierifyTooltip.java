package com.stalemated.sts.compat;

public final class TierifyTooltip {
    private static boolean drawing;

    private TierifyTooltip() {
    }

    public static boolean isDrawing() {
        return drawing;
    }

    public static void beginDraw() {
        drawing = true;
    }

    public static void endDraw() {
        drawing = false;
    }
}
