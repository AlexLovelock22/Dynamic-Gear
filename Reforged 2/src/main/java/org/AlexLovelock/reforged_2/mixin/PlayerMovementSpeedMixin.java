// src/main/java/org/AlexLovelock/reforged_2/mixin/PlayerMovementSpeedMixin.java
package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.AlexLovelock.reforged_2.prefix.PrefixDefinition;
import org.AlexLovelock.reforged_2.prefix.PrefixRegistry;
import org.AlexLovelock.reforged_2.rarity.RarityComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerMovementSpeedMixin {

    private static final Identifier PREFIX_MOVEMENT_SPEED_ID =
            Identifier.of("reforged_2", "prefix_movement_speed");

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void reforged2$applyPrefixMovementSpeed(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        EntityAttributeInstance attr =
                player.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);

        if (attr == null) return;

        // Remove previous modifier
        EntityAttributeModifier existing = attr.getModifier(PREFIX_MOVEMENT_SPEED_ID);
        if (existing != null) {
            attr.removeModifier(existing);
        }

        ItemStack stack = player.getMainHandStack();
        if (stack.isEmpty()) return;

        String prefixId = stack.get(RarityComponents.PREFIX);
        if (prefixId == null) return;

        PrefixDefinition prefix = PrefixRegistry.get(prefixId);
        if (prefix == null) return;

        int pct = prefix.movementSpeedPct();
        if (pct == 0) return;

        double amount = pct / 100.0;

        attr.addPersistentModifier(
                new EntityAttributeModifier(
                        PREFIX_MOVEMENT_SPEED_ID,
                        amount,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                )
        );
    }
}
