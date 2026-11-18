package com.stalemated.sts.scroll.easing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScrollEasingStyleTest {
    @Test
    void everyStyleStartsAtZeroAndEndsAtOne() {
        for (ScrollEasingStyle style : ScrollEasingStyle.values()) {
            assertEquals(0.0, style.progress(0.0), 1e-9, style.name());
            assertEquals(1.0, style.progress(1.0), 1e-9, style.name());
        }
    }

    @Test
    void classicMovesAtAConstantSpeed() {
        assertEquals(0.5, ScrollEasingStyle.CLASSIC.progress(0.5), 1e-9);
    }

    @Test
    void smoothSlowsDownTowardsTheEnd() {
        assertEquals(0.875, ScrollEasingStyle.SMOOTH.progress(0.5), 1e-9);
    }

    @Test
    void snappyIsAlmostThereHalfway() {
        assertEquals(0.96875, ScrollEasingStyle.SNAPPY.progress(0.5), 1e-9);
    }
}
