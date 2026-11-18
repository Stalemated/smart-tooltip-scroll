package com.stalemated.sts.scroll;

import com.stalemated.sts.scroll.easing.ScrollEasingStyle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScrollStateTest {
    private static final ScrollSettings INSTANT = new ScrollSettings(0.0f, 1.5f, ScrollEasingStyle.CLASSIC);
    private static final ScrollSettings QUARTER_SECOND = new ScrollSettings(0.25f, 1.5f, ScrollEasingStyle.CLASSIC);

    @Test
    void aWheelStepScrollsLinesPerScrollLines() {
        ScrollState state = renderedWithMaxScroll(100, 1000, INSTANT);

        assertTrue(state.scroll(-1, 1010, INSTANT));
        assertEquals(15, state.getScrollOffset(1010, INSTANT));
    }

    @Test
    void theScrollStopsAtBothEnds() {
        ScrollState state = renderedWithMaxScroll(20, 1000, INSTANT);

        state.scroll(-5, 1010, INSTANT);
        assertEquals(20, state.getScrollOffset(1010, INSTANT));

        state.scroll(10, 1020, INSTANT);
        assertEquals(0, state.getScrollOffset(1020, INSTANT));
    }

    @Test
    void aTooltipWithNothingToScrollLetsTheWheelThrough() {
        ScrollState state = renderedWithMaxScroll(0, 1000, INSTANT);

        assertFalse(state.scroll(-1, 1010, INSTANT));
    }

    @Test
    void aTooltipNotDrawnForAQuarterSecondLetsTheWheelThrough() {
        ScrollState state = renderedWithMaxScroll(100, 1000, INSTANT);

        assertFalse(state.scroll(-1, 1250, INSTANT));
    }

    @Test
    void aSmoothScrollReachesItsTargetWhenTheAnimationEnds() {
        ScrollState state = renderedWithMaxScroll(100, 1000, QUARTER_SECOND);

        state.scroll(-1, 1000, QUARTER_SECOND);
        assertEquals(7, state.getScrollOffset(1125, QUARTER_SECOND));
        assertEquals(15, state.getScrollOffset(1250, QUARTER_SECOND));
    }

    @Test
    void aShrinkingTooltipPullsTheScrollBackInside() {
        ScrollState state = renderedWithMaxScroll(100, 1000, INSTANT);
        state.scroll(-5, 1010, INSTANT);

        state.updateMaxScroll(30, 1020, INSTANT);
        assertEquals(30, state.getScrollOffset(1020, INSTANT));
    }

    private static ScrollState renderedWithMaxScroll(int maxScroll, long time, ScrollSettings settings) {
        ScrollState state = new ScrollState();
        state.markRendered(time);
        state.updateMaxScroll(maxScroll, time, settings);
        return state;
    }
}
