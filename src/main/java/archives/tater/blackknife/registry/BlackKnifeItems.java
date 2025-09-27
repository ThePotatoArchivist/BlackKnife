package archives.tater.blackknife.registry;

import archives.tater.blackknife.BlackKnife;
import archives.tater.blackknife.item.BlackKnifeItem;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class BlackKnifeItems {
    private static Item register(Identifier id, Function<Item.Settings, Item> item, Item.Settings settings) {
        var key = RegistryKey.of(RegistryKeys.ITEM, id);
        return Registry.register(Registries.ITEM, key, item.apply(settings.registryKey(key)));
    }

    private static Item register(String path, Function<Item.Settings, Item> item, Item.Settings settings) {
        return register(BlackKnife.id(path), item, settings);
    }

    private static Item register(String path, Item.Settings settings) {
        return register(path, Item::new, settings);
    }

    public static final Item BLACK_KNIFE = register("black_knife", BlackKnifeItem::new, new Settings()
            .sword(ToolMaterial.NETHERITE, 3f, -2.4f)
            .component(DataComponentTypes.WEAPON, new WeaponComponent(1, 5))
            .attributeModifiers(AttributeModifiersComponent.builder()
                    .add(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, 14, Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                    .add(EntityAttributes.ATTACK_SPEED, new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, -2.8, Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                    .add(EntityAttributes.ENTITY_INTERACTION_RANGE, new EntityAttributeModifier(BlackKnife.id("extra_reach_distance"), 2, Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                    .build())
            .fireproof());

    public static void init() {

    }
}
