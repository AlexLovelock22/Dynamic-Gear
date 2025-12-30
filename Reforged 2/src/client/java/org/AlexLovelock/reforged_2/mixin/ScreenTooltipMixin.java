package org.AlexLovelock.reforged_2.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
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

    private static final int OBFUSCATED_GLYPH_COUNT = 6;

    @Inject(
            method = "getTooltip",
            at = @At("RETURN"),
            cancellable = true
    )
    private void reforged$obfuscatePrefix(
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

        // Require hammer in slot 1 (reforging only)
        ItemStack hammer = anvilScreen.getScreenHandler().getSlot(1).getStack();
        if (!(hammer.getItem() instanceof ReforgeHammerItem)) {
            return;
        }

        // Only affect the output slot
        ItemStack output = anvilScreen.getScreenHandler().getSlot(2).getStack();
        if (output != self) {
            return;
        }

        // Must actually have a prefix
        if (!self.contains(RarityComponents.PREFIX)) {
            return;
        }

        List<Text> tooltip = cir.getReturnValue();
        if (tooltip.isEmpty()) {
            return;
        }

        Text firstLine = tooltip.get(0);
        String rawName = firstLine.getString();

        int spaceIndex = rawName.indexOf(' ');
        if (spaceIndex == -1) {
            return;
        }

        String baseName = rawName.substring(spaceIndex + 1);

        // Fixed-length letter mask (no info leakage)
        StringBuilder mask = new StringBuilder();
        for (int i = 0; i < OBFUSCATED_GLYPH_COUNT; i++) {
            mask.append('A');
        }

        MutableText obfuscatedPrefix = Text.literal(mask.toString())
                .formatted(Formatting.OBFUSCATED, Formatting.DARK_GRAY);

        MutableText newName = Text.empty()
                .append(obfuscatedPrefix)
                .append(" ")
                .append(Text.literal(baseName));

        tooltip.set(0, newName);
        cir.setReturnValue(tooltip);
    }
}
