package org.AlexLovelock.reforged_2.rarity;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;

public final class RarityComponents {

    public static ComponentType<String> RARITY;
    public static ComponentType<Integer> DAMAGE_PCT;
    public static ComponentType<Integer> DURABILITY_PCT;
    public static ComponentType<Integer> MINING_SPEED_PCT;
    public static ComponentType<Integer> MAX_HEALTH;

    private RarityComponents() {
    }
}
