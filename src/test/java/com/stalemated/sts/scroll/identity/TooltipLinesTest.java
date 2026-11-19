package com.stalemated.sts.scroll.identity;

import com.stalemated.sts.scroll.identity.TooltipLines.Line;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TooltipLinesTest {
    @Test
    void aTooltipThatGainsALineHasDifferentHeights() {
        TooltipLines collapsed = new TooltipLines(List.of(new Line(120, 10), new Line(80, 10)));
        TooltipLines expanded = new TooltipLines(List.of(new Line(120, 10), new Line(80, 10), new Line(100, 10)));

        assertFalse(expanded.sameHeights(collapsed));
    }

    @Test
    void newTextOnALineKeepsTheHeights() {
        TooltipLines before = new TooltipLines(List.of(new Line(120, 10), new Line(60, 10)));
        TooltipLines after = new TooltipLines(List.of(new Line(120, 10), new Line(66, 10)));

        assertTrue(after.sameHeights(before));
    }
}
