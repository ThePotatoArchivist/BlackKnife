package archives.tater.blackknife.mixin.client;

import archives.tater.blackknife.BlackKnifeClient;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.util.Colors;

import static net.minecraft.util.math.ColorHelper.withAlpha;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @ModifyExpressionValue(
            method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;isVisible(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;)Z")
    )
    private boolean notOpaque(boolean original) {
        return original && BlackKnifeClient.alpha < 0;
    }

    @ModifyExpressionValue(
            method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;invisibleToPlayer:Z")
    )
    private boolean useTranslucent(boolean original) {
        return original && BlackKnifeClient.alpha < 0;
    }

    @ModifyExpressionValue(
            method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
            at = @At(value = "CONSTANT", args = "intValue=654311423")
    )
    private int applyAlpha(int original) {
        return BlackKnifeClient.alpha < 0 ? original : withAlpha(BlackKnifeClient.alpha, Colors.WHITE);
    }
}
