package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.world.World;
import org.AlexLovelock.reforged_2.rarity.RarityHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingResultSlot.class)
public abstract class CraftingResultSlotMixin {

    @Inject(
            method = "onTakeItem",
            at = @At("TAIL")
    )
    private void reforged2$afterTakeItem(
            PlayerEntity player,
            ItemStack stack,
            CallbackInfo ci
    ) {
        World world = player.getEntityWorld();
        if (world.isClient()) return;

        RarityHelper.assignRarityIfMissing(stack, world.getRandom());
    }
}
