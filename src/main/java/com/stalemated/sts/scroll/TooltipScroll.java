package com.stalemated.sts.scroll;

import com.stalemated.lib.util.input.KeyBindingUtil;
import com.stalemated.sts.config.SmartTooltipScrollConfig;
import com.stalemated.sts.config.SmartTooltipScrollConfigRegistry;
import com.stalemated.sts.resize.TooltipStyles;
import com.stalemated.sts.scroll.identity.TooltipIdentityContext;
import com.stalemated.sts.scroll.identity.TooltipIdentityTracker;
import com.stalemated.sts.state.TooltipContexts;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

import java.util.List;

import static com.stalemated.sts.keybind.SmartTooltipScrollKeys.SCROLL_BYPASS_KEY;

public class TooltipScroll {
    public static final TooltipScroll INSTANCE = new TooltipScroll();

    private final ScrollState scrollState = new ScrollState();
    private final TooltipIdentityTracker identityTracker = new TooltipIdentityTracker();

    private TooltipScroll() {}

    public void track(List<ClientTooltipComponent> body, Font font, int maxScroll) {
        TooltipIdentityContext context = new TooltipIdentityContext(TooltipContexts.currentStack(), body, font);
        long currentTime = now();

        if (identityTracker.hasTooltipChanged(context) || scrollState.isUnhovered(currentTime)) {
            scrollState.reset(currentTime);
        }

        identityTracker.updateIdentity(context);
        scrollState.markRendered(currentTime);
        TooltipStyles.loaded().resetExternalScroll();
        scrollState.updateMaxScroll(maxScroll, currentTime, settings());
    }

    public boolean consumeWheel(double amount) {
        if (KeyBindingUtil.isKeyDownInGui(SCROLL_BYPASS_KEY)) return false;

        SmartTooltipScrollConfig config = SmartTooltipScrollConfigRegistry.CONFIG.get();
        if (!config.custom_tooltip_dimensions) return false;
        return scrollState.scroll(amount, now(), settings()) && config.lock_container_scrolling;
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
