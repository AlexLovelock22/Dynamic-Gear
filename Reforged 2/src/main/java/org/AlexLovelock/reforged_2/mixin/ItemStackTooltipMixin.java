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
    private void reforged2$appendCustomTooltips(
            Item.TooltipContext context,
            TooltipDisplayComponent display,
            PlayerEntity player,
            TooltipType type,
            Consumer<Text> textConsumer,
            CallbackInfo ci
    ) {
        ItemStack stack = (ItemStack) (Object) this;

        // ---------- PREFIX TOOLTIP ----------
        String prefixId = stack.get(RarityComponents.PREFIX);
        if (prefixId != null) {
            PrefixDefinition prefix = PrefixRegistry.get(prefixId);
            if (prefix != null) {
                textConsumer.accept(Text.empty());

                // Prefix name
                textConsumer.accept(
                        Text.literal(prefix.displayName())
                                .formatted(Formatting.GOLD)
                );

                // Prefix effects (JSON-driven)
                if (prefix.damagePct() != 0) {
                    textConsumer.accept(
                            Text.literal(formatPct(prefix.damagePct()) + " Damage")
                                    .formatted(Formatting.GRAY)
                    );
                }

                if (prefix.attackSpeedPct() != 0) {
                    textConsumer.accept(
                            Text.literal(formatPct(prefix.attackSpeedPct()) + " Attack Speed")
                                    .formatted(Formatting.GRAY)
                    );
                }

                // Future-proof: add more here later
                // reach, speed, movement, crit chance, etc
            }
        }

        // ---------- RARITY TOOLTIP ----------
        if (!RarityHelper.hasRarity(stack)) return;

        Rarity rarity = RarityHelper.getRarity(stack);
        if (rarity == null) return;

        textConsumer.accept(Text.empty());

        textConsumer.accept(
                Text.literal("Rarity: " + rarity.getDisplayName())
                        .formatted(rarity.getColor())
        );

        int durability = RarityHelper.getDurabilityRollPct(stack);

        Integer miningSpeed = stack.get(RarityComponents.MINING_SPEED_PCT);
        Integer damage = stack.get(RarityComponents.DAMAGE_PCT);
        Integer maxHealth = stack.get(RarityComponents.MAX_HEALTH);

        textConsumer.accept(
                Text.literal("Durability: " + formatPct(durability))
                        .formatted(Formatting.GRAY)
        );

        if (miningSpeed != null) {
            textConsumer.accept(
                    Text.literal("Mining Speed: " + formatPct(miningSpeed))
                            .formatted(Formatting.GRAY)
            );
        } else if (damage != null) {
            textConsumer.accept(
                    Text.literal("Damage: " + formatPct(damage))
                            .formatted(Formatting.GRAY)
            );
        }

        if (maxHealth != null) {
            textConsumer.accept(
                    Text.literal("Max Health: +" + maxHealth)
                            .formatted(Formatting.GRAY)
            );
        }
    }

    private static String formatPct(int value) {
        return (value >= 0 ? "+" : "") + value + "%";
    }
}
