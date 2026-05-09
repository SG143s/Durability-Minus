package com.sg.dminus.degrade_funcs;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Holder;

public class DegradeGetValue {
    public static double getWeaponBase(ItemStack stack) {
        double weaponBase = 0.0;
        ItemAttributeModifiers component = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (component != null) {
            for (var entry : component.modifiers()) {
                Holder<Attribute> attribute = entry.attribute();
                AttributeModifier modifier = entry.modifier();

                if (attribute.is(Attributes.ATTACK_DAMAGE.unwrapKey().get()) &&
                entry.slot().test(EquipmentSlot.MAINHAND)) {
                    if (modifier.is(Item.BASE_ATTACK_DAMAGE_ID)) {
                        weaponBase += modifier.amount();
                    }
                }
            }
        }
        return Math.max(0.0, weaponBase);

    }

    public static double getArmorBase(ItemStack stack, EquipmentSlot slot) {
        double armorBase = 0.0;

        ItemAttributeModifiers component = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (component == null) return 0.0;

        for (var entry : component.modifiers()) {
            if (!entry.slot().test(slot)) continue;

            var attribute = entry.attribute();
            var modifier = entry.modifier();

            if (!attribute.is(Attributes.ARMOR.unwrapKey().get())) continue;
            if (modifier.operation() != AttributeModifier.Operation.ADD_VALUE) continue;

            armorBase += modifier.amount();
        }

        return Math.max(0.0, armorBase);
    }

    public static double getArmorToughBase(ItemStack stack, EquipmentSlot slot) {
        double armorBase = 0.0;

        ItemAttributeModifiers component = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (component == null) return 0.0;

        for (var entry : component.modifiers()) {
            if (!entry.slot().test(slot)) continue;

            var attribute = entry.attribute();
            var modifier = entry.modifier();

            if (!attribute.is(Attributes.ARMOR_TOUGHNESS.unwrapKey().get())) continue;
            if (modifier.operation() != AttributeModifier.Operation.ADD_VALUE) continue;

            armorBase += modifier.amount();
        }

        return Math.max(0.0, armorBase);
    }
}
