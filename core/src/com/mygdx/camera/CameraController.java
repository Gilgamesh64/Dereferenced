package com.mygdx.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.Data;
import com.mygdx.GCStage;

public class CameraController {
    public static boolean stopCamera = false;
    private static final float lerpFactor = 5;
    private static CameraShaker cameraShaker;
    private static Vector2 mouseDirection;
    private static float mouseAngle;
    private static Vector2 xDirection;
    private static float xAngle;

    public static void initCamera() {
        OrthographicCamera gameCamera = (OrthographicCamera) GCStage.get().getCamera();
        float shakeRadius = 15f;
        float minimumShakeRadius = 3f;
        float radiusFallOffFactor = 0.50f;
        cameraShaker = new CameraShaker(gameCamera, shakeRadius, minimumShakeRadius, radiusFallOffFactor);
    }

    public static void updateCamera() {

        if (Gdx.input.isKeyJustPressed(Keys.SHIFT_RIGHT)) {
            CameraController.stopCamera = !CameraController.stopCamera;
        }

        OrthographicCamera camera = (OrthographicCamera) GCStage.get().getCamera();
        Vector3 cameraPosition = camera.position;
        if (CameraController.stopCamera) {
            if (Gdx.input.isKeyPressed(Keys.UP)) {
                cameraPosition.add(0f, 1f, 0f);
            }
            if (Gdx.input.isKeyPressed(Keys.DOWN)) {
                cameraPosition.add(0f, -1f, 0f);
            }
            if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                cameraPosition.add(-1f, 0f, 0f);
            }
            if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                cameraPosition.add(1f, 0f, 0f);
            }

            if (Gdx.input.isKeyPressed(Keys.NUMPAD_8)) {
                camera.zoom -= 0.005F;
            }
            if (Gdx.input.isKeyPressed(Keys.NUMPAD_2)) {
                camera.zoom += 0.005F;
            }
            return;
        }

        cameraShaker.origPosition = cameraPosition.cpy();
        cameraShaker.update(Gdx.graphics.getDeltaTime());

        cameraPosition.x += (GCStage.get().getPlayer().center.x - cameraPosition.x) * lerpFactor
                * Gdx.graphics.getDeltaTime();
        cameraPosition.y += (GCStage.get().getPlayer().center.y - cameraPosition.y) * lerpFactor
                * Gdx.graphics.getDeltaTime();

        // Apply the changes
        camera.position.set(cameraPosition);
        camera.update();
    }

    public static void applyShakeEffect() {
        cameraShaker.startShaking();
    }

    public static void calculateMouseAngle(Vector2 position) {
        float mX = Gdx.input.getX(), mY = Gdx.input.getY();
        Vector2 tmp = GCStage.get().getViewport().unproject(new Vector2(mX, mY));
        Vector2 dir = new Vector2();
        dir.set(tmp).sub(position.x, position.y).nor();
        mouseDirection = dir;
        mouseAngle = dir.angleDeg();
    }

    public static void calculateThowardsPos(Vector2 position1, Vector2 position2) {
        Vector2 dir = new Vector2();
        dir.set(position2).sub(position1.x, position1.y).nor();
        xDirection = dir;
        xAngle = dir.angleDeg();
    }

    public static float getAngle(Vector2 position1, Vector2 position2) {
        calculateThowardsPos(position1, position2);
        return getXAngle();
    }

    public static Vector2 getMouseDirection() {
        return mouseDirection;
    }

    public static float getMouseAngle() {
        return mouseAngle;
    }

    public static Vector2 getXDirection() {
        return xDirection;
    }

    public static float getXAngle() {
        return xAngle;
    }
}
