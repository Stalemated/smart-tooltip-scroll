package com.stalemated.sts.gui.screen;

import com.stalemated.lib.compat.yacl.controller.builder.SimpleEnumDropdownControllerBuilder;
import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.config.STSConfig;
import com.stalemated.sts.resize.TitleOverflowMode;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TooltipDimensionsScreen {


    public static Screen create(Screen parent) {
        STSConfig config = ConfigManager.getConfig();

        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("sts.tooltip_dimensions_screen.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("sts.tooltip_dimensions_screen.title"))
                        .group(createCustomDimensionsGroup(config))
                        .group(createScrollingGroup(config))
                        .build())
                .save(ConfigManager::save)
                .build()
                .generateScreen(parent);
    }

    private static OptionGroup createCustomDimensionsGroup(STSConfig config) {
        var maxWidth = Option.<Integer>createBuilder()
                .name(Text.translatable("sts.tooltip_dimensions_screen.max_width_percentage"))
                .description(OptionDescription.of(Text.translatable("sts.tooltip_dimensions_screen.max_width_percentage.description")))
                .binding(
                        50,
                        () -> config.max_width_percentage,
                        val -> config.max_width_percentage = val
                )
                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                        .range(1, 100)
                        .step(1))
                .build();

        var maxHeight = Option.<Integer>createBuilder()
                .name(Text.translatable("sts.tooltip_dimensions_screen.max_height_percentage"))
                .description(OptionDescription.of(Text.translatable("sts.tooltip_dimensions_screen.max_height_percentage.description")))
                .binding(
                        50,
                        () -> config.max_height_percentage,
                        val -> config.max_height_percentage = val
                )
                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                        .range(1, 100)
                        .step(1))
                .build();

        var enableCustomDimensions = Option.<Boolean>createBuilder()
                .name(Text.translatable("sts.tooltip_dimensions_screen.enable_custom_dimensions"))
                .description(OptionDescription.of(Text.translatable("sts.tooltip_dimensions_screen.enable_custom_dimensions.description")))
                .binding(
                        false,
                        () -> config.custom_tooltip_dimensions,
                        val -> config.custom_tooltip_dimensions = val
                )
                .controller(TickBoxControllerBuilder::create)
                .build();

        var titleOverflowMode = Option.<TitleOverflowMode>createBuilder()
                .name(Text.translatable("sts.tooltip_dimensions_screen.title_overflow_mode"))
                .description(OptionDescription.of(Text.translatable("sts.tooltip_dimensions_screen.title_overflow_mode.description")))
                .binding(
                        TitleOverflowMode.SCROLL,
                        () -> config.title_overflow_mode,
                        val -> config.title_overflow_mode = val
                )
                .controller(opt -> SimpleEnumDropdownControllerBuilder.create(opt)
                        .formatValue(mode -> Text.translatable("sts.tooltip_dimensions_screen.title_overflow_mode." + mode.name().toLowerCase())))
                .build();

        return OptionGroup.createBuilder()
                .name(Text.translatable("sts.tooltip_dimensions_screen.category.custom_dimensions"))
                .option(enableCustomDimensions)
                .option(maxHeight)
                .option(maxWidth)
                .option(titleOverflowMode)
                .build();
    }

    private static OptionGroup createScrollingGroup(STSConfig config) {
        var lockContainerScrolling = Option.<Boolean>createBuilder()
                .name(Text.translatable("sts.tooltip_dimensions_screen.lock_container_scrolling"))
                .description(OptionDescription.of(
                        Text.translatable("sts.tooltip_dimensions_screen.lock_container_scrolling.description"),
                        Text.translatable("sts.tooltip_dimensions_screen.lock_container_scrolling.warning")
                ))
                .binding(
                        true,
                        () -> config.lock_container_scrolling,
                        val -> config.lock_container_scrolling = val
                )
                .controller(TickBoxControllerBuilder::create)
                .build();

        var scrollSmoothness = Option.<Float>createBuilder()
                .name(Text.translatable("sts.tooltip_dimensions_screen.scroll_smoothness"))
                .description(OptionDescription.of(Text.translatable("sts.tooltip_dimensions_screen.scroll_smoothness.description")))
                .binding(
                        0.25f,
                        () -> config.scroll_smoothness,
                        val -> config.scroll_smoothness = val
                )
                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                        .range(0.0f, 1.0f)
                        .step(0.01f)
                        .formatValue(val -> Text.of(
                                new BigDecimal(Float.toString(val))
                                .setScale(2, RoundingMode.HALF_UP)
                                .toString())))
                .build();

        return OptionGroup.createBuilder()
                .name(Text.translatable("sts.tooltip_dimensions_screen.category.scrolling"))
                .option(scrollSmoothness)
                .option(lockContainerScrolling)
                .build();
    }
}
