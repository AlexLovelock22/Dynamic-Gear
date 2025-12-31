// src/main/java/org/AlexLovelock/reforged_2/rarity/RarityComponents.java
package org.AlexLovelock.reforged_2.rarity;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.AlexLovelock.reforged_2.Reforged_2;

public final class RarityComponents {

    private RarityComponents() {}

    public static ComponentType<String> RARITY;
    public static ComponentType<String> PREFIX;

    public static ComponentType<Integer> DAMAGE_PCT;
    public static ComponentType<Integer> DURABILITY_PCT;
    public static ComponentType<Integer> MINING_SPEED_PCT;
    public static ComponentType<Integer> MAX_HEALTH;

    public static void register() {
        // Strings
        RARITY = register("rarity", Codec.STRING, PacketCodecs.STRING.cast());
        PREFIX = register("prefix", Codec.STRING, PacketCodecs.STRING.cast());

        // Ints
        DAMAGE_PCT = register("damage_pct", Codec.INT, PacketCodecs.INTEGER.cast());
        DURABILITY_PCT = register("durability_pct", Codec.INT, PacketCodecs.INTEGER.cast());
        MINING_SPEED_PCT = register("mining_speed_pct", Codec.INT, PacketCodecs.INTEGER.cast());
        MAX_HEALTH = register("max_health", Codec.INT, PacketCodecs.INTEGER.cast());
    }

    private static <T> ComponentType<T> register(
            String id,
            Codec<T> codec,
            PacketCodec<? super RegistryByteBuf, T> packetCodec
    ) {
        return Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(Reforged_2.MOD_ID, id),
                ComponentType.<T>builder()
                        .codec(codec)
                        .packetCodec(packetCodec)
                        .build()
        );
    }
}
