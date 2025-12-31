package org.AlexLovelock.reforged_2.rarity;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import org.AlexLovelock.reforged_2.Reforged_2;
import org.AlexLovelock.reforged_2.prefix.PrefixDefinition;
import org.AlexLovelock.reforged_2.prefix.PrefixRegistry;
import java.util.List;




public final class RarityHelper {

    private RarityHelper() {
    }

    public static boolean isSword(ItemStack stack) {
        return stack.isOf(Items.WOODEN_SWORD)
                || stack.isOf(Items.STONE_SWORD)
                || stack.isOf(Items.IRON_SWORD)
                || stack.isOf(Items.GOLDEN_SWORD)
                || stack.isOf(Items.DIAMOND_SWORD)
                || stack.isOf(Items.NETHERITE_SWORD);
    }

    public static boolean isAxe(ItemStack stack){
        return stack.isOf(Items.WOODEN_AXE)
                || stack.isOf(Items.STONE_AXE)
                || stack.isOf(Items.IRON_AXE)
                || stack.isOf(Items.GOLDEN_AXE)
                || stack.isOf(Items.DIAMOND_AXE)
                || stack.isOf(Items.NETHERITE_AXE);
    }

    public static boolean isTool(ItemStack stack){
        return stack.isOf(Items.WOODEN_AXE)
                || stack.isOf(Items.STONE_AXE)
                || stack.isOf(Items.IRON_AXE)
                || stack.isOf(Items.GOLDEN_AXE)
                || stack.isOf(Items.DIAMOND_AXE)
                || stack.isOf(Items.NETHERITE_AXE)
                || stack.isOf(Items.WOODEN_PICKAXE)
                || stack.isOf(Items.STONE_PICKAXE)
                || stack.isOf(Items.IRON_PICKAXE)
                || stack.isOf(Items.GOLDEN_PICKAXE)
                || stack.isOf(Items.DIAMOND_PICKAXE)
                || stack.isOf(Items.NETHERITE_PICKAXE)
                || stack.isOf(Items.WOODEN_SHOVEL)
                || stack.isOf(Items.STONE_SHOVEL)
                || stack.isOf(Items.IRON_SHOVEL)
                || stack.isOf(Items.GOLDEN_SHOVEL)
                || stack.isOf(Items.DIAMOND_SHOVEL)
                || stack.isOf(Items.NETHERITE_SHOVEL);
    }

    public static boolean isArmor(ItemStack stack){
        return stack.isOf(Items.LEATHER_BOOTS)
                || stack.isOf(Items.LEATHER_HELMET)
                || stack.isOf(Items.LEATHER_CHESTPLATE)
                || stack.isOf(Items.LEATHER_LEGGINGS)

                || stack.isOf(Items.GOLDEN_BOOTS)
                || stack.isOf(Items.GOLDEN_LEGGINGS)
                || stack.isOf(Items.GOLDEN_HELMET)
                || stack.isOf(Items.GOLDEN_CHESTPLATE)

                || stack.isOf(Items.IRON_BOOTS)
                || stack.isOf(Items.IRON_LEGGINGS)
                || stack.isOf(Items.IRON_HELMET)
                || stack.isOf(Items.IRON_CHESTPLATE)

                || stack.isOf(Items.DIAMOND_BOOTS)
                || stack.isOf(Items.DIAMOND_LEGGINGS)
                || stack.isOf(Items.DIAMOND_HELMET)
                || stack.isOf(Items.DIAMOND_CHESTPLATE)

                || stack.isOf(Items.NETHERITE_BOOTS)
                || stack.isOf(Items.NETHERITE_LEGGINGS)
                || stack.isOf(Items.NETHERITE_HELMET)
                || stack.isOf(Items.NETHERITE_CHESTPLATE)

                || stack.isOf(Items.CHAINMAIL_BOOTS)
                || stack.isOf(Items.CHAINMAIL_LEGGINGS)
                || stack.isOf(Items.CHAINMAIL_HELMET)
                || stack.isOf(Items.CHAINMAIL_CHESTPLATE);
    }

    public static boolean hasRarity(ItemStack stack) {
        return stack.contains(RarityComponents.RARITY);
    }

    public static void assignRarityIfMissing(ItemStack stack, Random random) {

//        System.out.println(
//                "[Reforged][Rarity] assignRarityIfMissing called for " + stack.getItem()
//        );

        boolean armor = isArmor(stack);

        boolean hasRarity = hasRarity(stack);
        boolean hasPrefix = stack.contains(RarityComponents.PREFIX);

// If the item already has both rarity and prefix, do nothing
        if (hasRarity && hasPrefix) {
            //System.out.println("[Reforged][Rarity] Already fully initialized, skipping");
            return;
        }

        boolean sword = isSword(stack);
        boolean axe = isAxe(stack);
        boolean tool = isTool(stack);

//        System.out.println(
//                "[Reforged][Rarity] Type check -> sword=" + sword
//                        + " axe=" + axe
//                        + " tool=" + tool
//                        + " armor=" + armor
//        );

        if (!sword && !axe && !tool && !armor) {
            //System.out.println("[Reforged][Rarity] Unsupported item, skipping");
            return;
        }

        Rarity rarity = rollRarity(random);
        System.out.println("[Reforged][Rarity] Rolled rarity = " + rarity);

        int durabilityPct = roll(
                RarityRanges.getSwordDurabilityMin(rarity),
                RarityRanges.getSwordDurabilityMax(rarity),
                random
        );

        // ---------------- ARMOR ----------------
        // ARMOR
        if (armor) {
            int maxHealth =
                    (rarity == Rarity.EPIC || rarity == Rarity.LEGENDARY) ? 1 : 0;

            System.out.println(
                    "[Reforged][Armor] Applying armor rarity. maxHealth=" + maxHealth
            );

            stack.set(RarityComponents.RARITY, rarity.name());
            stack.set(RarityComponents.DURABILITY_PCT, durabilityPct);
            stack.set(RarityComponents.MAX_HEALTH, maxHealth);

        }

        // ---------------- AXE ----------------
        if (axe) {
            boolean treatAsWeapon = random.nextBoolean();

            System.out.println(
                    "[Reforged][Axe] Treat as weapon = " + treatAsWeapon
            );

            if (treatAsWeapon) {
                int damagePct = roll(
                        RarityRanges.getSwordDamageMin(rarity),
                        RarityRanges.getSwordDamageMax(rarity),
                        random
                );
                stack.set(RarityComponents.DAMAGE_PCT, damagePct);
            } else {
                int miningSpeedPct = roll(
                        RarityRanges.getToolSpeedMin(rarity),
                        RarityRanges.getToolSpeedMax(rarity),
                        random
                );
                stack.set(RarityComponents.MINING_SPEED_PCT, miningSpeedPct);
            }
        }
        // ---------------- SWORD ----------------
        else if (sword) {
            int damagePct = roll(
                    RarityRanges.getSwordDamageMin(rarity),
                    RarityRanges.getSwordDamageMax(rarity),
                    random
            );
            stack.set(RarityComponents.DAMAGE_PCT, damagePct);
        }
        // ---------------- TOOL ----------------
        else if (tool) {
            int miningSpeedPct = roll(
                    RarityRanges.getToolSpeedMin(rarity),
                    RarityRanges.getToolSpeedMax(rarity),
                    random
            );
            stack.set(RarityComponents.MINING_SPEED_PCT, miningSpeedPct);
        }

        // ---------------- COMMON DATA ----------------
        stack.set(RarityComponents.RARITY, rarity.name());
        stack.set(RarityComponents.DURABILITY_PCT, durabilityPct);

        // ---------------- PREFIX (CATEGORY-AWARE) ----------------
        ItemCategory category;

        if (armor) {
            category = ItemCategory.ARMOR;
        } else if (sword || axe) {
            category = ItemCategory.WEAPON;
        } else if (tool) {
            category = ItemCategory.TOOL;
        } else {
            category = ItemCategory.OTHER;
        }

        List<PrefixDefinition> validPrefixes =
                PrefixRegistry.getAll().stream()
                        .filter(p -> p.categories().contains(category))
                        .toList();

        if (validPrefixes.isEmpty()) {
            System.out.println(
                    "[Reforged][Prefix] No valid prefixes for category " + category
            );
            return;
        }

        PrefixDefinition prefix =
                validPrefixes.get(random.nextInt(validPrefixes.size()));

        stack.set(RarityComponents.PREFIX, prefix.id());

        System.out.println(
                "[Reforged][Prefix] Applied prefix " + prefix.id()
                        + " for category " + category
        );

    }

    public static void rerollPrefix(ItemStack stack) {
        System.out.println("[Reforged] rerollPrefix called");
        System.out.println("[Reforged] Stack before: " + stack);

        // Remove existing prefix
        if (stack.contains(RarityComponents.PREFIX)) {
            System.out.println("[Reforged] Removing existing prefix");
            stack.remove(RarityComponents.PREFIX);
        } else {
            System.out.println("[Reforged] No existing prefix");
        }

        boolean armor = isArmor(stack);
        boolean sword = isSword(stack);
        boolean axe = isAxe(stack);
        boolean tool = isTool(stack);

        System.out.println(
                "[Reforged] Flags -> armor=" + armor +
                        ", sword=" + sword +
                        ", axe=" + axe +
                        ", tool=" + tool
        );

        applyRandomPrefix(
                stack,
                armor,
                sword,
                axe,
                tool,
                net.minecraft.util.math.random.Random.create()
        );

        System.out.println("[Reforged] Stack after: " + stack);
    }


    private static void applyRandomPrefix(ItemStack stack, boolean armor, boolean sword, boolean axe, boolean tool, Random random) {
        ItemCategory category;

        if (armor) {
            category = ItemCategory.ARMOR;
        } else if (sword || axe) {
            category = ItemCategory.WEAPON;
        } else if (tool) {
            category = ItemCategory.TOOL;
        } else {
            category = ItemCategory.OTHER;
        }

        List<PrefixDefinition> validPrefixes =
                PrefixRegistry.getAll().stream()
                        .filter(p -> p.categories().contains(category))
                        .toList();

        if (validPrefixes.isEmpty()) {
            return;
        }

        PrefixDefinition prefix =
                validPrefixes.get(random.nextInt(validPrefixes.size()));

        stack.set(RarityComponents.PREFIX, prefix.id());
    }







    public static int getPrefixDamagePct(ItemStack stack) {
        if (!stack.contains(RarityComponents.PREFIX)) return 0;

        String prefixId = stack.get(RarityComponents.PREFIX);
        if (prefixId == null) return 0;

        PrefixDefinition def = PrefixRegistry.get(prefixId);
        if (def == null) return 0;

        return def.damagePct();
    }


    public static Rarity getRarity(ItemStack stack) {
        String value = stack.get(RarityComponents.RARITY);
        if (value == null) return null;
        return Rarity.valueOf(value);
    }

    public static int getDamageRollPct(ItemStack stack) {
        Integer value = stack.get(RarityComponents.DAMAGE_PCT);
        return value != null ? value : 0;
    }

    public static int getDurabilityRollPct(ItemStack stack) {
        Integer value = stack.get(RarityComponents.DURABILITY_PCT);
        return value != null ? value : 0;
    }

    private static int roll(int min, int max, Random random) {
        if (min == max) return min;
        return random.nextBetween(min, max);
    }

    private static Rarity rollRarity(Random random) {
        int roll = random.nextInt(100);

        if (roll < 50) return Rarity.COMMON;
        if (roll < 78) return Rarity.UNCOMMON;
        if (roll < 93) return Rarity.RARE;
        if (roll < 98) return Rarity.EPIC;
        return Rarity.LEGENDARY;
    }

    private static ItemCategory getCategory(ItemStack stack) {
        if (isSword(stack) || isAxe(stack)) {
            return ItemCategory.WEAPON;
        }

        if (isTool(stack)) {
            return ItemCategory.TOOL;
        }

        if (isArmor(stack)) {
            return ItemCategory.ARMOR;
        }

        return ItemCategory.OTHER;
    }


}
