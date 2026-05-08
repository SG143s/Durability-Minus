package com.sg.dminus.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.component.type.KineticWeaponComponent;
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
import static com.sg.dminus.degrade_funcs.DegradeMath.DegradeCalc;
import static com.sg.dminus.degrade_funcs.DegradeMath.PerformancePenaltyCalc;


@Mixin(KineticWeaponComponent.class)
public class KineticWeaponComponentMixin {
    @Unique
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger("durability-minus");

    @ModifyExpressionValue(
            method = "usageTick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/component/type/KineticWeaponComponent;damageMultiplier:F")
    )
    private float modifyMultiplier(float original, ItemStack stack, int remainingUseTicks, LivingEntity user, EquipmentSlot slot) {
        ensureInit(stack);
        float result = Math.max(0.1f, DegradeCalc(
                stack.getOrDefault(PERFORMANCE_PENALTY_PERCENTAGE, PerformancePenaltyCalc(stack)),
                original,
                0.1f
        ));
        LOGGER.info("Result: {}", result );
        return result;
    }
}
