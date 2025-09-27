package archives.tater.blackknife.mixin.client;

import archives.tater.blackknife.BlackKnifeClient;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LimbAnimator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow
    public boolean handSwinging;

    @Shadow
    @Final
    public LimbAnimator limbAnimator;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @SuppressWarnings("ConstantValue")
    @ModifyArg(
            method = "updateLimbs(Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateLimbs(F)V")
    )
    private float preventMoveLimbs(float posDelta) {
        return (Object) this instanceof PlayerEntity playerEntity && playerEntity.getAbilities().flying && BlackKnifeClient.hasAfterimages(this) ? 0f : posDelta;
    }

    @Inject(
            method = "swingHand(Lnet/minecraft/util/Hand;)V",
            at = @At("TAIL")
    )
    private void moveLimbsOnAttack(Hand hand, CallbackInfo ci) {
        if (hand == Hand.MAIN_HAND)
            limbAnimator.setSpeed(3f);
    }
}
