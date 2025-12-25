// src/main/java/org/AlexLovelock/reforged_2/prefix/PrefixDefinition.java
package org.AlexLovelock.reforged_2.prefix;

import org.AlexLovelock.reforged_2.rarity.ItemCategory;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public final class PrefixDefinition {

    private final String id;
    private final String displayName;

    // What item categories this prefix can apply to
    private final Set<ItemCategory> categories;

    // Prefix stat modifiers
    private final int damagePct;
    private final int attackSpeedPct;
    private final int movementSpeedPct;
    private final int reachBonus;
    private final int critChancePct;
    private final int bonusXpChancePct;

    public PrefixDefinition(
            String id,
            String displayName,
            Set<ItemCategory> categories,
            int damagePct,
            int attackSpeedPct,
            int movementSpeedPct,
            int reachBonus,
            int critChancePct,
            int bonusXpChancePct
    ) {
        this.id = id;
        this.displayName = displayName;
        this.categories = Collections.unmodifiableSet(EnumSet.copyOf(categories));
        this.damagePct = damagePct;
        this.attackSpeedPct = attackSpeedPct;
        this.movementSpeedPct = movementSpeedPct;
        this.reachBonus = reachBonus;
        this.critChancePct = critChancePct;
        this.bonusXpChancePct = bonusXpChancePct;
    }

    public String id() {
        return id;
    }

    public String displayName() {
        return displayName;
    }

    public Set<ItemCategory> categories() {
        return categories;
    }

    public boolean appliesTo(ItemCategory category) {
        return categories.contains(category);
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

    public int critChancePct() {
        return critChancePct;
    }

    public int bonusXpChancePct() {
        return bonusXpChancePct;
    }
}
