package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.AlexLovelock.reforged_2.item.ReforgeHammerItem;
import org.AlexLovelock.reforged_2.rarity.RarityComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ScreenTooltipMixin {

    private static final String OBFUSCATED_GLYPHS = "ẞẞẞẞẞẞ";

    @Inject(
            method = "getTooltip",
            at = @At("RETURN"),
            cancellable = true
    )
    private void reforged$obfuscatePrefixPreview(
            Item.TooltipContext context,
            PlayerEntity player,
            TooltipType type,
            CallbackInfoReturnable<List<Text>> cir
    ) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (!(client.currentScreen instanceof AnvilScreen anvilScreen)) {
            return;
        }

        ItemStack self = (ItemStack) (Object) this;

        // Output slot only
        ItemStack output = anvilScreen.getScreenHandler().getSlot(2).getStack();
        if (output != self) {
            return;
        }

        // Only when reforging (hammer present)
        ItemStack hammer = anvilScreen.getScreenHandler().getSlot(1).getStack();
        if (!(hammer.getItem() instanceof ReforgeHammerItem)) {
            return;
        }

        // Only if stack actually has a prefix
        if (!self.contains(RarityComponents.PREFIX)) {
            return;
        }

        List<Text> tooltip = cir.getReturnValue();
        if (tooltip == null || tooltip.isEmpty()) {
            return;
        }

        // 1) Obfuscate only the prefix in the name line (line 0)
        Text firstLine = tooltip.get(0);
        String rawName = firstLine.getString();

        int firstSpace = rawName.indexOf(' ');
        if (firstSpace > 0) {
            String baseName = rawName.substring(firstSpace + 1);

            MutableText obfuscatedPrefix = Text.literal(OBFUSCATED_GLYPHS)
                    .formatted(Formatting.OBFUSCATED, Formatting.DARK_GRAY);

            // Force base name to not inherit obfuscated formatting
            MutableText cleanBaseName = Text.literal(baseName)
                    .formatted(Formatting.RESET);

            MutableText newName = Text.empty()
                    .append(obfuscatedPrefix)
                    .append(Text.literal(" ").formatted(Formatting.RESET))
                    .append(cleanBaseName);

            tooltip.set(0, newName);
        }

        // 2) Remove ONLY the prefix tooltip block (gold title + following effect lines)
        // Keep rarity lines intact.
        removePrefixTooltipBlockOnly(tooltip);

        cir.setReturnValue(tooltip);
    }

    private static void removePrefixTooltipBlockOnly(List<Text> tooltip) {
        int goldIndex = findFirstGoldLineIndex(tooltip);
        if (goldIndex == -1) {
            return;
        }

        // Remove the gold prefix title line (e.g. "Experienced")
        tooltip.remove(goldIndex);

        // Remove subsequent prefix effect lines until we hit a "section break".
        // We stop when we reach a clearly non-prefix section (e.g. "Rarity:", "When in Main Hand:", blank line, etc).
        while (goldIndex < tooltip.size()) {
            String s = tooltip.get(goldIndex).getString();

            if (s.isBlank()) {
                break;
            }

            // Do NOT remove rarity lines
            if (s.startsWith("Rarity:")) {
                break;
            }

            // Do NOT remove vanilla sections
            if (s.startsWith("When in Main Hand:") || s.startsWith("When in Off Hand:")) {
                break;
            }

            // Heuristic: prefix effect lines are typically short “effect description” lines and appear immediately after the gold title.
            // If we hit something that looks like the debug/id/component block, stop.
            if (s.startsWith("minecraft:") || s.endsWith("component(s)")) {
                break;
            }

            tooltip.remove(goldIndex);
        }
    }

    private static int findFirstGoldLineIndex(List<Text> tooltip) {
        for (int i = 0; i < tooltip.size(); i++) {
            Text t = tooltip.get(i);

            TextColor color = t.getStyle().getColor();
            if (color == null) {
                continue;
            }

            // Formatting.GOLD is 0xFFD700 (16755200)
            if (color.getRgb() == Formatting.GOLD.getColorValue()) {
                // Avoid accidentally matching other gold-ish vanilla lines (rare, but safe)
                String s = t.getString();
                if (!s.startsWith("Rarity:") && !s.startsWith("When in ")) {
                    return i;
                }
            }
        }
        return -1;
    }
}
