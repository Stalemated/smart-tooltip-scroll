package com.stalemated.sts.resize.components;

import com.stalemated.lib.component.IndentedTextTooltipComponent;
import com.stalemated.sts.util.StsManagedTitle;
import net.minecraft.text.OrderedText;

public class StsIndentedTextTooltipComponent extends IndentedTextTooltipComponent implements StsManagedTitle {
    private final int bottomPadding;

    public StsIndentedTextTooltipComponent(OrderedText text, int xOffset, int bottomPadding) {
        super(text, xOffset);
        this.bottomPadding = bottomPadding;
    }

    @Override
    public int getHeight() {
        return super.getHeight() + this.bottomPadding;
    }
}
