// TODO(Ravel): Failed to fully resolve file: null cannot be cast to non-null type com.intellij.psi.PsiJavaCodeReferenceElement
package com.sg.dminus.degrade_funcs;

import com.sg.dminus.config.DegradeConfig;
import com.sg.dminus.enchantment.DegradeEnchantment;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Holder;

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
        float penalty = PerformancePenaltyCalc(
                stack.getMaxDamage(),
                stack.getDamageValue(),
                stack.getOrDefault(DEGRADE_RATIO, DegradeConfig.defaultRatio)
        );
        float multiplier = 1.0f;
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : stack.getEnchantments().entrySet()) {
            if (entry.getKey().is(DegradeEnchantment.RESILIENCE)) {
                multiplier = switch (entry.getIntValue()) {
                    case 1 -> DegradeConfig.resilienceLevelModifier[0];
                    case 2 -> DegradeConfig.resilienceLevelModifier[1];
                    default -> DegradeConfig.resilienceLevelModifier[2];
                };
                break;
            }
        }
        return penalty * multiplier;
    }

    private static float PerformancePenaltyCalc(int maxDamage, int currentDamage, float degradeRatio) {
        return (currentDamage/(float) maxDamage)/(degradeRatio/100);
    }

    public static double ScaleArmor(Player player, double total, int type) {
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
