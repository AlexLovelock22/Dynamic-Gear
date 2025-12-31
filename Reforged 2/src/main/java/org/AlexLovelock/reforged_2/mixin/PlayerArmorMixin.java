// src/main/java/org/AlexLovelock/reforged_2/mixin/PlayerArmorMixin.java
package org.AlexLovelock.reforged_2.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.AlexLovelock.reforged_2.prefix.PrefixDefinition;
import org.AlexLovelock.reforged_2.prefix.PrefixRegistry;
import org.AlexLovelock.reforged_2.rarity.ItemCategory;
import org.AlexLovelock.reforged_2.rarity.RarityComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerArmorMixin {

    private static final Identifier PREFIX_ARMOR_MODIFIER_ID =
            Identifier.of("reforged_2", "prefix_armor_bonus");

    @Unique
    private int reforged2$lastArmorBonus = 0;

    @Inject(method = "tick", at = @At("TAIL"))
    private void reforged2$applyArmorPrefixBonuses(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (player.getEntityWorld().isClient()) return;

        EntityAttributeInstance armorAttr =
                player.getAttributeInstance(EntityAttributes.ARMOR);

        if (armorAttr == null) return;

        int totalArmorBonus = 0;

        for (EquipmentSlot slot : new EquipmentSlot[]{
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET
        }) {
            ItemStack stack = player.getEquippedStack(slot);
            if (stack.isEmpty()) continue;

            String prefixId = stack.get(RarityComponents.PREFIX);

            System.out.println(
                    "[Reforged][Armor][DEBUG] Slot=" + slot +
                            " Item=" + stack.getItem() +
                            " Prefix=" + prefixId
            );

            if (prefixId == null) continue;

            PrefixDefinition prefix = PrefixRegistry.get(prefixId);
            if (prefix == null) continue;

            if (!prefix.appliesTo(ItemCategory.ARMOR)) continue;

            totalArmorBonus += prefix.armorBonus();
        }

        // No change → do nothing (prevents flicker)
        if (totalArmorBonus == reforged2$lastArmorBonus) {
            return;
        }

        // Remove old modifier
        armorAttr.removeModifier(PREFIX_ARMOR_MODIFIER_ID);

        // Apply new modifier if needed
        if (totalArmorBonus > 0) {
            armorAttr.addPersistentModifier(
                    new EntityAttributeModifier(
                            PREFIX_ARMOR_MODIFIER_ID,
                            totalArmorBonus,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
            );
        }

        reforged2$lastArmorBonus = totalArmorBonus;

        System.out.println(
                "[Reforged][Armor] Updated armor prefix bonus to " + totalArmorBonus
        );
    }
}
