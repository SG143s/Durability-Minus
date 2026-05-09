package com.sg.dminus.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.sg.dminus.degrade_components.DegradeDataComponentApply.ensureInit;
import static com.sg.dminus.degrade_components.DegradeDataComponents.PERFORMANCE_PENALTY_PERCENTAGE;
import static com.sg.dminus.degrade_funcs.DegradeMath.PerformancePenaltyCalc;

@Mixin(Mob.class)
public class MobMixin {

    @Redirect(
            method = "dropCustomDeathLoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;setDamageValue(I)V")
    )
    private void ensureMobDropPercentage(ItemStack stack, int damage) {
        ensureInit(stack);
        stack.setDamageValue(damage);

        stack.set(PERFORMANCE_PENALTY_PERCENTAGE, PerformancePenaltyCalc(stack));
    }

}
