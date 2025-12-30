package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.AlexLovelock.reforged_2.item.ReforgeHammerItem;
import org.AlexLovelock.reforged_2.rarity.RarityComponents;
import org.AlexLovelock.reforged_2.rarity.RarityHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin {

    @Shadow @Final private Property levelCost;

    /* =========================
       PREVIEW + CACHE RESULT
       ========================= */
    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    private void reforged$updateResult(CallbackInfo ci) {
        AnvilScreenHandler self = (AnvilScreenHandler) (Object) this;

        Slot leftSlot = self.slots.get(0);
        Slot rightSlot = self.slots.get(1);
        Slot outputSlot = self.slots.get(2);

        ItemStack input = leftSlot.getStack();
        ItemStack hammer = rightSlot.getStack();

        System.out.println("[Reforged] updateResult called");

        // Hard invalid: no item to reforge
        if (input.isEmpty()) {
            outputSlot.setStack(ItemStack.EMPTY);
            levelCost.set(0);
            ci.cancel();
            return;
        }

// Soft invalid: hammer missing or incompatible
        if (!(hammer.getItem() instanceof ReforgeHammerItem reforgeHammer)
                || !reforgeHammer.canReforge(input)) {

            // Do NOT clear output here
            // Just cancel vanilla
            levelCost.set(0);
            ci.cancel();
            return;
        }


        ItemStack result = input.copy();

// Always remove old prefix first
        if (result.contains(RarityComponents.PREFIX)) {
            result.remove(RarityComponents.PREFIX);
        }

        RarityHelper.rerollPrefix(result);

        outputSlot.setStack(result);
        levelCost.set(1);

        self.sendContentUpdates();
        ci.cancel();
    }

    /* =========================
       COMMIT TRANSACTION
       ========================= */
    @Inject(method = "onTakeOutput", at = @At("HEAD"), cancellable = true)
    private void reforged$onTakeOutput(PlayerEntity player, ItemStack output, CallbackInfo ci) {
        AnvilScreenHandler self = (AnvilScreenHandler) (Object) this;

        Slot leftSlot = self.slots.get(0);
        Slot rightSlot = self.slots.get(1);

        ItemStack input = leftSlot.getStack();
        ItemStack hammer = rightSlot.getStack();

        System.out.println("[Reforged] onTakeOutput called");

        if (input.isEmpty() || !(hammer.getItem() instanceof ReforgeHammerItem)) {
            System.out.println("[Reforged] onTakeOutput aborted – invalid state");
            return;
        }

        int cost = levelCost.get();
        if (!player.getAbilities().creativeMode && player.experienceLevel < cost) {
            System.out.println("[Reforged] onTakeOutput aborted – insufficient XP");
            return;
        }

        if (!player.getAbilities().creativeMode) {
            player.addExperienceLevels(-cost);
        }

        hammer.damage(1, player, EquipmentSlot.MAINHAND);
        leftSlot.setStack(ItemStack.EMPTY);

        if (hammer.isEmpty()) {
            rightSlot.setStack(ItemStack.EMPTY);
        }

        if (!player.getEntityWorld().isClient()) {
            ServerWorld world = (ServerWorld) player.getEntityWorld();
            world.playSound(
                    null,
                    player.getBlockPos(),
                    SoundEvents.BLOCK_ANVIL_USE,
                    SoundCategory.BLOCKS,
                    1.0f,
                    1.0f
            );
        }

        self.sendContentUpdates();
        ci.cancel();
    }
}
