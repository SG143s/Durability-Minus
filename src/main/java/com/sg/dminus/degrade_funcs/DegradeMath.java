package com.sg.dminus.degrade_funcs;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import static com.sg.dminus.degrade_components.DegradeDataComponentApply.ensureInit;
import static com.sg.dminus.degrade_components.DegradeDataComponents.DEGRADE_RATIO;
import static com.sg.dminus.degrade_components.DegradeDataComponents.PERFORMANCE_PENALTY_PERCENTAGE;
import static com.sg.dminus.degrade_funcs.DegradeGetValue.getArmorBase;
import static com.sg.dminus.degrade_funcs.DegradeGetValue.getArmorToughBase;

public class DegradeMath {
    public static float DegradeCalc(float performance, float baseValue, float cap) {

        float new_speed = baseValue - (baseValue * performance);

        return Math.max(new_speed, cap);
    }

    public static float PerformancePenaltyCalc(ItemStack stack) {
        return PerformancePenaltyCalc(
                stack.getMaxDamage(),
                stack.getDamage(),
                stack.getOrDefault(DEGRADE_RATIO, 100f)
        );
    }

    private static float PerformancePenaltyCalc(int maxDamage, int currentDamage, float degradeRatio) {
        return (currentDamage/(float) maxDamage)/(degradeRatio/100);
    }

    public static double ScaleArmor(PlayerEntity player, double total, int type) {
        double totalScaledBase = 0.0;
        double totalBase = 0.0;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!slot.isArmorSlot()) continue;

            ItemStack stack = player.getEquippedStack(slot);
            if (stack.isEmpty()) continue;

            double base = (type == 0)
                    ? getArmorBase(stack, slot)
                    : getArmorToughBase(stack, slot);

            totalBase += base;

            ensureInit(stack);
            float penalty = stack.getOrDefault(PERFORMANCE_PENALTY_PERCENTAGE, PerformancePenaltyCalc(stack));
            totalScaledBase += DegradeCalc(penalty, (float) base, 1.0f);
        }

        total = total - totalBase + totalScaledBase;

        return total;
    }

}
