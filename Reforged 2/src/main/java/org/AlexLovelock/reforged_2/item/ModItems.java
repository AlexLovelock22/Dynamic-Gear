package org.AlexLovelock.reforged_2.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.AlexLovelock.reforged_2.Reforged_2;

public final class ModItems {

    public static Item REFORGE_HAMMER;

    private static final RegistryKey<Item> REFORGE_HAMMER_KEY =
            RegistryKey.of(
                    Registries.ITEM.getKey(),
                    Identifier.of(Reforged_2.MOD_ID, "reforge_hammer")
            );

    private ModItems() {}

    public static void registerAll() {
        System.out.println("[Reforged][ModItems] ===== BEGIN ITEM REGISTRATION =====");
        System.out.println("[Reforged][ModItems] Registering key: " + REFORGE_HAMMER_KEY.getValue());

        REFORGE_HAMMER = Registry.register(
                Registries.ITEM,
                REFORGE_HAMMER_KEY,
                new ReforgeHammerItem(
                        new Item.Settings()
                                .registryKey(REFORGE_HAMMER_KEY)
                                .maxDamage(64)
                )
        );

        System.out.println(
                "[Reforged][ModItems] Registered item ID: " +
                        Registries.ITEM.getId(REFORGE_HAMMER)
        );
        System.out.println("[Reforged][ModItems] ===== END ITEM REGISTRATION =====");
    }
}
