package com.sg.dminus.degrade_data;

import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class DegradeRegistry {
    private static final Map<Item, Float> DEGRADE_MAP = new HashMap<>();

    public static void clear() {
        DEGRADE_MAP.clear();
    }

    public static void register(Item item, Float value) {
        DEGRADE_MAP.put(item, value);
    }

    public static float get(Item item) {
        return DEGRADE_MAP.getOrDefault(item, 0.0f);
    }
}
