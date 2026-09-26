package com.stalemated.sts.config;

import com.stalemated.lib.config.annotation.Comment;
import com.stalemated.lib.config.annotation.RangeDouble;
import com.stalemated.lib.config.annotation.RangeFloat;
import com.stalemated.lib.config.annotation.RangeInt;
import com.stalemated.sts.resize.TitleOverflowMode;
import com.stalemated.sts.scroll.easing.ScrollEasingStyle;

public class STSConfig {
    @Comment("If custom Tooltip dimensions are enabled.")
    public boolean custom_tooltip_dimensions = true;

    @Comment("How to handle long tooltip titles that exceed the maximum tooltip width. Accepts: TRUNCATE, WRAP, SCROLL")
    public TitleOverflowMode title_overflow_mode = TitleOverflowMode.SCROLL;

    @Comment("Maximum percentage of the Minecraft window allowed as the tooltip height.")
    @RangeInt(min = 0, max = 100)
    public int max_height_percentage = 50;

    @Comment("Maximum percentage of the Minecraft window allowed as the tooltip width.")
    @RangeInt(min = 0, max = 100)
    public int max_width_percentage = 50;

    @Comment("If scrolling in different containers is disabled while scrolling on a tooltip. You probably don't want to disable this.")
    public boolean lock_container_scrolling = true;

    @Comment("The style of the scroll animation. Accepts: CLASSIC, SMOOTH, SNAPPY")
    public ScrollEasingStyle scroll_easing_style = ScrollEasingStyle.SMOOTH;

    @Comment("Smoothness of the tooltip scroll animation. 0.0 is instant, 1.0 is very smooth.")
    @RangeFloat(min = 0.0f, max = 1.0f)
    public float scroll_smoothness = 0.25f;

    @Comment("Number of lines scrolled per mouse wheel scroll.")
    @RangeFloat(min = 0.5f, max = 5.0f)
    public float lines_per_scroll = 1.5f;

    @Comment("If the item's title gets automatically centered in the tooltip.")
    public boolean title_centering = true;

    @Comment("The tooltip title's scroll speed when Title Overflow mode is set to 'SCROLL', higher is faster.")
    @RangeDouble(min = 1.0, max = 100.0)
    public double title_scroll_speed = 25;

    @Comment("The elapsed time in ms while the scrolling motion is paused when Title Overflow mode is set to 'SCROLL'.")
    @RangeInt(min = 0, max = 5000)
    public int title_scroll_pause_time_ms = 2000;

    @Comment("If compatibility with Obscure Tooltips is enabled.")
    public boolean obscure_compat = true;

    @Comment("If compatibility with Tooltip Overhaul is enabled.")
    public boolean tooltip_overhaul_compat = true;
}