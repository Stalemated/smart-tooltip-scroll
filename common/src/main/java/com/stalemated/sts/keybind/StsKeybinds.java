package com.stalemated.sts.keybind;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import com.stalemated.lib.helper.PlatformHelper;
import net.minecraft.client.KeyMapping;

public class StsKeybinds {
    public static final KeyMapping SCROLL_BYPASS_KEY = new KeyMapping(
            "key.smart_tooltip_scroll.bypass_scroll",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_CONTROL,
            "category.smart_tooltip_scroll.keys"
    );

    public static void register() {
        PlatformHelper.INSTANCE.registerKeyBinding(SCROLL_BYPASS_KEY);
    }
}
