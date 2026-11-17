package com.stalemated.sts.resize.components;

import com.stalemated.lib.component.IndentedTextTooltipComponent;
import com.stalemated.sts.util.StsManagedTitle;
import net.minecraft.util.FormattedCharSequence;

public class StsIndentedTextTooltipComponent extends IndentedTextTooltipComponent implements StsManagedTitle {
    private final int bottomPadding;

    public StsIndentedTextTooltipComponent(FormattedCharSequence text, int xOffset, int bottomPadding) {
        super(text, xOffset);
        this.bottomPadding = bottomPadding;
    }

    @Override
    public int getHeight() {
        return super.getHeight() + this.bottomPadding;
    }
}
