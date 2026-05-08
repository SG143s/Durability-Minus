package com.sg.dminus.mixin;

import com.sg.dminus.config.ConfigManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import org.jspecify.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

import static com.sg.dminus.degrade_components.DegradeDataComponentApply.ensureInit;
import static com.sg.dminus.degrade_components.DegradeDataComponents.PERFORMANCE_PENALTY_PERCENTAGE;
import static com.sg.dminus.degrade_funcs.DegradeMath.DegradeCalc;
import static com.sg.dminus.degrade_funcs.DegradeMath.PerformancePenaltyCalc;


@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {
    @Unique
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger("durability-minus");
    @ModifyVariable(
            method = "shootAll",
            at = @At("HEAD"),
            argsOnly = true,
            index = 6
    )
    private float modifyProjectileBaseSpeed(float original,
                                            ServerWorld world,
                                            LivingEntity shooter,
                                            Hand hand,
                                            ItemStack stack,
                                            List<ItemStack> projectiles,
                                            float speed,
                                            float divergence,
                                            boolean critical,
                                            @Nullable LivingEntity target) {
        if(!ConfigManager.get().enableRangedWeapon) {
            return original;
        }
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