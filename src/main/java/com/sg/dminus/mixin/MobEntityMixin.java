package com.sg.dminus.mixin;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.sg.dminus.degrade_components.DegradeDataComponentApply.ensureInit;
import static com.sg.dminus.degrade_components.DegradeDataComponents.PERFORMANCE_PENALTY_PERCENTAGE;
import static com.sg.dminus.degrade_funcs.DegradeMath.PerformancePenaltyCalc;

@Mixin(MobEntity.class)
public class MobEntityMixin {

    @Redirect(
            method = "dropEquipment",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;setDamage(I)V")
    )
    private void ensureMobDropPercentage(ItemStack stack, int damage) {
        ensureInit(stack);
        stack.setDamage(damage);

        stack.set(PERFORMANCE_PENALTY_PERCENTAGE, PerformancePenaltyCalc(stack));
    }

}
