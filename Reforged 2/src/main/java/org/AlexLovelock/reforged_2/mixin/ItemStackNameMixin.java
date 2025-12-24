package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.AlexLovelock.reforged_2.prefix.PrefixDefinition;
import org.AlexLovelock.reforged_2.prefix.PrefixRegistry;
import org.AlexLovelock.reforged_2.rarity.RarityComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackNameMixin {

    @Inject(
            method = "getName",
            at = @At("HEAD"),
            cancellable = true
    )
    private void reforged2$prependPrefix(CallbackInfoReturnable<Text> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        String prefixId = stack.get(RarityComponents.PREFIX);
        if (prefixId == null || prefixId.isBlank()) return;

        PrefixDefinition prefix = PrefixRegistry.get(prefixId);
        if (prefix == null) return;

        // Original item name
        Text baseName = stack.getItem().getName(stack);

        // New name: "Sharp Diamond Sword"
        Text newName = Text.literal(prefix.displayName() + " ")
                .append(baseName);

        cir.setReturnValue(newName);
    }
}
