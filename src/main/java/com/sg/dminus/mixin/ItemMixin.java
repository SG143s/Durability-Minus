package com.sg.dminus.mixin;

import com.sg.dminus.config.ConfigManager;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.sg.dminus.degrade_components.DegradeDataComponentApply.ensureInit;

import static com.sg.dminus.degrade_components.DegradeDataComponents.PERFORMANCE_PENALTY_PERCENTAGE;
import static com.sg.dminus.degrade_funcs.DegradeMath.DegradeCalc;
import static com.sg.dminus.degrade_funcs.DegradeMath.PerformancePenaltyCalc;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(
            method = "getMiningSpeed",
            at = @At("RETURN"),
            cancellable = true
    )
    private void degradingMiningSpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
        if(!ConfigManager.get().enableTools) {
            return;
        }

        if (stack.isDamageable()) {
            float baseSpeed = cir.getReturnValue();
            if (baseSpeed > 1.0f) {
                ensureInit(stack);
                float performance_penalty = stack.getOrDefault(PERFORMANCE_PENALTY_PERCENTAGE, PerformancePenaltyCalc(stack));


                float newSpeed = DegradeCalc(performance_penalty, baseSpeed, 1.1f);

                cir.setReturnValue(newSpeed);
            }
        }
    }
}
