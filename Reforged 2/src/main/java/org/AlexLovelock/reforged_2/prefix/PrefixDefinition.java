package org.AlexLovelock.reforged_2.prefix;

public final class PrefixDefinition {

    private final String id;
    private final String displayName;

    private final int damagePct;
    private final int attackSpeedPct;
    private final int movementSpeedPct;
    private final int reachBonus;

    public PrefixDefinition(
            String id,
            String displayName,
            int damagePct,
            int attackSpeedPct,
            int movementSpeedPct,
            int reachBonus
    ) {
        this.id = id;
        this.displayName = displayName;
        this.damagePct = damagePct;
        this.attackSpeedPct = attackSpeedPct;
        this.movementSpeedPct = movementSpeedPct;
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

    public int movementSpeedPct() {
        return movementSpeedPct;
    }

    public int reachBonus() {
        return reachBonus;
    }
}
