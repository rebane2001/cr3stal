package com.rebane2001.cr3stal;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Quaternion;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class RubicsCubeRenderer {

    private static final int ANIMATION_LENGTH = 400;
    private static final float CUBELET_SCALE = 0.4f;

    // Currently rotating side
    private int rotatingSide = 0;
    // front - 0
    // back - 1
    // top - 2
    // bottom - 3
    // left - 4
    // right - 5

    private long lastTime = 0;

    public void render(ModelPart core, MatrixStack matrices, VertexConsumer vertices, int light, int overlay) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - ANIMATION_LENGTH > lastTime) {
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
            Util.cubeletStatus[currentSide[0]] = cubletsTemp[6];
            Util.cubeletStatus[currentSide[1]] = cubletsTemp[3];
            Util.cubeletStatus[currentSide[2]] = cubletsTemp[0];
            Util.cubeletStatus[currentSide[3]] = cubletsTemp[7];
            Util.cubeletStatus[currentSide[4]] = cubletsTemp[4];
            Util.cubeletStatus[currentSide[5]] = cubletsTemp[1];
            Util.cubeletStatus[currentSide[6]] = cubletsTemp[8];
            Util.cubeletStatus[currentSide[7]] = cubletsTemp[5];
            Util.cubeletStatus[currentSide[8]] = cubletsTemp[2];

            int[] trans = Util.cubeSideTransforms[rotatingSide];
            for (int x = -1; x < 2; x++) {
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

        matrices.scale(CUBELET_SCALE, CUBELET_SCALE, CUBELET_SCALE);

        // Draw non-rotating cubes
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    if (x != 0 || y != 0 || z != 0)
                        drawCubeletStatic(core, matrices, vertices, light, overlay, x, y, z);
                }
            }
        }

        // Draw rotating cubes
        int[] trans = Util.cubeSideTransforms[rotatingSide];
        matrices.push();
        matrices.translate(trans[0] * CUBELET_SCALE, trans[1] * CUBELET_SCALE, trans[2] * CUBELET_SCALE);
        //GlStateManager.rotate((currentTime - lastTime) * 90 / ANIMATION_LENGTH, trans[0], trans[1], trans[2]);
        float RotationAngle = (float) Math.toRadians(Util.easeInOutCubic(((float) (currentTime - lastTime)) / ANIMATION_LENGTH) * 90);
        float xx = (float) (trans[0] * Math.sin(RotationAngle / 2));
        float yy = (float) (trans[1] * Math.sin(RotationAngle / 2));
        float zz = (float) (trans[2] * Math.sin(RotationAngle / 2));
        float ww = (float) Math.cos(RotationAngle / 2);
        Quaternion qq = new Quaternion(xx, yy, zz, ww);
        matrices.multiply(qq);
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    if (x != 0 || y != 0 || z != 0)
                        drawCubeletRotating(core, matrices, vertices, light, overlay, x, y, z);
                }
            }
        }
        matrices.pop();
    }

    private void drawCubeletStatic(ModelPart core, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int x, int y, int z) {
        int cubletId = Util.cubletLookup[x + 1][y + 1][z + 1];

        if (Arrays.stream(Util.cubeSides[rotatingSide]).anyMatch(r -> r == cubletId))
            return;

        drawCubelet(core, matrices, vertices, light, overlay, x, y, z, cubletId);
    }

    private void drawCubeletRotating(ModelPart core, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int x, int y, int z) {
        int cubletId = Util.cubletLookup[x + 1][y + 1][z + 1];

        if (Arrays.stream(Util.cubeSides[rotatingSide]).noneMatch(r -> r == cubletId))
            return;

        int[] trans = Util.cubeSideTransforms[rotatingSide];
        drawCubelet(core, matrices, vertices, light, overlay, x - trans[0], y - trans[1], z - trans[2], cubletId);
    }

    private void applyCubeletRotation(int x, int y, int z, int rX, int rY, int rZ) {
        int cubletId = Util.cubletLookup[x + 1][y + 1][z + 1];

        if (Arrays.stream(Util.cubeSides[rotatingSide]).noneMatch(r -> r == cubletId))
            return;

        float RotationAngle = (float) Math.toRadians(90);
        float xx = (float) (rX * Math.sin(RotationAngle / 2));
        float yy = (float) (rY * Math.sin(RotationAngle / 2));
        float zz = (float) (rZ * Math.sin(RotationAngle / 2));
        float ww = (float) Math.cos(RotationAngle / 2);
        Quaternion tempQuat = new Quaternion(xx, yy, zz, ww);
        tempQuat.hamiltonProduct(Util.cubeletStatus[cubletId]);
        Util.cubeletStatus[cubletId] = tempQuat;
    }

    private void drawCubelet(ModelPart core, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int x, int y, int z, int cubletId) {
        matrices.push();
        matrices.translate(x * CUBELET_SCALE, y * CUBELET_SCALE, z * CUBELET_SCALE);
        matrices.push();
        matrices.multiply(Util.cubeletStatus[cubletId]);
        matrices.scale(0.8f, 0.8f, 0.8f);

        core.render(matrices, vertices, light, overlay);

        matrices.pop();
        matrices.pop();
    }
}