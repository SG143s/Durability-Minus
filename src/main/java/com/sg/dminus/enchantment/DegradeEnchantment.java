package com.sg.dminus.enchantment;

import com.sg.dminus.DurabilityMinus;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.value.MultiplyEnchantmentEffect;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

import java.util.List;

public class DegradeEnchantment {

    public static final RegistryKey<Enchantment> RESILIENCE =
            RegistryKey.of(
                    RegistryKeys.ENCHANTMENT,
                    Identifier.of(DurabilityMinus.MOD_ID, "resilience")
            );

    public static void bootstrap(Registerable<Enchantment> registerable) {

        var enchantments =
                registerable.getRegistryLookup(
                        RegistryKeys.ENCHANTMENT
                );

        var items =
                registerable.getRegistryLookup(
                        RegistryKeys.ITEM
                );

        register(
                registerable,
                RESILIENCE,

                Enchantment.builder(
                        Enchantment.definition(
                                items.getOrThrow(
                                        ItemTags.DURABILITY_ENCHANTABLE
                                ),

                                5,

                                3,

                                Enchantment.leveledCost(5, 8),

                                Enchantment.leveledCost(55, 8),

                                2,

                                AttributeModifierSlot.ANY
                        )
                )
        );
    }

    private static void register(
            Registerable<Enchantment> registry,
            RegistryKey<Enchantment> key,
            Enchantment.Builder builder
    ) {

        registry.register(
                key,
                builder.build(key.getValue())
        );
    }
}