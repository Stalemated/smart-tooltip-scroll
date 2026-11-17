package com.stalemated.sts.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

public class OrderedTextUtil {
    public static FormattedCharSequence stripSpaces(FormattedCharSequence original) {
        List<StyledChar> chars = new ArrayList<>();

        original.accept((index, style, codePoint) -> {
            chars.add(new StyledChar(codePoint, style));
            return true;
        });

        int start = 0;
        while (start < chars.size() && Character.isWhitespace(chars.get(start).codePoint)) start++;

        int end = chars.size() - 1;
        while (end >= start && Character.isWhitespace(chars.get(end).codePoint)) end--;

        if (start > end) return FormattedCharSequence.EMPTY;

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

    public static String getStringFromOrderedText(FormattedCharSequence text) {
        StringBuilder builder = new StringBuilder();
        text.accept((index, style, codePoint) -> {
            builder.appendCodePoint(codePoint);
            return true;
        });
        return builder.toString();
    }

    private record StyledChar(int codePoint, Style style) {}
}
