package com.rebane2001.cr3stal.mixin;


import com.rebane2001.cr3stal.RubiksCubeRenderer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.EndCrystalEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(EndCrystalEntityRenderer.class)
public abstract class EndCrystalEntityRendererMixin {
    @Unique
    private RubiksCubeRenderer cachedRubiksCubeRenderer = null;
    
    @Redirect(method = "render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
              at = @At(value = "INVOKE", target = "net/minecraft/client/util/math/MatrixStack.translate(DDD)V", ordinal = 1))
    public void translate(MatrixStack translateMatrixStack, double x, double y, double z, EndCrystalEntity crystalEntity, float f,
                          float g, MatrixStack renderMatrixStack /* same object as translateMatrixStack */,
                          VertexConsumerProvider vertexConsumer, int i) {
        translateMatrixStack.translate(x, crystalEntity.shouldShowBottom() ? 1.2 : 1.0, z);
    }
    
    @Redirect(method = "render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
              at = @At(value = "INVOKE",
                       target = "net/minecraft/client/model/ModelPart.render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V",
                       ordinal = 1))
    public void renderFrame(ModelPart modelPart, MatrixStack matrices, VertexConsumer vertices, int light, int overlay) {
        // Empty to replace default rendering
    }
    
    @Redirect(method = "render(Lnet/minecraft/entity/decoration/EndCrystalEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
              at = @At(value = "INVOKE",
                       target = "net/minecraft/client/model/ModelPart.render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V",
                       ordinal = 3))
    public void renderCore(ModelPart core, MatrixStack matrices, VertexConsumer vertices, int light, int overlay,
                           EndCrystalEntity endCrystalEntity, float f, float g, MatrixStack matrixStack,
                           VertexConsumerProvider vertexConsumer, int i) {
        getRubiksCubeRenderer().render(core, matrices, vertices, light, overlay);
    }
    
    private RubiksCubeRenderer getRubiksCubeRenderer() {
        if (cachedRubiksCubeRenderer == null) {
            cachedRubiksCubeRenderer = new RubiksCubeRenderer();
        }
        
        return cachedRubiksCubeRenderer;
    }
}
