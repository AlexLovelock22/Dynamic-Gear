// src/main/java/org/AlexLovelock/reforged_2/prefix/PrefixDefinition.java
package org.AlexLovelock.reforged_2.prefix;

public final class PrefixDefinition {

    private final String id;
    public String getId() {
        return id;
    }

    private final String displayName;

    // For now we only need this for Sharp (+8% damage)
    private final int damagePct;

    public PrefixDefinition(String id, String displayName, int damagePct) {
        this.id = id;
        this.displayName = displayName;
        this.damagePct = damagePct;
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
}
