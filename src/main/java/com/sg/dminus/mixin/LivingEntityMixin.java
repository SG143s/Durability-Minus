package com.sg.dminus.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static com.sg.dminus.degrade_funcs.DegradeMath.ScaleArmor;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract double getAttributeBaseValue(RegistryEntry<EntityAttribute> attribute);

    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("durability-minus");
    @ModifyExpressionValue(
            method = "getArmor",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D")
    )
    private double modifyBasicArmorValue(double original) {

        double base = this.getAttributeBaseValue(EntityAttributes.ARMOR);

        if (original > base) {
            LOGGER.info("[ArmorMixin] original={}", original);
            LivingEntity entity = (LivingEntity)(Object)this;
            if (entity instanceof PlayerEntity player) {
                double total = ScaleArmor(player, original, 0);
                LOGGER.info("[ArmorMixin] total={}", total);

                return total;
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

        if (original > base) {
            LOGGER.info("[ToughnessMixin] original={}", original);
            LivingEntity entity = (LivingEntity)(Object)this;
            if (entity instanceof PlayerEntity player) {
                double total = ScaleArmor(player, original, 1);
                LOGGER.info("[ToughnessMixin] total={}", total);
                return total;
            }
        }

        return original;
    }
}
