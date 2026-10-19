package com.mygdx.entities.helpers;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.AnimationManager;
import com.mygdx.map.MapManager;
import com.mygdx.movement.AutoMovementManager;
import com.mygdx.screens.ScreenManager;

public class GameActor extends Actor implements Telegraph {

    protected final AutoMovementManager autoMovementManager = new AutoMovementManager(this);
    public Vector2 center = new Vector2();
    protected AnimationManager animationManager;

    @Override
    protected void positionChanged() {
        super.positionChanged();
        center.x = getX() + getOriginX();
        center.y = getY() + getOriginY();
    }

    public void setCoords(float x, float y) {
        setX(x);
        setY(y);
    }

    public Vector2 getCoords() {
        return new Vector2(getX(), getY());
    }

    public void setCoords(Vector2 coords) {
        setX(coords.x);
        setY(coords.y);
    }

    public void movAbs(Vector2 coords) {
        autoMovementManager.goTo(coords);
    }

    public void movAbs(Vector2 coords, Runnable onEnd) {
        autoMovementManager.goTo(coords, onEnd);
    }

    public void movAbs(float x, float y) {
        autoMovementManager.goTo(new Vector2(x, y));
    }

    public void movAbs(float x, float y, Runnable onEnd) {
        autoMovementManager.goTo(new Vector2(x, y), onEnd);
    }

    public void movAbs(String destination) {
        Vector2 coords = MapManager.get().getMarker(destination);

        if (coords == null) return;
        movAbs(coords);
    }

    public void movRel(Vector2 coords) {
        autoMovementManager.goTo(this.getCoords().add(coords));
    }

    public void movRel(float x, float y) {
        autoMovementManager.goTo(this.getCoords().add(new Vector2(x, y)));
    }


    public boolean isAutoWalking() {
        return autoMovementManager.isAnimationInProgress();
    }


    @Override
    public boolean handleMessage(Telegram msg) {
        return true;
    }
}
