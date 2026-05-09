package com.sg.dminus.mixin;

import com.sg.dminus.config.ConfigManager;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static com.sg.dminus.degrade_components.DegradeDataComponentApply.ensureInit;
import static com.sg.dminus.degrade_components.DegradeDataComponents.PERFORMANCE_PENALTY_PERCENTAGE;
import static com.sg.dminus.degrade_funcs.DegradeMath.DegradeCalc;
import static com.sg.dminus.degrade_funcs.DegradeMath.PerformancePenaltyCalc;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin {

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
        return DegradeCalc(stack.getOrDefault(PERFORMANCE_PENALTY_PERCENTAGE, PerformancePenaltyCalc(stack)), original, 1.0f);
    }
}
