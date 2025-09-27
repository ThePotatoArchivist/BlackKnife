package archives.tater.blackknife.mixin.client;

import archives.tater.blackknife.BlackKnifeClient;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.Colors;

import java.util.List;

import static java.lang.Math.max;
import static net.minecraft.util.math.ColorHelper.withAlpha;

@Mixin(ItemRenderState.LayerRenderState.class)
public class ItemRenderStateMixin {
    @WrapOperation(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitItem(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemDisplayContext;III[ILjava/util/List;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/item/ItemRenderState$Glint;)V")
    )
    private void applyAlpha(
            OrderedRenderCommandQueue instance,
            MatrixStack matrices,
            ItemDisplayContext displayContext,
            int light,
            int overlay,
            int outlineColors,
            int[] tintLayers,
            List<BakedQuad> quads,
            RenderLayer renderLayer,
            ItemRenderState.Glint glintType,
            Operation<Void> original
    ) {
        var alpha = BlackKnifeClient.alpha;
        if (alpha < 0) {
            original.call(instance, matrices, displayContext, light, overlay, outlineColors, tintLayers, quads, renderLayer, glintType);
            return;
        }

        var newTints = new int[max(tintLayers.length, 1)];
        for (int i = 0; i < newTints.length; i++) {
            newTints[i] = withAlpha(alpha, i < tintLayers.length ? tintLayers[i] : Colors.WHITE);
        }

        original.call(instance, matrices, displayContext, light, overlay, outlineColors, newTints, quads, TexturedRenderLayers.getItemEntityTranslucentCull(), glintType);
    }
}
