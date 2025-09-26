package archives.tater.blackknife.mixin.client;

import archives.tater.blackknife.BlackKnifeClient;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.entity.LivingEntityRenderer;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @ModifyExpressionValue(
            method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;isVisible(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;)Z")
    )
    private boolean notOpaque(boolean original) {
        return original && BlackKnifeClient.opacity < 0;
    }

    @ModifyExpressionValue(
            method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;invisibleToPlayer:Z")
    )
    private boolean useTranslucent(boolean original) {
        return original && BlackKnifeClient.opacity < 0;
    }

    @ModifyExpressionValue(
            method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
            at = @At(value = "CONSTANT", args = "intValue=654311423")
    )
    private int useOpacity(int original) {
        return BlackKnifeClient.opacity < 0 ? original : (BlackKnifeClient.opacity << 6 * 4) + 0xFFFFFF;
    }
}
