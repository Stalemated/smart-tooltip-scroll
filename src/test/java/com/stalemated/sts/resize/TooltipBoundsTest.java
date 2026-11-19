package com.stalemated.sts.resize;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TooltipBoundsTest {
    @Test
    void halfTheUsableWindowAtFiftyPercent() {
        assertEquals(new TooltipBounds(196, 148), TooltipBounds.forWindow(400, 300, 50, 50));
    }

    @Test
    void theWholeUsableWindowAtOneHundredPercent() {
        assertEquals(new TooltipBounds(392, 296), TooltipBounds.forWindow(400, 300, 100, 100));
    }

    @Test
    void neverSmallerThanTheMinimumTooltipSize() {
        assertEquals(new TooltipBounds(TooltipBounds.MIN_WIDTH, TooltipBounds.MIN_HEIGHT), TooltipBounds.forWindow(400, 300, 1, 1));
    }

    @Test
    void widthAndHeightFollowTheirOwnPercentages() {
        assertEquals(new TooltipBounds(392, 148), TooltipBounds.forWindow(400, 300, 100, 50));
    }

    @Test
    void aWidthOfZeroPercentIsRejected() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> TooltipBounds.forWindow(400, 300, 0, 50));

        assertEquals("max_width_percentage must be 1-100, got 0", error.getMessage());
    }
}
