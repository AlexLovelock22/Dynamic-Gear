package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.AlexLovelock.reforged_2.rarity.RarityHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemStack.class)
public abstract class ItemStackDurabilityMixin {

    @ModifyVariable(
            method = "damage(ILnet/minecraft/server/world/ServerWorld;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Consumer;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private int reforged2$scaleDurabilityDamage(int amount) {
        ItemStack stack = (ItemStack) (Object) this;

        if (!RarityHelper.hasRarity(stack)) return amount;

        int durabilityPct = RarityHelper.getDurabilityRollPct(stack);
        if (durabilityPct == 0) return amount;

        float multiplier = 1.0f - (durabilityPct / 100.0f);
        int scaled = Math.max(1, MathHelper.floor(amount * multiplier));

        return scaled;
    }
}
