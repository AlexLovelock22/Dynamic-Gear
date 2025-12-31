// src/main/java/org/AlexLovelock/reforged_2/mixin/ItemStackPrefixTooltipMixin.java
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
import org.AlexLovelock.reforged_2.rarity.RarityComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackPrefixTooltipMixin {

    @Inject(
            method = "appendTooltip",
            at = @At("HEAD")
    )
    private void reforged2$addPrefixTooltip(
            Item.TooltipContext context,
            TooltipDisplayComponent display,
            PlayerEntity player,
            TooltipType type,
            Consumer<Text> consumer,
            CallbackInfo ci
    ) {
        ItemStack stack = (ItemStack) (Object) this;

        if (!stack.contains(RarityComponents.PREFIX)) return;

        String prefixId = stack.get(RarityComponents.PREFIX);
        if (prefixId == null) return;

        PrefixDefinition prefix = PrefixRegistry.get(prefixId);
        if (prefix == null) return;

        consumer.accept(
                Text.literal(prefix.displayName())
                        .formatted(Formatting.GRAY)
        );
    }
}
