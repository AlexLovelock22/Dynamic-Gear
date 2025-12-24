package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.AlexLovelock.reforged_2.rarity.RarityComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class ArmorHealthAttributeMixin {

    private static final Identifier ARMOR_HEALTH_ID =
            Identifier.of("reforged_2", "armor_max_health");

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void reforged2$applyArmorHealth(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!entity.isPlayer()) return;

        System.out.println("[Reforged][Armor] Tick running");

        EntityAttributeInstance attr =
                entity.getAttributeInstance(EntityAttributes.MAX_HEALTH);

        if (attr == null) {
            System.out.println("[Reforged][Armor] MAX_HEALTH attribute missing");
            return;
        }

        EntityAttributeModifier existing = attr.getModifier(ARMOR_HEALTH_ID);
        if (existing != null) {
            System.out.println("[Reforged][Armor] Removing existing modifier");
            attr.removeModifier(existing);
        }

        int totalBonus = 0;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!slot.isArmorSlot()) continue;

            ItemStack stack = entity.getEquippedStack(slot);

            if (stack.isEmpty()) {
                System.out.println("[Reforged][Armor] Slot " + slot + " empty");
                continue;
            }

            Integer bonus = stack.get(RarityComponents.MAX_HEALTH);

            System.out.println(
                    "[Reforged][Armor] Slot " + slot +
                            " item=" + stack.getItem() +
                            " MAX_HEALTH=" + bonus
            );

            if (bonus != null) {
                totalBonus += bonus;
            }
        }

        System.out.println("[Reforged][Armor] Total bonus = " + totalBonus);

        if (totalBonus > 0) {
            System.out.println("[Reforged][Armor] Applying modifier +" + totalBonus);

            attr.addPersistentModifier(
                    new EntityAttributeModifier(
                            ARMOR_HEALTH_ID,
                            totalBonus,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
            );
        } else {
            System.out.println("[Reforged][Armor] No bonus to apply");
        }
    }
}
