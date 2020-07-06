package com.rebane2001.cr3stal.mixin;

import com.rebane2001.cr3stal.Util;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Quaternion;
import org.spongepowered.asm.mixin.*;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static net.minecraft.client.render.entity.EndCrystalEntityRenderer.getYOffset;

@Mixin(EndCrystalEntityRenderer.class)
public abstract class EndCrystalEntityRendererMixin extends EntityRenderer<EndCrystalEntity> {

    protected EndCrystalEntityRendererMixin(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Shadow @Final private static Identifier TEXTURE = new Identifier("textures/entity/end_crystal/end_crystal.png");
    @Shadow @Final private static RenderLayer END_CRYSTAL;
    @Shadow @Final private static float SINE_45_DEGREES;
    @Shadow @Final private ModelPart core;
    @Shadow @Final private ModelPart frame;
    @Shadow @Final private ModelPart bottom;

    // Currently rotating side
    private int rotatingSide = 0;
    // front - 0
    // back - 1
    // top - 2
    // bottom - 3
    // left - 4
    // right - 5

    private long lastTime = 0;

    private static final int ANIMATION_LENGTH = 400;
    private static final float CUBELET_SCALE = 0.4f;

    // This Mixin is sloppy because it's based on the 1.12 version which was completely different
    @Overwrite
    public void render(EndCrystalEntity endCrystalEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        float h = getYOffset(endCrystalEntity, g);
        float j = ((float)endCrystalEntity.endCrystalAge + g) * 3.0F;
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(END_CRYSTAL);
        matrixStack.push();
        matrixStack.scale(2.0F, 2.0F, 2.0F);
        matrixStack.translate(0.0D, -0.5D, 0.0D);
        int k = OverlayTexture.DEFAULT_UV;
        if (endCrystalEntity.getShowBottom()) {
            this.bottom.render(matrixStack, vertexConsumer, i, k);
        }

        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(j));
        //matrixStack.translate(0.0D, (double)(1.5F + h / 2.0F), 0.0D);
        matrixStack.translate(0.0D,  (endCrystalEntity.getShowBottom()) ? 1.2D : 1D, 0.0D);
        matrixStack.multiply(new Quaternion(new Vector3f(SINE_45_DEGREES, 0.0F, SINE_45_DEGREES), 60.0F, true));
        // Hide one of the frames/glass
        //this.frame.render(matrixStack, vertexConsumer, i, k);
        float l = 0.875F;
        matrixStack.scale(0.875F, 0.875F, 0.875F);
        matrixStack.multiply(new Quaternion(new Vector3f(SINE_45_DEGREES, 0.0F, SINE_45_DEGREES), 60.0F, true));
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(j));
        this.frame.render(matrixStack, vertexConsumer, i, k);
        matrixStack.scale(0.875F, 0.875F, 0.875F);
        matrixStack.multiply(new Quaternion(new Vector3f(SINE_45_DEGREES, 0.0F, SINE_45_DEGREES), 60.0F, true));
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(j));

        long currentTime = System.currentTimeMillis();
        if (currentTime-ANIMATION_LENGTH > lastTime){
            // rotate sides and corners
            int[] currentSide = Util.cubeSides[rotatingSide];
            Quaternion[] cubletsTemp = {
                    Util.cubeletStatus[currentSide[0]],
                    Util.cubeletStatus[currentSide[1]],
                    Util.cubeletStatus[currentSide[2]],
                    Util.cubeletStatus[currentSide[3]],
                    Util.cubeletStatus[currentSide[4]],
                    Util.cubeletStatus[currentSide[5]],
                    Util.cubeletStatus[currentSide[6]],
                    Util.cubeletStatus[currentSide[7]],
                    Util.cubeletStatus[currentSide[8]]
            };

            // rotation direction
            if (true) {
                Util.cubeletStatus[currentSide[0]] = cubletsTemp[6];
                Util.cubeletStatus[currentSide[1]] = cubletsTemp[3];
                Util.cubeletStatus[currentSide[2]] = cubletsTemp[0];
                Util.cubeletStatus[currentSide[3]] = cubletsTemp[7];
                Util.cubeletStatus[currentSide[4]] = cubletsTemp[4];
                Util.cubeletStatus[currentSide[5]] = cubletsTemp[1];
                Util.cubeletStatus[currentSide[6]] = cubletsTemp[8];
                Util.cubeletStatus[currentSide[7]] = cubletsTemp[5];
                Util.cubeletStatus[currentSide[8]] = cubletsTemp[2];
            } else {
                Util.cubeletStatus[currentSide[0]] = cubletsTemp[2];
                Util.cubeletStatus[currentSide[1]] = cubletsTemp[5];
                Util.cubeletStatus[currentSide[2]] = cubletsTemp[8];
                Util.cubeletStatus[currentSide[3]] = cubletsTemp[1];
                Util.cubeletStatus[currentSide[4]] = cubletsTemp[4];
                Util.cubeletStatus[currentSide[5]] = cubletsTemp[7];
                Util.cubeletStatus[currentSide[6]] = cubletsTemp[0];
                Util.cubeletStatus[currentSide[7]] = cubletsTemp[3];
                Util.cubeletStatus[currentSide[8]] = cubletsTemp[6];
            }
            int[] trans = Util.cubeSideTransforms[rotatingSide];
            for (int x = -1; x < 2; x++){
                for (int y = -1; y < 2; y++) {
                    for (int z = -1; z < 2; z++) {
                        if (x != 0 || y != 0 || z != 0)
                            applyCubeletRotation(x, y, z, trans[0], trans[1], trans[2]);
                    }
                }
            }
            rotatingSide = ThreadLocalRandom.current().nextInt(0, 5 + 1);
            lastTime = currentTime;
        }

        matrixStack.scale(CUBELET_SCALE, CUBELET_SCALE, CUBELET_SCALE);

        // Draw non-rotating cubes
        for (int x = -1; x < 2; x++){
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    if (x != 0 || y != 0 || z != 0)
                        drawCubeletStatic(matrixStack, vertexConsumer, i, k, x, y, z);
                }
            }
        }

        // Draw rotating cubes
        int[] trans = Util.cubeSideTransforms[rotatingSide];
        matrixStack.push();
        matrixStack.translate(trans[0]*CUBELET_SCALE, trans[1]*CUBELET_SCALE, trans[2]*CUBELET_SCALE);
        //GlStateManager.rotate((currentTime - lastTime) * 90 / ANIMATION_LENGTH, trans[0], trans[1], trans[2]);
        float RotationAngle = (float) Math.toRadians(Util.easeInOutCubic(((float)(currentTime - lastTime)) / ANIMATION_LENGTH)*90);
        float xx = (float) (trans[0] * Math.sin(RotationAngle / 2));
        float yy = (float) (trans[1] * Math.sin(RotationAngle / 2));
        float zz = (float) (trans[2] * Math.sin(RotationAngle / 2));
        float ww = (float) Math.cos(RotationAngle / 2);
        Quaternion qq = new Quaternion(xx,yy,zz,ww);
        matrixStack.multiply(qq);
        for (int x = -1; x < 2; x++){
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    if (x != 0 || y != 0 || z != 0)
                        drawCubeletRotating(matrixStack, vertexConsumer, i, k, x, y, z);
                }
            }
        }
        matrixStack.pop();

        //this.core.render(matrixStack, vertexConsumer, i, k);
        matrixStack.pop();
        matrixStack.pop();
        BlockPos blockPos = endCrystalEntity.getBeamTarget();
        if (blockPos != null) {
            float m = (float)blockPos.getX() + 0.5F;
            float n = (float)blockPos.getY() + 0.5F;
            float o = (float)blockPos.getZ() + 0.5F;
            float p = (float)((double)m - endCrystalEntity.getX());
            float q = (float)((double)n - endCrystalEntity.getY());
            float r = (float)((double)o - endCrystalEntity.getZ());
            matrixStack.translate((double)p, (double)q, (double)r);
            EnderDragonEntityRenderer.renderCrystalBeam(-p, -q + h, -r, g, endCrystalEntity.endCrystalAge, matrixStack, vertexConsumerProvider, i);
        }

        super.render(endCrystalEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private void drawCubeletStatic(MatrixStack matrixStack, VertexConsumer vertexConsumer, int i, int k, int x, int y, int z){
        int cubletId = Util.cubletLookup[x+1][y+1][z+1];
        if (Arrays.stream(Util.cubeSides[rotatingSide]).anyMatch(r -> r == cubletId))
            return;
        drawCubelet(matrixStack, vertexConsumer, i, k, x, y, z, cubletId);
    }

    private void drawCubeletRotating(MatrixStack matrixStack, VertexConsumer vertexConsumer, int i, int k, int x, int y, int z){
        int cubletId = Util.cubletLookup[x+1][y+1][z+1];
        if (!Arrays.stream(Util.cubeSides[rotatingSide]).anyMatch(r -> r == cubletId))
            return;
        int[] trans = Util.cubeSideTransforms[rotatingSide];
        drawCubelet(matrixStack, vertexConsumer, i, k, x - trans[0], y - trans[1], z - trans[2], cubletId);
    }

    private void applyCubeletRotation(int x, int y, int z, int rX, int rY, int rZ){
        int cubletId = Util.cubletLookup[x+1][y+1][z+1];
        if (!Arrays.stream(Util.cubeSides[rotatingSide]).anyMatch(r -> r == cubletId))
            return;
        float RotationAngle = (float) Math.toRadians(90);
        float xx = (float) (rX * Math.sin(RotationAngle / 2));
        float yy = (float) (rY * Math.sin(RotationAngle / 2));
        float zz = (float) (rZ * Math.sin(RotationAngle / 2));
        float ww = (float) Math.cos(RotationAngle / 2);
        Quaternion tempQuat = new Quaternion(xx,yy,zz,ww);
        tempQuat.hamiltonProduct(Util.cubeletStatus[cubletId]);
        Util.cubeletStatus[cubletId] = tempQuat;
    }

    private void drawCubelet(MatrixStack matrixStack, VertexConsumer vertexConsumer, int i, int k, int x, int y, int z, int cubletId){
        matrixStack.push();
        matrixStack.translate(x*CUBELET_SCALE, y*CUBELET_SCALE, z*CUBELET_SCALE);
        matrixStack.push();
        matrixStack.multiply(Util.cubeletStatus[cubletId]);
        matrixStack.scale(0.8f, 0.8f, 0.8f);
        this.core.render(matrixStack, vertexConsumer, i, k);
        matrixStack.pop();
        matrixStack.pop();
    }

}
