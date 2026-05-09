package com.sg.dminus.mixin;

import com.sg.dminus.config.ConfigManager;
import net.minecraft.world.item.component.PiercingWeapon;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.sg.dminus.degrade_components.DegradeDataComponentApply.ensureInit;
import static com.sg.dminus.degrade_components.DegradeDataComponents.PERFORMANCE_PENALTY_PERCENTAGE;
import static com.sg.dminus.degrade_funcs.DegradeGetValue.getWeaponBase;
import static com.sg.dminus.degrade_funcs.DegradeMath.DegradeCalc;
import static com.sg.dminus.degrade_funcs.DegradeMath.PerformancePenaltyCalc;

@Mixin(PiercingWeapon.class)
public class PiercingWeaponMixin {
    @ModifyVariable(
            method = "attack",
            at = @At("STORE"),
            ordinal = 0
    )
    private float modifyBaseAttack(float original, LivingEntity attacker, EquipmentSlot slot) {
        ItemStack stack = attacker.getWeaponItem();
        if (!stack.isEmpty() && ConfigManager.get().enableSpears) {
            float weaponBase = (float) getWeaponBase(stack);
            if (weaponBase > 0.0f) {
                ensureInit(stack);
                float scaledWeapon = DegradeCalc(stack.getOrDefault(PERFORMANCE_PENALTY_PERCENTAGE, PerformancePenaltyCalc(stack)), weaponBase, 0.5f);
                scaledWeapon = Math.max(0.5f, scaledWeapon);

                return original - weaponBase + scaledWeapon;
            }
        }
        return original;
    }
}
