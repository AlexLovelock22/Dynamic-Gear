package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import org.AlexLovelock.reforged_2.rarity.RarityHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingScreenHandler.class)
public abstract class CraftingResultSlotStackCraftMixin {

    @Inject(
            method = "quickMove",
            at = @At("HEAD")
    )
    private void reforged2$applyRarityBeforeShiftCraft(
            PlayerEntity player,
            int slotIndex,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        if (!(player instanceof ServerPlayerEntity)) return;

        CraftingScreenHandler handler = (CraftingScreenHandler) (Object) this;

        Slot resultSlot = handler.getSlot(0);
        ItemStack template = resultSlot.getStack();

        if (template.isEmpty()) return;

        RarityHelper.assignRarityIfMissing(template, Random.create());
    }
}
