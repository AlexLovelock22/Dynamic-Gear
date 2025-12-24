package org.AlexLovelock.reforged_2.rarity;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;


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
        boolean armor = isArmor(stack);

        if (hasRarity(stack) && !armor) return;

        boolean sword = isSword(stack);
        boolean axe = isAxe(stack);
        boolean tool = isTool(stack);

        if (!sword && !axe && !tool && !armor) return;

        Rarity rarity = rollRarity(random);

        int durabilityPct = roll(
                RarityRanges.getSwordDurabilityMin(rarity),
                RarityRanges.getSwordDurabilityMax(rarity),
                random
        );

        // ARMOR
        if (armor) {
            int maxHealth =
                    (rarity == Rarity.EPIC || rarity == Rarity.LEGENDARY) ? 1 : 0;

            stack.set(RarityComponents.RARITY, rarity.name());
            stack.set(RarityComponents.DURABILITY_PCT, durabilityPct);
            stack.set(RarityComponents.MAX_HEALTH, maxHealth);
            return;
        }

        // AXE (50/50)
        if (axe) {
            if (random.nextBoolean()) {
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
        // SWORD
        else if (sword) {
            int damagePct = roll(
                    RarityRanges.getSwordDamageMin(rarity),
                    RarityRanges.getSwordDamageMax(rarity),
                    random
            );
            stack.set(RarityComponents.DAMAGE_PCT, damagePct);
        }
        // TOOL
        else if (tool) {
            int miningSpeedPct = roll(
                    RarityRanges.getToolSpeedMin(rarity),
                    RarityRanges.getToolSpeedMax(rarity),
                    random
            );
            stack.set(RarityComponents.MINING_SPEED_PCT, miningSpeedPct);
        }

        stack.set(RarityComponents.RARITY, rarity.name());
        stack.set(RarityComponents.DURABILITY_PCT, durabilityPct);
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

        if (roll < 60) return Rarity.COMMON;
        if (roll < 85) return Rarity.UNCOMMON;
        if (roll < 95) return Rarity.RARE;
        if (roll < 99) return Rarity.EPIC;
        return Rarity.LEGENDARY;
    }


}
