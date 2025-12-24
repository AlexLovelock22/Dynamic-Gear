package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.AlexLovelock.reforged_2.rarity.RarityHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class ItemStackAttackDamageMixin {

    @ModifyVariable(
            method = "damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z",
            at = @At("HEAD"),
            ordinal = 0
    )
    private float reforged2$modifyDamage(
            float amount,
            ServerWorld world,
            DamageSource source
    ) {
        if (world.isClient()) {
            return amount;
        }

        if (source.getAttacker() instanceof LivingEntity attacker) {
            ItemStack stack = attacker.getMainHandStack();

            if (!stack.isEmpty()) {
                int pct = RarityHelper.getDamageRollPct(stack);

                System.out.println("[Reforged] Damage roll pct = " + pct);

                float multiplier = 1.0f + (pct / 100.0f);
                float newAmount = amount * multiplier;

                System.out.println(
                        "[Reforged] Damage modified: " + amount + " -> " + newAmount
                );

                return newAmount;
            }
        }

        return amount;
    }
}
