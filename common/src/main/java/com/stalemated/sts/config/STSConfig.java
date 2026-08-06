package com.stalemated.sts.config;

import com.stalemated.sts.resize.TitleOverflowMode;
import dev.isxander.yacl3.config.v2.api.SerialEntry;

public class STSConfig {
    @SerialEntry(comment = "If custom Tooltip dimensions are enabled.")
    public boolean custom_tooltip_dimensions = true;

    @SerialEntry(comment = "How to handle long tooltip titles that exceed the maximum tooltip width. Accepts: TRUNCATE, WRAP, SCROLL")
    public TitleOverflowMode title_overflow_mode = TitleOverflowMode.SCROLL;

    @SerialEntry(comment = "Maximum percentage of the Minecraft window allowed as the tooltip height.")
    public int max_height_percentage = 50;

    @SerialEntry(comment = "Maximum percentage of the Minecraft window allowed as the tooltip width.")
    public int max_width_percentage = 50;

    @SerialEntry(comment = "If scrolling in different containers is disabled while scrolling on a tooltip. You probably don't want to disable this.")
    public boolean lock_container_scrolling = true;

    @SerialEntry(comment = "Smoothness of the tooltip scroll animation. 0.0 is instant, 1.0 is very smooth.")
    public float scroll_smoothness = 0.25f;

    @SerialEntry(comment = "If the item's title gets automatically centered in the tooltip.")
    public boolean title_centering = true;

    @SerialEntry(comment = "The tooltip title's scroll speed when Title Overflow mode is set to 'SCROLL', higher is faster.")
    public double title_scroll_speed = 25;

    @SerialEntry(comment = "The elapsed time in ms while the scrolling motion is paused when Title Overflow mode is set to 'SCROLL'.")
    public long title_scroll_pause_time_ms = 2000;

    @SerialEntry(comment = "If compatibility with Pufferfish's Skills skill trees is enabled.")
    public boolean puffish_compat = true;

    @SerialEntry(comment = "If compatibility with Obscure Tooltips is enabled.")
    public boolean obscure_compat = true;
}