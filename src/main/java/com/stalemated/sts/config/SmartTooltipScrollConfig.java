package com.stalemated.sts.config;

import com.stalemated.lib.config.annotation.Comment;
import com.stalemated.lib.config.annotation.EnabledIf;
import com.stalemated.lib.config.annotation.Format;
import com.stalemated.lib.config.annotation.RangeDouble;
import com.stalemated.lib.config.annotation.RangeFloat;
import com.stalemated.lib.config.annotation.RangeInt;
import com.stalemated.sts.resize.TitleOverflowMode;
import com.stalemated.sts.scroll.easing.ScrollEasingStyle;

public class SmartTooltipScrollConfig {
    @Comment("If custom Tooltip dimensions are enabled.")
    public boolean custom_tooltip_dimensions = true;

    @Comment("How to handle long tooltip titles that exceed the maximum tooltip width. Accepts: TRUNCATE, WRAP, SCROLL")
    @EnabledIf("custom_tooltip_dimensions")
    public TitleOverflowMode title_overflow_mode = TitleOverflowMode.SCROLL;

    @Comment("Maximum percentage of the Minecraft window allowed as the tooltip height.")
    @EnabledIf("custom_tooltip_dimensions")
    @RangeInt(min = 1, max = 100)
    @Format("%d%%")
    public int max_height_percentage = 50;

    @Comment("Maximum percentage of the Minecraft window allowed as the tooltip width.")
    @EnabledIf("custom_tooltip_dimensions")
    @RangeInt(min = 1, max = 100)
    @Format("%d%%")
    public int max_width_percentage = 50;

    @Comment("If scrolling in different containers is disabled while scrolling on a tooltip. You probably don't want to disable this.")
    @EnabledIf("custom_tooltip_dimensions")
    public boolean lock_container_scrolling = true;

    @Comment("The style of the scroll animation. Accepts: CLASSIC, SMOOTH, SNAPPY")
    @EnabledIf("custom_tooltip_dimensions")
    public ScrollEasingStyle scroll_easing_style = ScrollEasingStyle.SMOOTH;

    @Comment("Smoothness of the tooltip scroll animation. 0.0 is instant, 1.0 is very smooth.")
    @EnabledIf("custom_tooltip_dimensions")
    @RangeFloat(min = 0.0f, max = 1.0f, step = 0.01f)
    public float scroll_smoothness = 0.25f;

    @Comment("Number of lines scrolled per mouse wheel scroll.")
    @EnabledIf("custom_tooltip_dimensions")
    @RangeFloat(min = 0.5f, max = 5.0f, step = 0.5f)
    public float lines_per_scroll = 1.5f;

    @Comment("If the item's title gets automatically centered in the tooltip.")
    @EnabledIf("custom_tooltip_dimensions")
    public boolean title_centering = true;

    @Comment("The tooltip title's scroll speed when Title Overflow mode is set to 'SCROLL', higher is faster.")
    @EnabledIf(value = "title_overflow_mode", equals = "SCROLL")
    @RangeDouble(min = 1.0, max = 100.0, step = 1.0)
    @Format("%.0f")
    public double title_scroll_speed = 25;

    @Comment("The elapsed time in ms while the scrolling motion is paused when Title Overflow mode is set to 'SCROLL'.")
    @EnabledIf(value = "title_overflow_mode", equals = "SCROLL")
    @RangeInt(min = 0, max = 5000, step = 100)
    public int title_scroll_pause_time_ms = 2000;

    @Comment("If compatibility with Obscure Tooltips is enabled.")
    @EnabledIf("custom_tooltip_dimensions")
    public boolean obscure_compat = true;

    @Comment("If compatibility with Tooltip Overhaul is enabled.")
    @EnabledIf("custom_tooltip_dimensions")
    public boolean tooltip_overhaul_compat = true;
}
