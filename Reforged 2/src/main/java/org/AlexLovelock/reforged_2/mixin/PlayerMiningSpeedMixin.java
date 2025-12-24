package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.AlexLovelock.reforged_2.rarity.RarityComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerMiningSpeedMixin {

    @Inject(
            method = "getBlockBreakingSpeed",
            at = @At("RETURN"),
            cancellable = true
    )
    private void reforged2$applyMiningSpeedBonus(
            BlockState state,
            CallbackInfoReturnable<Float> cir
    ) {
        float original = cir.getReturnValue();

        PlayerEntity player = (PlayerEntity) (Object) this;
        ItemStack stack = player.getMainHandStack();

        if (stack.isEmpty()) return;



        Integer pct = stack.get(RarityComponents.MINING_SPEED_PCT);
        if (pct == null || pct == 0) return;

        float strength = 1.75f;
        float multiplier = 1.0f + ((pct / 100.0f) * strength);
        float modified = original * multiplier;


        System.out.println(
                "[Reforged] Mining speed modified: " + original + " -> " + modified
        );

        cir.setReturnValue(modified);
    }
}
