package org.AlexLovelock.reforged_2.rarity;

public final class RarityRanges {

    private RarityRanges() {
    }
    // Lower Bound for sword max damage
    public static int getSwordDamageMin(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> -10;
            case UNCOMMON -> 0;
            case RARE -> 4;
            case EPIC -> 5;
            case LEGENDARY -> 8;
        };
    }

    // Upper Bound for sword max damage
    public static int getSwordDamageMax(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> 0;
            case UNCOMMON -> 4;
            case RARE -> 6;
            case EPIC -> 8;
            case LEGENDARY -> 10;
        };
    }

    public static int getSwordDurabilityMin(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> -10;
            case UNCOMMON -> 0;
            case RARE -> 4;
            case EPIC -> 5;
            case LEGENDARY -> 8;
        };
    }

    public static int getSwordDurabilityMax(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> 0;
            case UNCOMMON -> 4;
            case RARE -> 6;
            case EPIC -> 8;
            case LEGENDARY -> 10;
        };
    }

    public static int getToolSpeedMin(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> -10;
            case UNCOMMON -> -1;
            case RARE -> 6;
            case EPIC -> 8;
            case LEGENDARY -> 10;
        };
    }

    public static int getToolSpeedMax(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> -1;
            case UNCOMMON -> 3;
            case RARE -> 8;
            case EPIC -> 12;
            case LEGENDARY -> 15;
        };
    }

    public static int getArmorDurabilityMax(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> 2;
            case UNCOMMON -> 4;
            case RARE -> 6;
            case EPIC -> 8;
            case LEGENDARY -> 10;
        };
    }
    public static int getArmorDurabilityMin(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> 2;
            case UNCOMMON -> 4;
            case RARE -> 6;
            case EPIC -> 8;
            case LEGENDARY -> 10;
        };
    }
}
