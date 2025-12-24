package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
    private void reforged2$appendRarityTooltip(
            Item.TooltipContext context,
            TooltipDisplayComponent display,
            PlayerEntity player,
            TooltipType type,
            Consumer<Text> textConsumer,
            CallbackInfo ci
    ) {
        ItemStack stack = (ItemStack) (Object) this;

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

        // Weapon / tool stat
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

        // Armor stat (always show, even +0)
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
