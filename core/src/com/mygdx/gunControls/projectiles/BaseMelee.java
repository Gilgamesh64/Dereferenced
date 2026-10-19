package com.mygdx.gunControls.projectiles;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.hitboxes.Collider;
import com.mygdx.hitboxes.Tags;
import com.mygdx.messages.ObjectInfo;
import com.mygdx.movement.BaseMovement;

public class BaseMelee extends Actor {
    protected Sprite s;
    protected BaseMovement movement;
    protected float angle, speed, end;
    protected Collider collider = new Collider();
    protected ObjectInfo info;
    private final boolean flipped;

    public BaseMelee(Sprite s, Vector2 origin, float angle, float arc, float speed, boolean flipped) {
        this.s = s;

        this.angle = angle;
        this.speed = speed;
        this.flipped = flipped;

        if (flipped) {
            s.flip(false, true);
            arc = arc * -1;
            this.speed = speed * -1;
        }
        end = angle + arc;

        setSize(s.getWidth(), s.getHeight());
        setOrigin(getWidth() / 2, getHeight() / 2);

        movement = new BaseMovement(getOriginX(), getOriginY());
        movement.anchor(origin);
        s.setOrigin(movement.origin.x, movement.origin.y);

        collider = new Collider(origin, getWidth(), getHeight(), angle);
        collider.setTags(Tags.PROJECTILE);
        collider.setSearchTags(Tags.ENEMY);
        collider.setOnHit(hitbox -> delete());
        collider.register();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        s.draw(batch);
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.polygon(collider.getTransformedVertices());
    }

    @Override
    public void act(float delta) {
        Vector2 worldCoords = movement.getWorldCoords();
        setPosition(worldCoords.x, worldCoords.y);

        angle += speed * delta;

        s.setRotation(angle);
        collider.setRotation(angle);

        if (flipped) {
            if (angle < end)
                delete();
        } else if (angle > end)
            delete();
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        s.setPosition(getX(), getY());
        collider.setPosition();
    }

    protected void delete() {
        this.remove();
        collider.unregister();
    }

    public void setOffset(float x, float y, float angle) {
        setOffset(new Vector2(x, y), angle);
    }

    public void setOffset(Vector2 offset, float angle) {
        movement.offset(offset, angle);
        s.setOrigin(movement.origin.x, movement.origin.y);
        collider.setOffset(offset, angle);
    }

    /**
     * Creates the additional info object instance with the specified maps to
     * initialize, then, attaches it to the collider.
     *
     * <pre>
     * 1: Boolean Map.
     * 2: Integer Map.
     * 3: Float Map.
     * 4: String Map.
     * </pre>
     *
     * @param selector array of numbers.
     */
    protected void attachInfo(int... selector) {
        info = new ObjectInfo(selector);
        collider.setExtraInfo(info);
    }
}
