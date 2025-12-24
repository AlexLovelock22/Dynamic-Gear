package org.AlexLovelock.reforged_2.rarity;

import net.minecraft.util.Formatting;

public enum Rarity {
    COMMON("Common", Formatting.GRAY),
    UNCOMMON("Uncommon", Formatting.GREEN),
    RARE("Rare", Formatting.BLUE),
    EPIC("Epic", Formatting.DARK_PURPLE),
    LEGENDARY("Legendary", Formatting.GOLD);

    private final String displayName;
    private final Formatting color;

    Rarity(String displayName, Formatting color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Formatting getColor() {
        return color;
    }
}
