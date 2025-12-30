package org.AlexLovelock.reforged_2.prefix;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.AlexLovelock.reforged_2.rarity.ItemCategory;

import java.io.InputStreamReader;
import java.util.*;

public final class PrefixRegistry {

    private static final Gson GSON = new Gson();

    private static final Map<String, PrefixDefinition> BY_ID = new HashMap<>();
    private static final List<PrefixDefinition> ALL = new ArrayList<>();

    private PrefixRegistry() {
    }

    public static void clear() {
        BY_ID.clear();
        ALL.clear();
    }

    public static PrefixDefinition get(String id) {
        if (id == null) return null;
        return BY_ID.get(id);
    }

    public static PrefixDefinition getRandom(Random random) {
        if (ALL.isEmpty()) return null;
        return ALL.get(random.nextInt(ALL.size()));
    }




    /**
     * Called once server resources are available
     */
    public static void load(ResourceManager manager) {
        clear();

        // Looks inside: data/reforged_2/prefixes/*.json
        Map<Identifier, Resource> resources =
                manager.findResources(
                        "prefixes",
                        id -> id.getNamespace().equals("reforged_2")
                                && id.getPath().endsWith(".json")
                );

        for (Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
            Identifier id = entry.getKey();
            Resource resource = entry.getValue();

            try (InputStreamReader reader =
                         new InputStreamReader(resource.getInputStream())) {

                JsonObject obj = GSON.fromJson(reader, JsonObject.class);
                PrefixDefinition def = parse(obj);

                if (def != null) {
                    BY_ID.put(def.id(), def);
                    ALL.add(def);
                    System.out.println("[Reforged][PrefixRegistry] Loaded " + id);
                }

            } catch (Exception e) {
                System.out.println("[Reforged][PrefixRegistry] Failed loading " + id);
                e.printStackTrace();
            }
        }

        System.out.println("[Reforged][PrefixRegistry] Loaded prefixes: " + ALL.size());
    }

    private static PrefixDefinition parse(JsonObject obj) {
        if (obj == null) return null;

        String id = getString(obj, "id");
        String displayName = getString(obj, "displayName");

        if (id == null || id.isBlank()) return null;
        if (displayName == null || displayName.isBlank()) {
            displayName = id;
        }

        Set<ItemCategory> categories = EnumSet.noneOf(ItemCategory.class);
        if (obj.has("categories")) {
            for (var el : obj.getAsJsonArray("categories")) {
                categories.add(ItemCategory.valueOf(el.getAsString()));
            }
        }
        int damagePct = getInt(obj, "damagePct", 0);
        int attackSpeedPct = getInt(obj, "attackSpeedPct", 0);
        int movementSpeedPct = getInt(obj, "movementSpeedPct", 0);
        int reachBonus = getInt(obj, "reachBonus", 0);
        int critChancePct = getInt(obj, "critChancePct", 0);
        int bonusXpChancePct = getInt(obj, "bonusXpChancePct", 0);

        int armorBonus = getInt(obj, "armorBonus", 0);
        int armorToughnessBonus = getInt(obj, "armorToughnessBonus", 0);
        int maxHealthBonus = getInt(obj, "maxHealthBonus", 0);

        return new PrefixDefinition(
                id,
                displayName,
                categories,
                damagePct,
                attackSpeedPct,
                movementSpeedPct,
                reachBonus,
                critChancePct,
                bonusXpChancePct,
                armorBonus,
                armorToughnessBonus,
                maxHealthBonus
        );

    }

    private static String getString(JsonObject obj, String key) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) return null;
        return obj.get(key).getAsString();
    }

    private static int getInt(JsonObject obj, String key, int fallback) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) return fallback;
        return obj.get(key).getAsInt();
    }

    public static List<PrefixDefinition> getAll() {
        return List.copyOf(ALL);
    }
}
