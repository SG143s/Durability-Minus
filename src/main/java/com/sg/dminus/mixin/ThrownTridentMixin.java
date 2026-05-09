package com.sg.dminus.mixin;

import com.sg.dminus.config.ConfigManager;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static com.sg.dminus.degrade_components.DegradeDataComponentApply.ensureInit;
import static com.sg.dminus.degrade_components.DegradeDataComponents.PERFORMANCE_PENALTY_PERCENTAGE;
import static com.sg.dminus.degrade_funcs.DegradeMath.DegradeCalc;
import static com.sg.dminus.degrade_funcs.DegradeMath.PerformancePenaltyCalc;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin {

    @ModifyConstant(
            method = "onHitEntity",
            constant = @Constant(floatValue = 8.0f)
    )
    private float modifyBaseThrownTridentDamage(float original) {
        if(!ConfigManager.get().enableTrident) {
            return original;
        }
        ThrownTrident entity = (ThrownTrident) (Object) this;
        ItemStack stack = entity.getWeaponItem();
        ensureInit(stack);
        return DegradeCalc(stack.getOrDefault(PERFORMANCE_PENALTY_PERCENTAGE, PerformancePenaltyCalc(stack)), original, 1.0f);
    }
}
