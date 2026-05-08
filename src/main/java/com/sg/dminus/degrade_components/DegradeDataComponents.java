package com.sg.dminus.degrade_components;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class DegradeDataComponents {
    public static final ComponentType<Float> DEGRADE_RATIO =
            Registry.register(
                    Registries.DATA_COMPONENT_TYPE,
                    Identifier.of("durability_minus", "degrade_ratio"),
                    ComponentType.<Float>builder()
                            .codec(Codec.FLOAT)
                            .packetCodec(PacketCodecs.FLOAT)
                            .build()
            );
    public static final ComponentType<Float> DEFAULT_DEGRADE_RATIO =
            Registry.register(
                    Registries.DATA_COMPONENT_TYPE,
                    Identifier.of("durability_minus", "default_degrade_ratio"),
                    ComponentType.<Float>builder()
                            .codec(Codec.FLOAT)
                            .packetCodec(PacketCodecs.FLOAT)
                            .build()
            );
    public static final ComponentType<Float> PERFORMANCE_PENALTY_PERCENTAGE =
            Registry.register(
                    Registries.DATA_COMPONENT_TYPE,
                    Identifier.of("durability_minus", "performance_percentage"),
                    ComponentType.<Float>builder()
                            .codec(Codec.FLOAT)
                            .packetCodec(PacketCodecs.FLOAT)
                            .build()
            );
    public static final ComponentType<Integer> DEFAULT_MAX_DAMAGE =
            Registry.register(
                    Registries.DATA_COMPONENT_TYPE,
                    Identifier.of("durability_minus","default_max_damage"),
                    ComponentType.<Integer>builder()
                            .codec(Codec.INT)
                            .packetCodec(PacketCodecs.INTEGER)
                            .build()
            );

    private DegradeDataComponents() {}

    public static void init() {}
}
