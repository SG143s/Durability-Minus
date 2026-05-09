package com.sg.dminus.degrade_funcs;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

public class DegradeGetValue {
    public static double getWeaponBase(ItemStack stack) {
        double weaponBase = 0.0;
        AttributeModifiersComponent component = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        if (component != null) {
            for (var entry : component.modifiers()) {
                RegistryEntry<EntityAttribute> attribute = entry.attribute();
                EntityAttributeModifier modifier = entry.modifier();

                if (attribute.matchesKey(EntityAttributes.ATTACK_DAMAGE.getKey().get()) &&
                entry.slot().matches(EquipmentSlot.MAINHAND)) {
                    if (modifier.idMatches(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID)) {
                        weaponBase += modifier.value();
                    }
                }
            }
        }
        return Math.max(0.0, weaponBase);

    }

    public static double getArmorBase(ItemStack stack, EquipmentSlot slot) {
        double armorBase = 0.0;

        AttributeModifiersComponent component = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        if (component == null) return 0.0;

        for (var entry : component.modifiers()) {
            if (!entry.slot().matches(slot)) continue;

            var attribute = entry.attribute();
            var modifier = entry.modifier();

            if (!attribute.matchesKey(EntityAttributes.ARMOR.getKey().get())) continue;
            if (modifier.operation() != EntityAttributeModifier.Operation.ADD_VALUE) continue;

            armorBase += modifier.value();
        }

        return Math.max(0.0, armorBase);
    }

    public static double getArmorToughBase(ItemStack stack, EquipmentSlot slot) {
        double armorBase = 0.0;

        AttributeModifiersComponent component = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        if (component == null) return 0.0;

        for (var entry : component.modifiers()) {
            if (!entry.slot().matches(slot)) continue;

            var attribute = entry.attribute();
            var modifier = entry.modifier();

            if (!attribute.matchesKey(EntityAttributes.ARMOR_TOUGHNESS.getKey().get())) continue;
            if (modifier.operation() != EntityAttributeModifier.Operation.ADD_VALUE) continue;

            armorBase += modifier.value();
        }

        return Math.max(0.0, armorBase);
    }
}
