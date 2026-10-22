package com.mygdx.entities.helpers;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.mygdx.AnimationManager;
import com.mygdx.map.MapManager;
import com.mygdx.movement.AutoMovementManager;

public class GameActor extends Actor implements Telegraph {

    public Vector2 center = new Vector2();
    protected final AutoMovementManager autoMovementManager = new AutoMovementManager(this);
    protected AnimationManager animationManager;

    @Override
    protected void positionChanged() {
        super.positionChanged();
        center.x = getX(Align.center);
        center.y = getY(Align.center);
    }

    public void setCoords(float x, float y) {
        setX(x);
        setY(y);
        center.x = getX(Align.center);
        center.y = getY(Align.center);
    }

    public Vector2 getCoords() {
        return new Vector2(getX(), getY());
    }

    public void setCoords(Vector2 coords) {
        setCoords(coords.x, coords.y);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("coords: (x,y) = (").append(getX() + "," + getY());
        sb.append("center: (x,y) = (").append(center.x + "," + center.y);
        sb.append("is auto walking: ").append(isAutoWalking());
        return sb.toString();
    }
}
