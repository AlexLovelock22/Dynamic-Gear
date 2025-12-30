package org.AlexLovelock.reforged_2;

import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.AlexLovelock.reforged_2.item.ModItems;
import org.AlexLovelock.reforged_2.prefix.PrefixRegistry;
import org.AlexLovelock.reforged_2.rarity.RarityComponents;

public class Reforged_2 implements ModInitializer {

    public static final String MOD_ID = "reforged_2";

    @Override
    public void onInitialize() {
        System.out.println("[Reforged] onInitialize START");


        // Item registration
        System.out.println("[Reforged] Registering items");
        ModItems.registerAll();
        System.out.println("[Reforged] Item registration complete");

        // Prefix loading
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            System.out.println("[Reforged] Server starting - loading prefixes");
            PrefixRegistry.load(server.getResourceManager());
            System.out.println("[Reforged] Prefix system initialized");
        });

        // Data components
        System.out.println("[Reforged] Registering data components");

        RarityComponents.RARITY = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(MOD_ID, "rarity"),
                ComponentType.<String>builder()
                        .codec(Codec.STRING)
                        .build()
        );
        System.out.println("[Reforged] Registered component: rarity");

        RarityComponents.DAMAGE_PCT = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(MOD_ID, "damage_pct"),
                ComponentType.<Integer>builder()
                        .codec(Codec.INT)
                        .build()
        );
        System.out.println("[Reforged] Registered component: damage_pct");

        RarityComponents.DURABILITY_PCT = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(MOD_ID, "durability_pct"),
                ComponentType.<Integer>builder()
                        .codec(Codec.INT)
                        .build()
        );
        System.out.println("[Reforged] Registered component: durability_pct");

        RarityComponents.MINING_SPEED_PCT = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(MOD_ID, "mining_speed_pct"),
                ComponentType.<Integer>builder()
                        .codec(Codec.INT)
                        .build()
        );
        System.out.println("[Reforged] Registered component: mining_speed_pct");

        RarityComponents.MAX_HEALTH = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(MOD_ID, "max_health"),
                ComponentType.<Integer>builder()
                        .codec(Codec.INT)
                        .build()
        );
        System.out.println("[Reforged] Registered component: max_health");

        RarityComponents.PREFIX = Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(MOD_ID, "prefix"),
                ComponentType.<String>builder()
                        .codec(Codec.STRING)
                        .build()
        );
        System.out.println("[Reforged] Registered component: prefix");

        System.out.println("[Reforged] onInitialize END");
    }
}
