package com.mygdx.gunControls.guns;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.GCStage;
import com.mygdx.camera.CameraController;
import com.mygdx.messages.MSG;
import com.mygdx.movement.BaseMovement;

public class BaseGun extends Actor {
    protected Sprite s;
    protected BaseMovement movement;
    protected float angleOffset;
    protected boolean flipped, stop = false;

    public BaseGun(Sprite s, Vector2 origin, float angleOffset) {
        this.s = s;
        flipped = true;
        setSize(s.getWidth(), s.getHeight());
        movement = new BaseMovement(getWidth() / 2, getHeight() / 2);
        movement.anchor(origin);
        s.setOrigin(movement.origin.x, movement.origin.y);

        this.angleOffset = angleOffset;
    }

    public BaseGun() {
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        s.draw(batch);
    }

    @Override
    public void act(float delta) {
        Vector2 worldCoords = movement.getWorldCoords();
        setPosition(worldCoords.x, worldCoords.y);

        float angle = CameraController.getMouseAngle();

        boolean left = angle > 90 && angle <= 270;
        boolean right = (angle <= 90 || angle > 270);

        boolean flipLeft = flipped && left;
        boolean flipRight = !flipped && right;

        if (flipLeft || flipRight) {
            s.flip(false, true);
            angleOffset = -angleOffset;
            flipped = !flipped;
        }

        float calculatedAngle = angle + angleOffset;
        if (angleOffset != 0 && stop) {
            boolean calculatedLeft = calculatedAngle > 90 && calculatedAngle <= 270;
            boolean calculatedRight = (calculatedAngle <= 90 || calculatedAngle > 270);

            boolean lockAtLeft = left && calculatedRight;
            boolean lockAtRight = right && calculatedLeft;

            if (lockAtLeft)
                calculatedAngle = angleOffset < 0 ? 90 : 270;
            if (lockAtRight)
                calculatedAngle = angleOffset < 0 ? 270 : 90;
        }

        s.setRotation(calculatedAngle);
    }

    @Override
    protected void positionChanged() {
        s.setPosition(getX(), getY());
    }

    public void onCurrent() {
    }
    
    public void onSwitched() {
    }

    public void leftTrigger() {
        GCStage.get().send(MSG.SHOT);
    }

    public void rightTrigger() {
        GCStage.get().send(MSG.SHOT);
    }

    public void middleTrigger() {
        GCStage.get().send(MSG.SHOT);
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public void flip(boolean x, boolean y) {
        s.flip(x, y);
    }

    public void setOffset(float x, float y, float angle) {
        setOffset(new Vector2(x, y), angle);
    }

    public void setOffset(Vector2 offset, float angle) {
        movement.offset(offset, angle);
        s.setOrigin(movement.origin.x, movement.origin.y);
    }

    public void destroy() {
    }
}
