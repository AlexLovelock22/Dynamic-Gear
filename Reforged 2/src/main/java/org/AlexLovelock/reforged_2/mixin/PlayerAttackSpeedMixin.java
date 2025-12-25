package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.AlexLovelock.reforged_2.prefix.PrefixDefinition;
import org.AlexLovelock.reforged_2.prefix.PrefixRegistry;
import org.AlexLovelock.reforged_2.rarity.RarityComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class PlayerAttackSpeedMixin {

    private static final Identifier PREFIX_ATTACK_SPEED_ID =
            Identifier.of("reforged_2", "prefix_attack_speed");

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void reforged2$applyPrefixAttackSpeed(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!entity.isPlayer()) return;

        EntityAttributeInstance attr =
                entity.getAttributeInstance(EntityAttributes.ATTACK_SPEED);

        if (attr == null) return;

        // Remove old modifier
        EntityAttributeModifier existing = attr.getModifier(PREFIX_ATTACK_SPEED_ID);
        if (existing != null) {
            attr.removeModifier(existing);
        }

        ItemStack held = entity.getEquippedStack(EquipmentSlot.MAINHAND);
        if (held.isEmpty()) return;

        String prefixId = held.get(RarityComponents.PREFIX);
        if (prefixId == null) return;

        PrefixDefinition prefix = PrefixRegistry.get(prefixId);
        if (prefix == null) return;

        int attackSpeedPct = prefix.attackSpeedPct();
        if (attackSpeedPct == 0) return;

        double amount = attackSpeedPct / 100.0;

        attr.addPersistentModifier(
                new EntityAttributeModifier(
                        PREFIX_ATTACK_SPEED_ID,
                        amount,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE

                )
        );
    }
}
