package com.stalemated.sts.compat.tierify.component;

import com.anthonyhilyard.legendarytooltips.tooltip.ItemModelComponent;
import com.stalemated.lib.component.IndentedTextTooltipComponent;
import com.stalemated.sts.resize.TooltipStyles;
import com.stalemated.sts.resize.components.WrappedTitleTooltipComponent;
import com.stalemated.sts.state.TooltipContexts;
import org.joml.Matrix4f;

import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;

import static com.stalemated.sts.resize.TooltipLayout.TITLE_BODY_GAP;

public class LegendaryTieredWrapper implements ClientTooltipComponent {

    private final ItemModelComponent modelComponent;
    private final List<ClientTooltipComponent> titleComponents;
    private final int extraWidth;

    public LegendaryTieredWrapper(List<ClientTooltipComponent> titleComponents) {
        this.titleComponents = titleComponents;
        ItemStack currentStack = TooltipContexts.currentStack();
        this.modelComponent = new ItemModelComponent(currentStack);
        this.extraWidth = TooltipStyles.currentModelOffset();
    }

    private int getTitleHeight() {
        int totalTitleHeight = 0;
        if (titleComponents != null) {
            for (ClientTooltipComponent component : titleComponents) {
                totalTitleHeight += component.getHeight();
            }
        }
        return totalTitleHeight;
    }

    private int getFirstLineHeight() {
        if (titleComponents != null && !titleComponents.isEmpty()) {
            ClientTooltipComponent firstComp = titleComponents.get(0);
            if (firstComp instanceof WrappedTitleTooltipComponent) {
                return ((WrappedTitleTooltipComponent) firstComp).getFirstLineHeight();
            }
            return firstComp.getHeight();
        }
        return 10;
    }

    @Override
    public int getHeight() {
        int yOffset = Math.max(0, (this.extraWidth - getFirstLineHeight()) / 2);
        return Math.max(this.extraWidth, yOffset * 2 + getTitleHeight() - TITLE_BODY_GAP);
    }

    private int getCalculatedX(ClientTooltipComponent component, int x, int i) {
        int drawX = x;

        if (i == 0 || !(component instanceof IndentedTextTooltipComponent)) {
            drawX += this.extraWidth;
        }
        return drawX;
    }

    @Override
    public int getWidth(Font textRenderer) {
        int maxTitleWidth = 0;
        if (titleComponents != null) {
            for (int i = 0; i < titleComponents.size(); i++) {
                ClientTooltipComponent component = titleComponents.get(i);
                int compWidth = component.getWidth(textRenderer);
                
                if (i == 0 || !(component instanceof IndentedTextTooltipComponent)) {
                    compWidth += this.extraWidth;
                }
                maxTitleWidth = Math.max(maxTitleWidth, compWidth);
            }
        }
        return maxTitleWidth;
    }

    @Override
    public void renderText(Font textRenderer, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource vertexConsumers) {
        int yOffset = Math.max(0, (this.extraWidth - getFirstLineHeight()) / 2);
        int currentY = y + yOffset - 1;

        if (titleComponents != null) {
            for (int i = 0; i < titleComponents.size(); i++) {
                ClientTooltipComponent component = titleComponents.get(i);
                int drawX = getCalculatedX(component, x, i);

                component.renderText(textRenderer, drawX, currentY, matrix, vertexConsumers);
                currentY += component.getHeight();
            }
        }
    }

    @Override
    public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
        if (this.extraWidth > 0) {
            modelComponent.renderImage(textRenderer, x, y, context);
        }

        int yOffset = Math.max(0, (this.extraWidth - getFirstLineHeight()) / 2);
        int currentY = y + yOffset;

        if (titleComponents != null) {
            for (int i = 0; i < titleComponents.size(); i++) {
                ClientTooltipComponent component = titleComponents.get(i);
                int drawX = getCalculatedX(component, x, i);

                component.renderImage(textRenderer, drawX, currentY, context);
                currentY += component.getHeight();
            }
        }
    }
}
