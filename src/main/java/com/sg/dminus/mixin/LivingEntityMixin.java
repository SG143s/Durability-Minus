package com.sg.dminus.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.sg.dminus.config.ConfigManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Holder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static com.sg.dminus.degrade_funcs.DegradeMath.ScaleArmor;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract double getAttributeBaseValue(Holder<Attribute> attribute);

    @ModifyExpressionValue(
            method = "getArmorValue",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/core/Holder;)D")
    )
    private double modifyBasicArmorValue(double original) {

        double base = this.getAttributeBaseValue(Attributes.ARMOR);

        if (original > base && ConfigManager.get().enableArmor) {
            LivingEntity entity = (LivingEntity)(Object)this;
            if (entity instanceof Player player) {
                return ScaleArmor(player, original, 0);
            }
        }

        return original;
    }
    @ModifyExpressionValue(
            method = "getDamageAfterArmorAbsorb",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/core/Holder;)D")
    )
    private double modifyBasicArmorToughnessValue(double original) {

        double base = this.getAttributeBaseValue(Attributes.ARMOR_TOUGHNESS);

        if (original > base && ConfigManager.get().enableArmor) {
            LivingEntity entity = (LivingEntity)(Object)this;
            if (entity instanceof Player player) {
                return ScaleArmor(player, original, 1);
            }
        }

        return original;
    }
}
