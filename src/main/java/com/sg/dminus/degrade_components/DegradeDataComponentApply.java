package com.sg.dminus.degrade_components;

import com.sg.dminus.degrade_data.DegradeRegistry;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;

import static com.sg.dminus.degrade_components.DegradeDataComponents.DEFAULT_DEGRADE_RATIO;
import static com.sg.dminus.degrade_components.DegradeDataComponents.DEGRADE_RATIO;

public class DegradeDataComponentApply {
    public static void apply() {
        DefaultItemComponentEvents.MODIFY.register(
                context -> {
                    context.modify(
                            item -> true,
                            (builder, item) -> {
                                if (builder.contains(DataComponentTypes.MAX_DAMAGE)) {
                                    if (!builder.contains(DEGRADE_RATIO)) {
                                        builder.add(DEGRADE_RATIO, 100f);
                                    }
                                    if (!builder.contains(DEFAULT_DEGRADE_RATIO)) {
                                        builder.add(DEFAULT_DEGRADE_RATIO, 100f);
                                    }
                                    if (!builder.contains(DegradeDataComponents.PERFORMANCE_PENALTY_PERCENTAGE)) {
                                        builder.add(DegradeDataComponents.PERFORMANCE_PENALTY_PERCENTAGE, 0f);
                                    }
                                    if (!builder.contains(DegradeDataComponents.DEFAULT_MAX_DAMAGE)) {
                                        builder.add(DegradeDataComponents.DEFAULT_MAX_DAMAGE,
                                                builder.getOrDefault(DataComponentTypes.MAX_DAMAGE, 100));
                                    }
                                }
                            }
                    );
                }
        );
    }
    public static void ensureInit(ItemStack stack) {
        float default_value = DegradeRegistry.get(stack.getItem());
        if (default_value <= 0) return;

        float storedDefault = stack.getOrDefault(DEFAULT_DEGRADE_RATIO, -1.0f);

        if (storedDefault < 0.0f) {
            stack.set(DEGRADE_RATIO, default_value);
            stack.set(DEFAULT_DEGRADE_RATIO, default_value);
            return;
        }

        if (storedDefault != default_value) {
            stack.set(DEFAULT_DEGRADE_RATIO, default_value);

            if (stack.getOrDefault(DEGRADE_RATIO, default_value) == storedDefault) {
                stack.set(DEGRADE_RATIO, default_value);
            }
        }

    }
    private DegradeDataComponentApply() {}
}
