package com.sg.dminus.mixin;

import com.sg.dminus.config.ConfigManager;
import net.minecraft.component.type.PiercingWeaponComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.sg.dminus.degrade_components.DegradeDataComponentApply.ensureInit;
import static com.sg.dminus.degrade_components.DegradeDataComponents.PERFORMANCE_PENALTY_PERCENTAGE;
import static com.sg.dminus.degrade_funcs.DegradeGetValue.getWeaponBase;
import static com.sg.dminus.degrade_funcs.DegradeMath.DegradeCalc;
import static com.sg.dminus.degrade_funcs.DegradeMath.PerformancePenaltyCalc;

@Mixin(PiercingWeaponComponent.class)
public class PiercingWeaponComponentMixin {
    @Unique
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger("durability-minus");
    @ModifyVariable(
            method = "stab",
            at = @At("STORE"),
            ordinal = 0
    )
    private float modifyBaseAttack(float original, LivingEntity attacker, EquipmentSlot slot) {
        ItemStack stack = attacker.getWeaponStack();
        if (!stack.isEmpty() && ConfigManager.get().enableSpears) {
            float weaponBase = (float) getWeaponBase(stack);
            if (weaponBase > 0.0f) {
                ensureInit(stack);
                float scaledWeapon = DegradeCalc(stack.getOrDefault(PERFORMANCE_PENALTY_PERCENTAGE, PerformancePenaltyCalc(stack)), weaponBase, 0.5f);
                scaledWeapon = Math.max(0.5f, scaledWeapon);

                float result = original - weaponBase + scaledWeapon;
                LOGGER.info("Result: {}", result);
                return result;
            }
        }
        return original;
    }
}
