// src/main/java/org/AlexLovelock/reforged_2/mixin/PlayerReachMixin.java
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
public abstract class PlayerReachMixin {

    private static final Identifier PREFIX_REACH_ID =
            Identifier.of("reforged_2", "prefix_reach");

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void reforged2$applyPrefixReach(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        EntityAttributeInstance reachAttr =
                player.getAttributeInstance(EntityAttributes.ENTITY_INTERACTION_RANGE);

        if (reachAttr == null) return;

        // Remove old modifier every tick (safe + simple)
        EntityAttributeModifier existing = reachAttr.getModifier(PREFIX_REACH_ID);
        if (existing != null) {
            reachAttr.removeModifier(existing);
        }

        ItemStack stack = player.getMainHandStack();
        if (stack.isEmpty()) return;

        String prefixId = stack.get(RarityComponents.PREFIX);
        if (prefixId == null) return;

        PrefixDefinition prefix = PrefixRegistry.get(prefixId);
        if (prefix == null) return;

        int reachBonus = prefix.reachBonus();
        if (reachBonus == 0) return;

        reachAttr.addPersistentModifier(
                new EntityAttributeModifier(
                        PREFIX_REACH_ID,
                        reachBonus,
                        EntityAttributeModifier.Operation.ADD_VALUE
                )
        );
    }
}
