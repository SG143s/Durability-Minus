package com.sg.dminus.mixin;

import com.sg.dminus.config.ConfigManager;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.sg.dminus.degrade_components.DegradeDataComponentApply.ensureInit;
import static com.sg.dminus.degrade_components.DegradeDataComponents.PERFORMANCE_PENALTY_PERCENTAGE;
import static com.sg.dminus.degrade_funcs.DegradeMath.DegradeCalc;
import static com.sg.dminus.degrade_funcs.DegradeMath.PerformancePenaltyCalc;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin {
    @Unique
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger("durability-minus");

    @ModifyConstant(
            method = "onEntityHit",
            constant = @Constant(floatValue = 8.0f)
    )
    private float modifyBaseThrownTridentDamage(float original) {
        if(!ConfigManager.get().enableTrident) {
            return original;
        }
        TridentEntity entity = (TridentEntity) (Object) this;
        ItemStack stack = entity.getWeaponStack();
        ensureInit(stack);
        float result = DegradeCalc(stack.getOrDefault(PERFORMANCE_PENALTY_PERCENTAGE, PerformancePenaltyCalc(stack)), original, 1.0f);
        LOGGER.info("Thrown Trident Base Damage: {}", result);
        return result;
    }
}
