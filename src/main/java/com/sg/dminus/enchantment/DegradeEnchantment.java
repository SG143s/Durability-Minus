package com.sg.dminus.enchantment;

import com.sg.dminus.DurabilityMinus;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.MultiplyValue;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.Identifier;

import java.util.List;

public class DegradeEnchantment {

    public static final ResourceKey<Enchantment> RESILIENCE =
            ResourceKey.create(
                    Registries.ENCHANTMENT,
                    Identifier.fromNamespaceAndPath(DurabilityMinus.MOD_ID, "resilience")
            );

    public static void bootstrap(BootstrapContext<Enchantment> registerable) {

        var enchantments =
                registerable.lookup(
                        Registries.ENCHANTMENT
                );

        var items =
                registerable.lookup(
                        Registries.ITEM
                );

        register(
                registerable,
                RESILIENCE,

                Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(
                                        ItemTags.DURABILITY_ENCHANTABLE
                                ),

                                5,

                                3,

                                Enchantment.dynamicCost(5, 8),

                                Enchantment.dynamicCost(55, 8),

                                2,

                                EquipmentSlotGroup.ANY
                        )
                )
        );
    }

    private static void register(
            BootstrapContext<Enchantment> registry,
            ResourceKey<Enchantment> key,
            Enchantment.Builder builder
    ) {

        registry.register(
                key,
                builder.build(key.identifier())
        );
    }
}