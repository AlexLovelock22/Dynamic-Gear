package org.AlexLovelock.reforged_2;

import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.AlexLovelock.reforged_2.prefix.PrefixRegistry;
import org.AlexLovelock.reforged_2.rarity.RarityComponents;


public class Reforged_2 implements ModInitializer {

    public static final String MOD_ID = "reforged_2";

    @Override
    public void onInitialize() {


        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            PrefixRegistry.load(server.getResourceManager());
            System.out.println("[Reforged] Prefix system initialized");
        });


        RarityComponents.RARITY = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(MOD_ID, "rarity"),
                ComponentType.<String>builder()
                        .codec(Codec.STRING)
                        .build()
        );

        RarityComponents.DAMAGE_PCT = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(MOD_ID, "damage_pct"),
                ComponentType.<Integer>builder()
                        .codec(Codec.INT)
                        .build()
        );

        RarityComponents.DURABILITY_PCT = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(MOD_ID, "durability_pct"),
                ComponentType.<Integer>builder()
                        .codec(Codec.INT)
                        .build()
        );

        RarityComponents.MINING_SPEED_PCT = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(MOD_ID, "mining_speed_pct"),
                ComponentType.<Integer>builder().codec(Codec.INT).build()
        );

        RarityComponents.MAX_HEALTH = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(MOD_ID, "max_health"),
                ComponentType.<Integer>builder()
                        .codec(Codec.INT)
                        .build()
        );

        RarityComponents.PREFIX = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(MOD_ID, "prefix"),
                ComponentType.<String>builder()
                        .codec(Codec.STRING)
                        .build()
        );




    }
}
