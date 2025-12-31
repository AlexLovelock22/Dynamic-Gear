package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import org.AlexLovelock.reforged_2.rarity.RarityComponents;
import org.AlexLovelock.reforged_2.rarity.RarityHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class InventoryRarityTickMixin {

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void reforged2$applyMissingRarities(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        PlayerInventory inventory = player.getInventory();

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);

            if (stack.isEmpty()) continue;
            if (stack.contains(RarityComponents.RARITY)) continue;

            RarityHelper.assignRarityIfMissing(
                    stack,
                    player.getRandom()
            );
        }
    }
}
