package com.mygdx.entities.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.AnimationManager;
import com.mygdx.entities.helpers.GameActor;
import com.mygdx.resources.enums.AnimationEnum;
import com.mygdx.resources.enums.AtlasEnum;
import com.mygdx.resources.enums.TextureEnum;

public class Component extends GameActor {

    protected final AnimationManager animationManager;

    public Component(Vector2 coords, TextureEnum... textures) {
        super();
        setX(coords.x);
        setY(coords.y);

        animationManager = new AnimationManager(AtlasEnum.COMPONENTS, 0.2f, textures);
    }
    public Component(float x, float y, TextureEnum... textures){
        this(new Vector2(x, y), textures);
    }

    public Component(Vector2 coords, AnimationEnum textures) {
        super();
        setX(coords.x);
        setY(coords.y);

        animationManager = new AnimationManager(AtlasEnum.COMPONENTS,textures);
    }
    public Component(float x, float y, TextureEnum textures){
        this(new Vector2(x, y), textures);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.draw(animationManager.getCurrentFrame(), getX(), getY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        animationManager.updateAnimation(delta);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
    }
}
