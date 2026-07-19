package com.stalemated.sts.state;

import net.minecraft.item.ItemStack;
import java.util.ArrayDeque;
import java.util.Deque;

public class TooltipContextManager {
    private static final Deque<TooltipContext> contextStack = new ArrayDeque<>();
    private static final int MAX_STACK_DEPTH = 10;

    public static void push(ItemStack itemStack) {
        if (contextStack.size() >= MAX_STACK_DEPTH) {
            clear();
        }
        contextStack.push(new TooltipContext(itemStack));
    }

    public static TooltipContext peek() {
        return contextStack.peek();
    }

    public static void pop() {
        if (!contextStack.isEmpty()) {
            contextStack.pop();
        }
    }

    public static void clear() {
        contextStack.clear();
    }
}
