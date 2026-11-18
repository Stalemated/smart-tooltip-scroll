package com.stalemated.sts.scroll;

import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.resize.TooltipStyles;
import com.stalemated.sts.scroll.identity.TooltipIdentityContext;
import com.stalemated.sts.scroll.identity.TooltipIdentityTracker;
import com.stalemated.sts.state.TooltipContextManager;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

import java.util.List;

public class TooltipScrollManager {
    public static final TooltipScrollManager INSTANCE = new TooltipScrollManager();

    private final ScrollState scrollState = new ScrollState();
    private final TooltipIdentityTracker identityTracker = new TooltipIdentityTracker();

    private TooltipScrollManager() {}

    public void track(List<ClientTooltipComponent> body, Font font, int maxScroll) {
        TooltipIdentityContext context = new TooltipIdentityContext(TooltipContextManager.currentStack(), body, font);
        long currentTime = now();

        if (identityTracker.hasTooltipChanged(context) || scrollState.isUnhovered(currentTime)) {
            scrollState.reset(currentTime);
        }

        identityTracker.updateIdentity(context);
        scrollState.markRendered(currentTime);
        TooltipStyles.loaded().resetExternalScroll();
        scrollState.updateMaxScroll(maxScroll, currentTime, settings());
    }

    public boolean scroll(double amount) {
        return scrollState.scroll(amount, now(), settings());
    }

    public int getScrollOffset() {
        return scrollState.getScrollOffset(now(), settings());
    }

    private static long now() {
        return System.nanoTime() / 1_000_000L;
    }

    private static ScrollSettings settings() {
        return ScrollSettings.of(SmartTooltipScrollConfigRegistry.CONFIG.get());
    }
}
