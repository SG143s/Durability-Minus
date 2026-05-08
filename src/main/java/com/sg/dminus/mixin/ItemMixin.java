package com.sg.dminus.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.sg.dminus.degrade_components.DegradeDataComponentApply.ensureInit;
import static com.sg.dminus.degrade_components.DegradeDataComponents.DEGRADE_RATIO;
import static com.sg.dminus.degrade_components.DegradeDataComponents.PERFORMANCE_PENALTY_PERCENTAGE;
import static com.sg.dminus.degrade_funcs.DegradeMath.DegradeCalc;
import static com.sg.dminus.degrade_funcs.DegradeMath.PerformancePenaltyCalc;

@Mixin(Item.class)
public class ItemMixin {
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("durability-minus");
    @Inject(
            method = "getMiningSpeed",
            at = @At("RETURN"),
            cancellable = true
    )
    private void degradingMiningSpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {

        if (stack.isDamageable()) {
            float baseSpeed = cir.getReturnValue();
            if (baseSpeed > 1.0f) {
                ensureInit(stack);
                float performance_penalty = stack.getOrDefault(PERFORMANCE_PENALTY_PERCENTAGE, PerformancePenaltyCalc(stack));


                float newSpeed = DegradeCalc(performance_penalty, baseSpeed, 1.1f);
                LOGGER.info("Speed {}", String.valueOf(newSpeed));
                LOGGER.info("Penalty {}", String.valueOf(stack.getOrDefault(PERFORMANCE_PENALTY_PERCENTAGE,0)));
                LOGGER.info("Ratio {}", String.valueOf(stack.getOrDefault(DEGRADE_RATIO,0)));
                cir.setReturnValue(newSpeed);
            }
        }
    }
}
