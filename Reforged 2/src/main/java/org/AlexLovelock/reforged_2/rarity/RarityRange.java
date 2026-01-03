package org.AlexLovelock.reforged_2.rarity;

public enum RarityRange {
    COMMON(0, 50),
    UNCOMMON(50, 78),
    RARE(78, 93),
    EPIC(93, 98),
    LEGENDARY(98, 100);
    private final int maxPctExclusive;
    private final int minPct;
    RarityRange(int minPct, int maxPctExclusive) {
        this.minPct = minPct;
        this.maxPctExclusive = maxPctExclusive;
    }
    public int getMinPercent() {
        return minPct;
    }
    public int getMaxPercent() {
        return maxPctExclusive;
    }
    public boolean matches(int pct) {
        return pct >= minPct && pct < maxPctExclusive;
    }

    public static RarityRange fromPercent(int pct) {
        pct = Math.max(0, Math.min(100, pct));

        for (RarityRange rarity : values()) {
            if (rarity.matches(pct)) {
                return rarity;
            }
        }

        // Should never happen
        return LEGENDARY;
    }
}
