package com.sg.dminus.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.sg.dminus.config.ConfigManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.sg.dminus.degrade_components.DegradeDataComponentApply.ensureInit;
import static com.sg.dminus.degrade_components.DegradeDataComponents.PERFORMANCE_PENALTY_PERCENTAGE;
import static com.sg.dminus.degrade_funcs.DegradeGetValue.getWeaponBase;
import static com.sg.dminus.degrade_funcs.DegradeMath.DegradeCalc;
import static com.sg.dminus.degrade_funcs.DegradeMath.PerformancePenaltyCalc;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @ModifyExpressionValue(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D")
    )
    private double modifyAttackDamage(double original) {
        PlayerEntity entity = (PlayerEntity)(Object)this;
        ItemStack stack = entity.getWeaponStack();
        if (!stack.isEmpty() && ConfigManager.get().enableMeleeWeapon) {
            double weaponBase = getWeaponBase(stack);
            if (weaponBase > 0.0) {
                ensureInit(stack);
                double scaledWeapon = DegradeCalc(stack.getOrDefault(PERFORMANCE_PENALTY_PERCENTAGE, PerformancePenaltyCalc(stack)), (float)weaponBase, 0.5f);
                scaledWeapon = Math.max(0.5, scaledWeapon);

                return original - weaponBase + scaledWeapon;
            }
        }
        return original;
    }
}
