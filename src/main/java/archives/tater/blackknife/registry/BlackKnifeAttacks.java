package archives.tater.blackknife.registry;

import archives.tater.blackknife.BlackKnife;
import archives.tater.blackknife.item.BlackKnifeItem.Attack;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BlackKnifeAttacks {
    public static final Registry<Attack> ATTACKS = FabricRegistryBuilder.createSimple(RegistryKey.<Attack>ofRegistry(BlackKnife.id("attacks")))
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    private static Attack register(String path, Attack attack) {
        return Registry.register(ATTACKS, BlackKnife.id(path), attack);
    }

    public static final Attack TEST1 = register("test1", new Attack() {
        @Override
        public ActionResult use(World world, PlayerEntity user, Hand hand) {
            user.playSound(SoundEvents.ENTITY_RAVAGER_ROAR);
            return ActionResult.SUCCESS;
        }
    });
    public static final Attack TEST2 = register("test2", new Attack() {});
    public static final Attack TEST3 = register("test3", new Attack() {});
}
