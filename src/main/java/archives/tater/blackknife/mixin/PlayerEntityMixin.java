package archives.tater.blackknife.mixin;

import archives.tater.blackknife.registry.BlackKnifeItems;
import archives.tater.blackknife.registry.BlackKnifeParticles;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;onAttacking(Lnet/minecraft/entity/Entity;)V")
    )
    private void spawnSweepParticles(Entity target, CallbackInfo ci, @Local ItemStack weapon, @Local(ordinal = 0) boolean cooldownDone, @Local(ordinal = 3) boolean sweepAttack) {
        if (!cooldownDone || !weapon.isOf(BlackKnifeItems.BLACK_KNIFE)) return;
        double d = -MathHelper.sin(getYaw() * (float) (Math.PI / 180.0));
        double e = MathHelper.cos(getYaw() * (float) (Math.PI / 180.0));
        if (getEntityWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(BlackKnifeParticles.BLACK_KNIFE_SWEEP, target.getX() - d, target.getBodyY(0.5), target.getZ() - e, 0, d, 0.0, e, 0.0);
            if (sweepAttack)
                serverWorld.spawnParticles(BlackKnifeParticles.BLACK_KNIFE_SWEEP, getX() + d, getBodyY(0.5), getZ() + e, 0, d, 0.0, e, 0.0);
        }
    }

    @WrapWithCondition(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;spawnSweepAttackParticles()V")
    )
    private boolean preventDefaultSweepParticle(PlayerEntity instance, @Local ItemStack weapon) {
        return !weapon.isOf(BlackKnifeItems.BLACK_KNIFE);
   }
}
