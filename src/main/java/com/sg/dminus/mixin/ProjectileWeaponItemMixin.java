package com.sg.dminus.mixin;

import com.sg.dminus.config.ConfigManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

import static com.sg.dminus.degrade_components.DegradeDataComponentApply.ensureInit;
import static com.sg.dminus.degrade_components.DegradeDataComponents.PERFORMANCE_PENALTY_PERCENTAGE;
import static com.sg.dminus.degrade_funcs.DegradeMath.DegradeCalc;
import static com.sg.dminus.degrade_funcs.DegradeMath.PerformancePenaltyCalc;


@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemMixin {
    @ModifyVariable(
            method = "shoot",
            at = @At("HEAD"),
            argsOnly = true,
            index = 6
    )
    private float modifyProjectileBaseSpeed(float original,
                                            ServerLevel world,
                                            LivingEntity shooter,
                                            InteractionHand hand,
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
        return Math.max(0.1f, DegradeCalc(
                stack.getOrDefault(PERFORMANCE_PENALTY_PERCENTAGE, PerformancePenaltyCalc(stack)),
                original,
                0.1f
        ));
    }
}