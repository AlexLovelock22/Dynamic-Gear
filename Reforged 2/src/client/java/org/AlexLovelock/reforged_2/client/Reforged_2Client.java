// src/client/java/org/AlexLovelock/reforged_2/client/Reforged_2Client.java
package org.AlexLovelock.reforged_2.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.AlexLovelock.reforged_2.Reforged_2;
import org.AlexLovelock.reforged_2.prefix.PrefixRegistry;

public class Reforged_2Client implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        System.out.println("[Reforged][Client] Registering prefix reload listener");

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
                .registerReloadListener(new SimpleSynchronousResourceReloadListener() {

                    @Override
                    public Identifier getFabricId() {
                        return Identifier.of(Reforged_2.MOD_ID, "prefix_registry");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        System.out.println("[Reforged][Client] Reloading PrefixRegistry");
                        PrefixRegistry.load(manager);
                    }
                });
    }
}
