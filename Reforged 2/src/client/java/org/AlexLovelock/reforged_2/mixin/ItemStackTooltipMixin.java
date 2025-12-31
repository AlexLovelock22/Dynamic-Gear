// src/client/java/org/AlexLovelock/reforged_2/mixin/ItemStackTooltipMixin.java
package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.AlexLovelock.reforged_2.prefix.PrefixDefinition;
import org.AlexLovelock.reforged_2.prefix.PrefixRegistry;
import org.AlexLovelock.reforged_2.rarity.ItemCategory;
import org.AlexLovelock.reforged_2.rarity.Rarity;
import org.AlexLovelock.reforged_2.rarity.RarityComponents;
import org.AlexLovelock.reforged_2.rarity.RarityHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackTooltipMixin {

    @Inject(
            method = "appendTooltip",
            at = @At("TAIL")
    )
    private void reforged2$appendReforgedTooltips(
            Item.TooltipContext context,
            TooltipDisplayComponent display,
            PlayerEntity player,
            TooltipType type,
            Consumer<Text> consumer,
            CallbackInfo ci
    ) {
        ItemStack stack = (ItemStack) (Object) this;

        // ---------------- PREFIX ----------------

        String prefixId = stack.get(RarityComponents.PREFIX);
        if (prefixId != null) {
            PrefixDefinition prefix = PrefixRegistry.get(prefixId);
            if (prefix != null) {
                consumer.accept(Text.empty());

                consumer.accept(
                        Text.literal(prefix.displayName())
                                .formatted(Formatting.GOLD)
                );

                if (prefix.damagePct() != 0) {
                    consumer.accept(
                            Text.literal(formatPct(prefix.damagePct()) + " Damage")
                                    .formatted(Formatting.GRAY)
                    );
                }

                if (prefix.attackSpeedPct() != 0) {
                    consumer.accept(
                            Text.literal(formatPct(prefix.attackSpeedPct()) + " Attack Speed")
                                    .formatted(Formatting.GRAY)
                    );
                }

                if (prefix.reachBonus() != 0) {
                    int reach = prefix.reachBonus();
                    consumer.accept(
                            Text.literal((reach > 0 ? "+" : "") + reach + " Reach")
                                    .formatted(Formatting.GRAY)
                    );
                }

                if (prefix.movementSpeedPct() != 0) {
                    consumer.accept(
                            Text.literal(formatPct(prefix.movementSpeedPct()) + " Movement Speed")
                                    .formatted(Formatting.GRAY)
                    );
                }

                if (prefix.critChancePct() != 0) {
                    consumer.accept(
                            Text.literal(formatPct(prefix.critChancePct()) + " Critical Chance")
                                    .formatted(Formatting.GRAY)
                    );
                }

                if (prefix.bonusXpChancePct() != 0) {
                    consumer.accept(
                            Text.literal(prefix.bonusXpChancePct() + "% chance to double XP")
                                    .formatted(Formatting.GRAY)
                    );
                }

                if (prefix.appliesTo(ItemCategory.ARMOR)) {
                    if (prefix.armorBonus() != 0) {
                        consumer.accept(
                                Text.literal(
                                        (prefix.armorBonus() > 0 ? "+" : "") +
                                                prefix.armorBonus() + " Armor"
                                ).formatted(Formatting.GRAY)
                        );
                    }

                    if (prefix.armorToughnessBonus() != 0) {
                        consumer.accept(
                                Text.literal(
                                        (prefix.armorToughnessBonus() > 0 ? "+" : "") +
                                                prefix.armorToughnessBonus() + " Armor Toughness"
                                ).formatted(Formatting.GRAY)
                        );
                    }

                    if (prefix.maxHealthBonus() != 0) {
                        consumer.accept(
                                Text.literal(
                                        (prefix.maxHealthBonus() > 0 ? "+" : "") +
                                                prefix.maxHealthBonus() + " Max Health"
                                ).formatted(Formatting.GRAY)
                        );
                    }
                }
            }
        }

        // ---------------- RARITY ----------------

        if (!RarityHelper.hasRarity(stack)) return;

        Rarity rarity = RarityHelper.getRarity(stack);
        if (rarity == null) return;

        consumer.accept(Text.empty());

        consumer.accept(
                Text.literal("Rarity: " + rarity.getDisplayName())
                        .formatted(rarity.getColor())
        );

        int durability = RarityHelper.getDurabilityRollPct(stack);

        Integer miningSpeed = stack.get(RarityComponents.MINING_SPEED_PCT);
        Integer damage = stack.get(RarityComponents.DAMAGE_PCT);
        Integer maxHealth = stack.get(RarityComponents.MAX_HEALTH);

        consumer.accept(
                Text.literal("Durability: " + formatPct(durability))
                        .formatted(Formatting.GRAY)
        );

        if (miningSpeed != null) {
            consumer.accept(
                    Text.literal("Mining Speed: " + formatPct(miningSpeed))
                            .formatted(Formatting.GRAY)
            );
        } else if (damage != null) {
            consumer.accept(
                    Text.literal("Damage: " + formatPct(damage))
                            .formatted(Formatting.GRAY)
            );
        }

        if (maxHealth != null) {
            consumer.accept(
                    Text.literal("Max Health: +" + maxHealth)
                            .formatted(Formatting.GRAY)
            );
        }
    }

    private static String formatPct(int value) {
        return (value >= 0 ? "+" : "") + value + "%";
    }
}
