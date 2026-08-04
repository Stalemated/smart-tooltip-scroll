package com.stalemated.sts.util;

import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;

import java.util.ArrayList;
import java.util.List;

public class OrderedTextUtil {
    public static OrderedText stripSpaces(OrderedText original) {
        List<StyledChar> chars = new ArrayList<>();

        original.accept((index, style, codePoint) -> {
            chars.add(new StyledChar(codePoint, style));
            return true;
        });

        int start = 0;
        while (start < chars.size() && Character.isWhitespace(chars.get(start).codePoint)) start++;

        int end = chars.size() - 1;
        while (end >= start && Character.isWhitespace(chars.get(end).codePoint)) end--;

        if (start > end) return OrderedText.EMPTY;

        final int finalStart = start;
        final int finalEnd = end;

        return visitor -> {
            for (int i = finalStart; i <= finalEnd; i++) {
                StyledChar sc = chars.get(i);
                if (!visitor.accept(i - finalStart, sc.style, sc.codePoint)) {
                    return false;
                }
            }
            return true;
        };
    }

    private record StyledChar(int codePoint, Style style) {}
}
