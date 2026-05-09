package com.sg.dminus.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.sg.dminus.config.ConfigManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static com.sg.dminus.degrade_funcs.DegradeMath.ScaleArmor;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract double getAttributeBaseValue(RegistryEntry<EntityAttribute> attribute);

    @ModifyExpressionValue(
            method = "getArmor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D")
    )
    private double modifyBasicArmorValue(double original) {

        double base = this.getAttributeBaseValue(EntityAttributes.ARMOR);

        if (original > base && ConfigManager.get().enableArmor) {
            LivingEntity entity = (LivingEntity)(Object)this;
            if (entity instanceof PlayerEntity player) {
                return ScaleArmor(player, original, 0);
            }
        }

        return original;
    }
    @ModifyExpressionValue(
            method = "applyArmorToDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D")
    )
    private double modifyBasicArmorToughnessValue(double original) {

        double base = this.getAttributeBaseValue(EntityAttributes.ARMOR_TOUGHNESS);

        if (original > base && ConfigManager.get().enableArmor) {
            LivingEntity entity = (LivingEntity)(Object)this;
            if (entity instanceof PlayerEntity player) {
                return ScaleArmor(player, original, 1);
            }
        }

        return original;
    }
}
