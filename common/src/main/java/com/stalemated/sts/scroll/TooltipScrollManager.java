package com.stalemated.sts.scroll;

import com.stalemated.sts.scroll.identity.TooltipIdentityContext;
import com.stalemated.sts.scroll.identity.TooltipIdentityTracker;
import com.stalemated.sts.scroll.resetter.ExternalScrollStateResetter;

import java.util.ArrayList;
import java.util.List;

public class TooltipScrollManager {
    public static final TooltipScrollManager INSTANCE = new TooltipScrollManager();

    private final ScrollState scrollState = new ScrollState();
    private final TooltipIdentityTracker identityTracker = new TooltipIdentityTracker();
    private final List<ExternalScrollStateResetter> externalResetters = new ArrayList<>();

    private TooltipScrollManager() {}

    public static void registerResetter(ExternalScrollStateResetter resetter) {
        INSTANCE.externalResetters.add(resetter);
    }

    public void onTooltipRendered(int newMaxScroll, TooltipIdentityContext context) {
        long currentTime = System.currentTimeMillis();
        
        boolean isDifferentTooltip = identityTracker.hasTooltipChanged(context);

        if (isDifferentTooltip || scrollState.isUnhovered(currentTime)) {
            scrollState.reset(currentTime);
        }
        
        identityTracker.updateIdentity(context);
        scrollState.markRendered(currentTime);

        for (ExternalScrollStateResetter resetter : externalResetters) {
            resetter.resetState();
        }

        scrollState.updateMaxScroll(newMaxScroll, currentTime);
    }

    public boolean scroll(double amount) {
        return scrollState.scroll(amount, System.currentTimeMillis());
    }

    public int getScrollOffset() {
        return scrollState.getScrollOffset(System.currentTimeMillis());
    }
}