package com.stalemated.sts.resize.components;

import com.stalemated.sts.util.StsManagedTitle;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.util.FormattedCharSequence;

public class TruncatedTitleTooltipComponent extends ClientTextTooltip implements ClientTooltipComponent, StsManagedTitle {
    public TruncatedTitleTooltipComponent(FormattedCharSequence text) {
        super(text);
    }
}
