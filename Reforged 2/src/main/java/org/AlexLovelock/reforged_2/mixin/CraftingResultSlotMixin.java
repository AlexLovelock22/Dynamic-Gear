package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.random.Random;
import net.minecraft.entity.player.PlayerEntity;
import org.AlexLovelock.reforged_2.rarity.RarityHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingResultSlot.class)
public abstract class CraftingResultSlotMixin extends Slot {

    public CraftingResultSlotMixin(CraftingResultInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Inject(method = "onTakeItem", at = @At("HEAD"))
    private void reforged2$assignRarity(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        RarityHelper.assignRarityIfMissing(stack, Random.create());
    }
}
