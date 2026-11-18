package com.stalemated.sts.scroll.resetter;

/**
 * Interface for resetting external mods' tooltip scroll states.
 * This ensures that other mods don't accidentally intercept scroll events
 * while a Smart Tooltip Scroll tooltip is being rendered.
 */
public interface ExternalScrollStateResetter {
    void resetState();
}
