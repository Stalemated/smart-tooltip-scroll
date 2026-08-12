package com.stalemated.sts.scroll;

import com.stalemated.lib.util.math.MathUtils;
import com.stalemated.sts.config.ConfigManager;

public class ScrollState {
    private int targetScroll = 0;
    private int startScroll = 0;
    private long scrollStartTime = 0;
    private int maxScroll = 0;
    private long lastRenderTime = 0;

    private static final int MAX_UNHOVERED_RENDER_TIME_MS = 250;
    private static final int SMOOTHNESS_TIME_MS = 1000;
    private static final int PIXELS_PER_SCROLL = 15;

    public void updateMaxScroll(int newMaxScroll, long currentTime) {
        this.maxScroll = Math.max(0, newMaxScroll);
        
        int oldTarget = this.targetScroll;
        this.targetScroll = MathUtils.clamp(this.targetScroll, 0, this.maxScroll);
        
        if (this.targetScroll != oldTarget) {
            this.startScroll = getScrollOffset(currentTime);
            this.scrollStartTime = currentTime;
        }
    }

    public boolean scroll(double amount, long currentTime) {
        if (this.maxScroll <= 0) return false;

        if (currentTime - this.lastRenderTime < MAX_UNHOVERED_RENDER_TIME_MS) {
            this.startScroll = getScrollOffset(currentTime);
            this.scrollStartTime = currentTime;
            
            this.targetScroll -= (int) (amount * PIXELS_PER_SCROLL);
            this.targetScroll = MathUtils.clamp(this.targetScroll, 0, this.maxScroll);
            return true;
        }
        return false;
    }

    public void reset(long currentTime) {
        this.targetScroll = 0;
        this.startScroll = 0;
        this.scrollStartTime = currentTime;
    }

    public int getScrollOffset(long currentTime) {
        if (this.targetScroll == this.startScroll) return this.targetScroll;
        
        float smoothness = ConfigManager.getConfig().scroll_smoothness;
        if (smoothness <= 0.0f) return this.targetScroll;

        int durationMs = (int) (smoothness * SMOOTHNESS_TIME_MS);
        if (durationMs <= 0) return this.targetScroll;
        
        long elapsed = currentTime - this.scrollStartTime;
        if (elapsed >= durationMs) {
            this.startScroll = this.targetScroll;
            return this.targetScroll;
        }
        
        // Ease-out cubic
        double t = (double) elapsed / durationMs;
        double easeOutCubic = 1.0 - Math.pow(1.0 - t, 3.0);
        
        return (int) (this.startScroll + (this.targetScroll - this.startScroll) * easeOutCubic);
    }
    
    public void markRendered(long currentTime) {
        this.lastRenderTime = currentTime;
    }

    public boolean isUnhovered(long currentTime) {
        return currentTime - this.lastRenderTime > MAX_UNHOVERED_RENDER_TIME_MS;
    }
}
