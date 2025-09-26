package archives.tater.blackknife.mixin.client;

import archives.tater.blackknife.BlackKnifeClient;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;

import static archives.tater.blackknife.BlackKnifeClient.AFTERIMAGE_RATE;
import static net.minecraft.util.math.MathHelper.sin;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
	@ModifyReturnValue(
            method = "getAndUpdateRenderState",
            at = @At("RETURN")
    )
	private S attachAfterimages(S original, @Local(argsOnly = true) T entity) {
        if (!BlackKnifeClient.hasAfterimages(entity)) return original;
        var afterimages = BlackKnifeClient.getAfterimages(entity);
        original.setData(BlackKnifeClient.AFTERIMAGES, afterimages.toArray(new EntityRenderState[0]));
        if (afterimages.isEmpty() || (int) original.age / AFTERIMAGE_RATE != (int) afterimages.getFirst().age / AFTERIMAGE_RATE)
            BlackKnifeClient.addAfterimage(afterimages, original);
        return original;
    }

    @ModifyExpressionValue(
            method = "updateRenderState",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(DDD)D", ordinal = 1)
    )
    private double bob(double original, @Local(argsOnly = true) T entity, @Local(argsOnly = true) float tickProgress) {
        return BlackKnifeClient.hasAfterimages(entity) ? original + 0.125 * (1 + sin(0.1875f * (entity.age + tickProgress))) : original;
    }
}