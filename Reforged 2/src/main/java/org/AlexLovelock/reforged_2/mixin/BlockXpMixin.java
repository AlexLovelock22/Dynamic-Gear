// src/main/java/org/AlexLovelock/reforged_2/mixin/BlockXpMixin.java
package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.AlexLovelock.reforged_2.prefix.PrefixDefinition;
import org.AlexLovelock.reforged_2.prefix.PrefixRegistry;
import org.AlexLovelock.reforged_2.rarity.RarityComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Block.class)
public abstract class BlockXpMixin {

    @ModifyVariable(
            method = "dropExperience(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;I)V",
            at = @At("HEAD"),
            ordinal = 0
    )
    private static int reforged2$modifyDroppedXp(
            int amount,
            ServerWorld world,
            BlockPos pos
    ) {
        if (amount <= 0) {
            return amount;
        }

        PlayerEntity player = world.getClosestPlayer(
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                5.0,
                false
        );

        if (player == null) {
            return amount;
        }

        ItemStack stack = player.getMainHandStack();
        if (stack.isEmpty()) {
            return amount;
        }

        String prefixId = stack.get(RarityComponents.PREFIX);
        if (prefixId == null) {
            return amount;
        }

        PrefixDefinition prefix = PrefixRegistry.get(prefixId);
        if (prefix == null) {
            return amount;
        }

        int chancePct = prefix.bonusXpChancePct();
        if (chancePct <= 0) {
            return amount;
        }

        Random random = world.getRandom();
        if (random.nextInt(100) < chancePct) {
            System.out.println(
                    "[Reforged][XP] Bonus XP triggered (" + chancePct + "%): " + amount + " -> " + (amount * 2)
            );
            return amount * 2;
        }

        return amount;
    }
}
