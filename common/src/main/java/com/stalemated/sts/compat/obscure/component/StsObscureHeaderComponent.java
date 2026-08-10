package com.stalemated.sts.compat.obscure.component;

import com.stalemated.sts.config.ConfigManager;
import com.stalemated.sts.resize.components.WrappedTitleTooltipComponent;
import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.component.BlankComponent;
import dev.obscuria.tooltips.client.component.HeaderComponent;
import dev.obscuria.tooltips.client.tooltip.particle.GraphicUtils;
import dev.obscuria.tooltips.config.ClientConfig;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.VertexConsumerProvider;
import org.joml.Matrix4f;

import static com.stalemated.sts.compat.obscure.ObscureTooltipsCompat.*;

public class StsObscureHeaderComponent implements TooltipComponent {

    public static final int VERTICAL_PADDING = 1;
    public static final int OT_EFFECT_OFFSET_X = 10;
    public static final int OT_EFFECT_OFFSET_Y = 11;

    private final TooltipState state;
    private final TooltipComponent title;
    private final TooltipComponent label;
    private final boolean drawSeparator;
    private final ARGB separatorColor;
    private final int totalHeight;
    private int totalWidth = -1;

    public StsObscureHeaderComponent(HeaderComponent original, TooltipComponent processedTitle) {
        this.state = original.state();
        this.title = processedTitle;
        this.label = original.label();
        this.drawSeparator = original.drawSeparator();
        this.separatorColor = original.separatorColor();

        int titleHeight = processedTitle.getHeight();
        boolean hasLabel = ClientConfig.LABELS_ENABLED.get() && !(this.label instanceof BlankComponent);
        int labelHeight = hasLabel ? this.label.getHeight() : 0;

        int contentHeight = Math.max(OT_ITEM_MODEL_OFFSET, VERTICAL_PADDING + titleHeight + labelHeight + (hasLabel ? VERTICAL_PADDING : 0));
        this.totalHeight = contentHeight + (drawSeparator ? SEPARATOR_HEIGHT : 0);
    }

    public void setTotalWidth(int totalWidth) {
        this.totalWidth = totalWidth;
    }

    private int getCenterOffset(int textBlockWidth, int lineWidth) {
        boolean centerTitle = ConfigManager.getConfig().title_centering;
        return centerTitle ? Math.max(0, (textBlockWidth - lineWidth) / 2) : 0;
    }

    @Override
    public int getHeight() {
        return this.totalHeight;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return OT_ITEM_MODEL_OFFSET + Math.max(title.getWidth(textRenderer), label.getWidth(textRenderer));
    }

    @Override
    public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix, VertexConsumerProvider.Immediate vertexConsumers) {
        boolean hasLabel = ClientConfig.LABELS_ENABLED.get() && !(this.label instanceof BlankComponent);
        int textBlockWidth = Math.max(0, (this.totalWidth > 0 ? this.totalWidth : getWidth(textRenderer)) - OT_ITEM_MODEL_OFFSET);

        int currentY = VERTICAL_PADDING + y;
        if (title instanceof WrappedTitleTooltipComponent wrapped) {
            for (TooltipComponent line : wrapped.getWrappedLines()) {
                int lineWidth = line.getWidth(textRenderer);
                int centerOffset = getCenterOffset(textBlockWidth, lineWidth);

                line.drawText(textRenderer, OT_ITEM_MODEL_OFFSET + x + centerOffset, currentY, matrix, vertexConsumers);
                currentY += line.getHeight();
            }
            if (hasLabel) {
                int labelWidth = label.getWidth(textRenderer);
                int labelCenterOffset = getCenterOffset(textBlockWidth, labelWidth);

                label.drawText(textRenderer, OT_ITEM_MODEL_OFFSET + x + labelCenterOffset, currentY + VERTICAL_PADDING, matrix, vertexConsumers);
            }
        } else {
            int titleWidth = title.getWidth(textRenderer);
            int centerOffset = getCenterOffset(textBlockWidth, titleWidth);

            if (!hasLabel) {
                int titleY = y + (this.totalHeight - (drawSeparator ? SEPARATOR_HEIGHT : 0) - title.getHeight()) / 2;
                title.drawText(textRenderer, OT_ITEM_MODEL_OFFSET + x + centerOffset, Math.max(currentY, titleY), matrix, vertexConsumers);
            } else {
                title.drawText(textRenderer, OT_ITEM_MODEL_OFFSET + x + centerOffset, currentY, matrix, vertexConsumers);
                int labelWidth = label.getWidth(textRenderer);
                int labelCenterOffset = getCenterOffset(textBlockWidth, labelWidth);
                label.drawText(textRenderer, OT_ITEM_MODEL_OFFSET + x + labelCenterOffset, currentY + title.getHeight() + VERTICAL_PADDING, matrix, vertexConsumers);
            }
        }
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        state.style.slot().ifPresent(it -> it.render(context, x, y + VERTICAL_PADDING, OT_ICON_SIZE, OT_ICON_SIZE));
        state.style.effects().forEach(it -> it.renderIcon(state, context, x + OT_EFFECT_OFFSET_X, y + OT_EFFECT_OFFSET_Y));
        state.style.icon().ifPresent(it -> it.render(state, context, x + OT_EFFECT_OFFSET_X, y + OT_EFFECT_OFFSET_Y));

        title.drawItems(textRenderer, OT_ITEM_MODEL_OFFSET + x, VERTICAL_PADDING + y, context);
        label.drawItems(textRenderer, OT_ITEM_MODEL_OFFSET + x, VERTICAL_PADDING + y + title.getHeight(), context);

        if (!drawSeparator) return;
        int separatorY = y + totalHeight - SEPARATOR_HEIGHT;
        int currentWidth = (this.totalWidth > 0) ? this.totalWidth : getWidth(textRenderer);
        int length = currentWidth / 2;
        ARGB edgeColor = separatorColor.withAlpha(0f);
        GraphicUtils.drawHLine(context, x, separatorY, length, edgeColor, separatorColor);
        GraphicUtils.drawHLine(context, x + length, separatorY, 1 + length, separatorColor, edgeColor);
    }
}
