package com.mygdx.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.Data;
import com.mygdx.GCStage;
import com.mygdx.entities.helpers.GameActor;
import com.mygdx.hitboxes.Hitbox;
import com.mygdx.hitboxes.Tags;
import com.mygdx.resources.RM;
import com.mygdx.resources.enums.AtlasEnum;
import com.mygdx.resources.enums.TextureEnum;

public class Scope extends GameActor {
    public boolean hitplayer = false;
    private final Sprite sprite;
    private Hitbox hitbox = new Hitbox();
    private boolean isFighting = GCStage.get().getPlayer().isFighting();


    public Scope(Vector2 coords) {
        this.sprite = RM.get().getSpriteFromAtlas(AtlasEnum.OTHERS, TextureEnum.SCOPE);

        setSize(sprite.getWidth(), sprite.getHeight());
        setOrigin(getWidth() / 2, getHeight() / 2);

        hitbox = new Hitbox(center, getWidth(), getHeight(), 0, true);
        hitbox.setTags(Tags.SCOPE);
        hitbox.setOnFrame((collider) -> {
            if (!hitbox.touching(collider))
                return;
            if (!collider.containsTag(Tags.PLAYER))
                return;
            hitplayer = true;
        });
        hitbox.setOnLeave(collider -> hitplayer = false);
        hitbox.register();

        if (Data.HITBOX_DEBUG) debug();
        setCoords(coords);
    }

    public Scope(float x, float y) {
        this(new Vector2(x, y));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isFighting) return;
        super.draw(batch, parentAlpha);
        batch.draw(sprite, center.x - getOriginX(), center.y - getOriginY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        isFighting = GCStage.get().getPlayer().isFighting();
        if (!isFighting) return;

        addAction(Actions.moveTo(GCStage.get().getPlayer().center.x - getOriginX(), GCStage.get().getPlayer().center.y - getOriginY(), 2));
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        hitbox.setPosition();
    }

    @Override
    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.polygon(hitbox.getTransformedVertices());
    }
}
