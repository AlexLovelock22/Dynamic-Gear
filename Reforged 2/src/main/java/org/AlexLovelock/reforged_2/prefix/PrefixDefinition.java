// src/main/java/org/AlexLovelock/reforged_2/prefix/PrefixDefinition.java
package org.AlexLovelock.reforged_2.prefix;

public final class PrefixDefinition {

    private final String id;
    private final String displayName;

    // Prefix stat modifiers
    private final int damagePct;
    private final int attackSpeedPct;
    private final int reachBonus;

    public PrefixDefinition(
            String id,
            String displayName,
            int damagePct,
            int attackSpeedPct,
            int reachBonus
    ) {
        this.id = id;
        this.displayName = displayName;
        this.damagePct = damagePct;
        this.attackSpeedPct = attackSpeedPct;
        this.reachBonus = reachBonus;
    }

    public String id() {
        return id;
    }

    public String displayName() {
        return displayName;
    }

    public int damagePct() {
        return damagePct;
    }

    public int attackSpeedPct() {
        return attackSpeedPct;
    }

    public int reachBonus() {
        return reachBonus;
    }
}
