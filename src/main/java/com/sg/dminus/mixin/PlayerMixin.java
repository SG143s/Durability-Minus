package com.sg.dminus.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.sg.dminus.config.ConfigManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.sg.dminus.degrade_components.DegradeDataComponentApply.ensureInit;
import static com.sg.dminus.degrade_components.DegradeDataComponents.PERFORMANCE_PENALTY_PERCENTAGE;
import static com.sg.dminus.degrade_funcs.DegradeGetValue.getWeaponBase;
import static com.sg.dminus.degrade_funcs.DegradeMath.DegradeCalc;
import static com.sg.dminus.degrade_funcs.DegradeMath.PerformancePenaltyCalc;

@Mixin(Player.class)
public class PlayerMixin {
    @ModifyExpressionValue(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D")
    )
    private double modifyAttackDamage(double original) {
        Player entity = (Player)(Object)this;
        ItemStack stack = entity.getWeaponItem();
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
