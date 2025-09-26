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

import java.util.List;

import static java.lang.Math.max;

@Mixin(ItemRenderState.LayerRenderState.class)
public class ItemRenderStateMixin {
    @WrapOperation(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitItem(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemDisplayContext;III[ILjava/util/List;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/item/ItemRenderState$Glint;)V")
    )
    private void useOpacity(
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
        var opacity = BlackKnifeClient.opacity;
        if (opacity < 0) {
            original.call(instance, matrices, displayContext, light, overlay, outlineColors, tintLayers, quads, renderLayer, glintType);
            return;
        }

        var newTints = new int[max(tintLayers.length, 1)];
        for (int i = 0; i < newTints.length; i++) {
            newTints[i] = opacity << (6 * 4) + (i < tintLayers.length ? tintLayers[i] & 0xffffff : 0xffffff);
        }

        original.call(instance, matrices, displayContext, light, overlay, outlineColors, newTints, quads, TexturedRenderLayers.getItemEntityTranslucentCull(), glintType);
    }
}
