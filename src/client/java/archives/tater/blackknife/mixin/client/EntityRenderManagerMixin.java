package archives.tater.blackknife.mixin.client;

import archives.tater.blackknife.BlackKnifeClient;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;

import static archives.tater.blackknife.BlackKnifeClient.*;
import static java.lang.Math.clamp;

@Mixin(EntityRenderManager.class)
public class EntityRenderManagerMixin {
    @WrapOperation(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V")
    )
    private <S extends EntityRenderState> void renderAfterimages(EntityRenderer<?, ? super S> instance, S renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState, Operation<Void> original) {
        original.call(instance, renderState, matrices, queue, cameraRenderState);
        var afterimages = renderState.getData(BlackKnifeClient.AFTERIMAGES);
        if (afterimages == null) return;
        for (var afterimage : afterimages) {
            matrices.push();
            var ageDiff = renderState.age - afterimage.age;
            matrices.translate(afterimage.x - renderState.x - 0.125 * ageDiff, afterimage.y - renderState.y, afterimage.z - renderState.z);
            BlackKnifeClient.opacity = clamp((int) (MAX_AFTERIMAGE_OPACITY * (1 - ageDiff / (AFTERIMAGE_COUNT * AFTERIMAGE_RATE))), 0, 255);
            original.call(instance, afterimage, matrices, queue, cameraRenderState);
            BlackKnifeClient.opacity = -1;
            matrices.pop();
        }
    }
}
