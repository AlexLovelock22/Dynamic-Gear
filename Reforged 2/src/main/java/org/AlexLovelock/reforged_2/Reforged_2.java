// src/main/java/org/AlexLovelock/reforged_2/Reforged_2.java
package org.AlexLovelock.reforged_2;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.AlexLovelock.reforged_2.item.ModItems;
import org.AlexLovelock.reforged_2.prefix.PrefixRegistry;
import org.AlexLovelock.reforged_2.rarity.RarityComponents;

public class Reforged_2 implements ModInitializer {

    public static final String MOD_ID = "reforged_2";

    @Override
    public void onInitialize() {
        System.out.println("[Reforged] onInitialize START");

        // Register components first (must be the same on client + server)
        RarityComponents.register();

        // Register items
        ModItems.registerAll();

        // Load prefix JSONs on server start
        ServerLifecycleEvents.SERVER_STARTING.register(server ->
                PrefixRegistry.load(server.getResourceManager())
        );

        System.out.println("[Reforged] onInitialize END");
    }
}
