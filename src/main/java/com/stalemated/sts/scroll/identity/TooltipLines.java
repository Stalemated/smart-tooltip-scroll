package com.stalemated.sts.scroll.identity;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

import java.util.ArrayList;
import java.util.List;

public record TooltipLines(List<Line> lines) {
    public static final TooltipLines NONE = new TooltipLines(List.of());

    public record Line(int width, int height) {
    }

    public static TooltipLines measure(List<ClientTooltipComponent> components, Font font) {
        List<Line> lines = new ArrayList<>(components.size());
        for (ClientTooltipComponent component : components) lines.add(new Line(component.getWidth(font), component.getHeight()));
        return new TooltipLines(lines);
    }

    public boolean sameHeights(TooltipLines other) {
        if (lines.size() != other.lines.size()) return false;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).height() != other.lines.get(i).height()) return false;
        }
        return true;
    }
}
