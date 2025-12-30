package org.AlexLovelock.reforged_2.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.AlexLovelock.reforged_2.Reforged_2;

public final class ModItems {

    public static Item IRON_HAMMER;
    public static Item DIAMOND_HAMMER;
    public static Item NETHERITE_HAMMER;

    private static final RegistryKey<Item> IRON_HAMMER_KEY =
            RegistryKey.of(Registries.ITEM.getKey(),
                    Identifier.of(Reforged_2.MOD_ID, "iron_hammer"));

    private static final RegistryKey<Item> DIAMOND_HAMMER_KEY =
            RegistryKey.of(Registries.ITEM.getKey(),
                    Identifier.of(Reforged_2.MOD_ID, "diamond_hammer"));

    private static final RegistryKey<Item> NETHERITE_HAMMER_KEY =
            RegistryKey.of(Registries.ITEM.getKey(),
                    Identifier.of(Reforged_2.MOD_ID, "netherite_hammer"));

    private ModItems() {}

    public static void registerAll() {
        registerItems();
        registerCreativeTabEntries();
    }

    private static void registerItems() {

        IRON_HAMMER = Registry.register(
                Registries.ITEM,
                IRON_HAMMER_KEY,
                new ReforgeHammerItem(
                        ReforgeHammerItem.HammerTier.IRON,
                        new Item.Settings()
                                .registryKey(IRON_HAMMER_KEY)
                                .maxDamage(250)
                )
        );

        DIAMOND_HAMMER = Registry.register(
                Registries.ITEM,
                DIAMOND_HAMMER_KEY,
                new ReforgeHammerItem(
                        ReforgeHammerItem.HammerTier.DIAMOND,
                        new Item.Settings()
                                .registryKey(DIAMOND_HAMMER_KEY)
                                .maxDamage(500)
                )
        );

        NETHERITE_HAMMER = Registry.register(
                Registries.ITEM,
                NETHERITE_HAMMER_KEY,
                new ReforgeHammerItem(
                        ReforgeHammerItem.HammerTier.NETHERITE,
                        new Item.Settings()
                                .registryKey(NETHERITE_HAMMER_KEY)
                                .maxDamage(1000)
                                .fireproof()
                )
        );
    }

    private static void registerCreativeTabEntries() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(IRON_HAMMER);
            entries.add(DIAMOND_HAMMER);
            entries.add(NETHERITE_HAMMER);
        });
    }
}
