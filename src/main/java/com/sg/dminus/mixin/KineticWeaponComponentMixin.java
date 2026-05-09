package com.sg.dminus.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.sg.dminus.config.ConfigManager;
import net.minecraft.component.type.KineticWeaponComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.sg.dminus.degrade_components.DegradeDataComponentApply.ensureInit;
import static com.sg.dminus.degrade_components.DegradeDataComponents.PERFORMANCE_PENALTY_PERCENTAGE;
import static com.sg.dminus.degrade_funcs.DegradeMath.DegradeCalc;
import static com.sg.dminus.degrade_funcs.DegradeMath.PerformancePenaltyCalc;


@Mixin(KineticWeaponComponent.class)
public class KineticWeaponComponentMixin {

    @ModifyExpressionValue(
            method = "usageTick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/component/type/KineticWeaponComponent;damageMultiplier:F")
    )
    private float modifyMultiplier(float original, ItemStack stack, int remainingUseTicks, LivingEntity user, EquipmentSlot slot) {
        if(!ConfigManager.get().enableSpears) {
            return original;
        }
        ensureInit(stack);
        return Math.max(0.1f, DegradeCalc(
                stack.getOrDefault(PERFORMANCE_PENALTY_PERCENTAGE, PerformancePenaltyCalc(stack)),
                original,
                0.1f
        ));
    }
}
