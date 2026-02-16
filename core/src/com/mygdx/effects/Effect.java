package com.mygdx.effects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.AnimationManager;
import com.mygdx.entities.helpers.GameActor;
import com.mygdx.resources.enums.AtlasEnum;
import com.mygdx.resources.enums.TextureEnum;

public class Effect extends GameActor {

    private boolean shouldDoOnce;

    /**
     * creates an effect that lasts for a certain time
     * @param texture
     * @param x
     * @param y
     * @param duration
     */
    public Effect(TextureEnum texture, float x, float y, float duration){
        setCoords(x, y);
        float animationRate = texture.animationRate != -1 ? texture.animationRate : 0.2f;
        float delay = texture.delay != -1 ? texture.delay : 0f;
        animationManager = new AnimationManager(AtlasEnum.EFFECTS, animationRate, delay, true,1, texture);

        this.addAction(Actions.sequence(
            Actions.delay(duration),
            Actions.run(this::remove)
        ));
    }

    /**
     * creates an effect that plays its animation just one time
     * @param texture
     * @param x
     * @param y
     */
    public Effect(TextureEnum texture, float x, float y){
        setCoords(x, y);
        float animationRate = texture.animationRate != -1 ? texture.animationRate : 0.2f;
        float delay = texture.delay != -1 ? texture.delay : 0f;
        animationManager = new AnimationManager(AtlasEnum.EFFECTS, animationRate, delay, true,1, texture);

        shouldDoOnce = true;
    }
    

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(animationManager.getCurrentFrame(), getX(), getY());

        if(shouldDoOnce && animationManager.isFinishedOnce()) remove();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        animationManager.updateAnimation(delta);
    }
}
