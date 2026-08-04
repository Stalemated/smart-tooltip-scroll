package com.stalemated.sts.resize.components;

import com.stalemated.sts.util.StsManagedTitle;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.OrderedText;

public class TruncatedTitleTooltipComponent extends OrderedTextTooltipComponent implements TooltipComponent, StsManagedTitle {
    public TruncatedTitleTooltipComponent(OrderedText text) {
        super(text);
    }
}
