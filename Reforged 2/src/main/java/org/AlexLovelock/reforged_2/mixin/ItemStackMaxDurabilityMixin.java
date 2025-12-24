package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.item.ItemStack;
import org.AlexLovelock.reforged_2.rarity.RarityHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMaxDurabilityMixin {

    @Inject(
            method = "getMaxDamage",
            at = @At("RETURN"),
            cancellable = true
    )
    private void reforged2$scaleMaxDurability(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        if (!RarityHelper.hasRarity(stack)) return;

        int baseMax = cir.getReturnValue();
        if (baseMax <= 0) return;

        int pct = RarityHelper.getDurabilityRollPct(stack);
        if (pct == 0) return;

        int scaledMax = Math.max(1, Math.round(baseMax * (1.0f + (pct / 100.0f))));
        cir.setReturnValue(scaledMax);
    }
}
