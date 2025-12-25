// src/main/java/org/AlexLovelock/reforged_2/mixin/ArmorHealthAttributeMixin.java
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
public abstract class ArmorHealthAttributeMixin {

    private static final Identifier ARMOR_HEALTH_ID =
            Identifier.of("reforged_2", "armor_max_health_total");

    @Inject(method = "tick", at = @At("TAIL"))
    private void reforged2$applyArmorHealth(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!entity.isPlayer()) return;

        EntityAttributeInstance attr =
                entity.getAttributeInstance(EntityAttributes.MAX_HEALTH);
        if (attr == null) return;

        // Remove previous modifier
        EntityAttributeModifier existing = attr.getModifier(ARMOR_HEALTH_ID);
        if (existing != null) {
            attr.removeModifier(existing);
        }

        int totalBonus = 0;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!slot.isArmorSlot()) continue;

            ItemStack stack = entity.getEquippedStack(slot);
            if (stack.isEmpty()) continue;

            // Rarity-based max health (Epic / Legendary etc)
            Integer rarityBonus = stack.get(RarityComponents.MAX_HEALTH);
            if (rarityBonus != null) {
                totalBonus += rarityBonus;
            }

            // Prefix-based max health (Vital, Sickly, Weighted)
            String prefixId = stack.get(RarityComponents.PREFIX);
            if (prefixId != null) {
                PrefixDefinition prefix = PrefixRegistry.get(prefixId);
                if (prefix != null) {
                    totalBonus += prefix.maxHealthBonus();
                }
            }
        }

        if (totalBonus != 0) {
            attr.addPersistentModifier(
                    new EntityAttributeModifier(
                            ARMOR_HEALTH_ID,
                            totalBonus,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
            );

            // Clamp current health if max health was reduced
            float max = (float) attr.getValue();
            if (entity.getHealth() > max) {
                entity.setHealth(max);
            }
        }
    }
}
