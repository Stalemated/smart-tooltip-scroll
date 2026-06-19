package com.stalemated.sts.scroll;

import com.stalemated.sts.config.ConfigManager;

public class TooltipScrollManager {
    private static int targetScroll = 0;
    private static int startScroll = 0;
    private static long scrollStartTime = 0;

    private static int maxScroll = 0;
    private static long lastRenderTime = 0;
    private static final int maxUnhoveredRenderTimeMs = 250;
    private static final int smoothnessTimeMs = 1000;

    public static void updateMaxScroll(int newMaxScroll) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastRenderTime > maxUnhoveredRenderTimeMs) {
            targetScroll = 0;
            startScroll = 0;
            scrollStartTime = currentTime;
        }
        lastRenderTime = currentTime;

        maxScroll = Math.max(0, newMaxScroll);
        
        int oldTarget = targetScroll;
        targetScroll = Math.clamp(targetScroll, 0, maxScroll);
        if (targetScroll != oldTarget) {
            startScroll = getScrollOffset();
            scrollStartTime = currentTime;
        }
    }

    public static boolean scroll(double amount) {
        if (maxScroll <= 0) return false;

        int pixelsPerScroll = 15;
        if (System.currentTimeMillis() - lastRenderTime < maxUnhoveredRenderTimeMs) {
            startScroll = getScrollOffset();
            scrollStartTime = System.currentTimeMillis();
            
            targetScroll -= (int) (amount * pixelsPerScroll);
            targetScroll = Math.clamp(targetScroll, 0, maxScroll);
            return true;
        }
        return false;
    }

    public static int getScrollOffset() {
        if (targetScroll == startScroll) return targetScroll;
        
        float smoothness = ConfigManager.getConfig().scroll_smoothness;
        if (smoothness <= 0.0f) return targetScroll;

        int durationMs = (int) (smoothness * smoothnessTimeMs);
        if (durationMs <= 0) return targetScroll;
        
        long elapsed = System.currentTimeMillis() - scrollStartTime;
        if (elapsed >= durationMs) {
            startScroll = targetScroll;
            return targetScroll;
        }
        
        // Ease-out cubic
        double t = (double) elapsed / durationMs;
        double easeOutCubic = 1.0 - Math.pow(1.0 - t, 3.0);
        
        return (int) (startScroll + (targetScroll - startScroll) * easeOutCubic);
    }
}