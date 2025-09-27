package archives.tater.blackknife.registry;

import archives.tater.blackknife.BlackKnife;
import archives.tater.blackknife.item.BlackKnifeItem.Attack;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Items;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BlackKnifeAttacks {
    public static final Registry<Attack> ATTACKS = FabricRegistryBuilder.createSimple(RegistryKey.<Attack>ofRegistry(BlackKnife.id("attacks")))
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();

    private static Attack register(String path, Attack attack) {
        return Registry.register(ATTACKS, BlackKnife.id(path), attack);
    }

    public static final Attack TRACKING_SWORDS = register("tracking_swords", new Attack() {
        @Override
        public ActionResult use(World world, PlayerEntity user, Hand hand) {
            if (world.isClient()) return ActionResult.SUCCESS;
            var start = user.getEyePos();
            var end = start.add(user.getRotationVector().multiply(16));
            var hit = ProjectileUtil.raycast(user, start, end, new Box(start, end), Entity::isLiving, 16 * 16);
            if (hit == null) return ActionResult.FAIL;
            var target = hit.getEntity();
            var offset = new Vec3d(
                    user.getRandom().nextBetween(-1, 1),
                    user.getRandom().nextBetween(0, 1),
                    user.getRandom().nextBetween(-1, 1)
            ).multiply(target.getWidth() / 2 + 2);
            var projectile = new ArrowEntity(world, user, Items.ARROW.getDefaultStack(), null);
            projectile.setPosition(target.getEyePos().add(offset));
            projectile.setVelocity(-offset.x, -offset.y, -offset.z, 3f, 1f);
            world.spawnEntity(projectile);

            return ActionResult.SUCCESS;
        }
    });
    public static final Attack TEST2 = register("test2", new Attack() {});
    public static final Attack TEST3 = register("test3", new Attack() {});
}
