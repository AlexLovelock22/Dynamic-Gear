package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerEntity;
import org.AlexLovelock.reforged_2.rarity.RarityComponents;
import org.AlexLovelock.reforged_2.rarity.RarityHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(ItemStack.class)
public abstract class MineProgressionMixin {
    @Inject(method = "postMine", at = @At("TAIL"))
    private void reforged2$onPostMine(CallbackInfo ci) {
        System.out.println("enter prog mixin");
        // Cast mixin -> ItemStack
        ItemStack stack = (ItemStack) (Object) this;

//        // Only run on server
//        if (world.isClient()) return;

        // Only count tools (optional safety)
        if (!stack.isDamageable()) return;

        // Example: only count player mining
//        if (!(miner instanceof PlayerEntity)) return;

        incrementRarity(stack);
    }

    private static void incrementRarity(ItemStack stack) {

        Integer current = stack.get(RarityComponents.RARITY_PROGRESS);
        int newRarity = ((current == null) ? 0 : current) + 1;
        stack.set(RarityComponents.RARITY_PROGRESS, newRarity);
        RarityHelper.incrementRarityTool(stack, newRarity);

    }
}
