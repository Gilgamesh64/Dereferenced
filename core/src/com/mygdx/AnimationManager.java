package com.mygdx;

import java.util.EnumMap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.entities.helpers.GameActor;
import com.mygdx.resources.RM;
import com.mygdx.resources.enums.AnimationEnum;
import com.mygdx.resources.enums.AtlasEnum;
import com.mygdx.resources.enums.TextureEnum;

public class AnimationManager {
    private EnumMap<TextureEnum, Animation<TextureRegion>> animationMap = new EnumMap<>(TextureEnum.class);

    private TextureEnum currentAnimation;
    private TextureRegion currentFrame;

    private float stateTime = 0f;

    private boolean playOnce = false;
    private boolean finishedOnce = false;

    private boolean shouldNotDoFirstPlay = false;
    private boolean updatedOnce = false;

    private boolean paused = false;
    private boolean alreadyPausedOnce = false;
    private GameActor pauser = new GameActor();

    private float defaultDelay = 0;
    private float currentDelay = 0;

    public AnimationManager(AtlasEnum atlas, AnimationEnum textures) {
        this(atlas, textures.animationRate, textures.frameList);
        delay(defaultDelay);
    }

    public AnimationManager(AtlasEnum atlas, float animationRate, TextureEnum... textures) {
        this(atlas, animationRate, 1, 1, textures);
    }

    public AnimationManager(AtlasEnum atlas, float animationRate, int sizeX, int sizeY, TextureEnum... textures) {
        for (TextureEnum texture : textures) {
            TextureAtlas.AtlasRegion region = RM.get().getAtlas(atlas).findRegion(texture.path);
            if (region == null) {
                throw new RuntimeException("Region not found: " + texture.path);
            }
            int frameCount = texture.frameCount / sizeX;

            int frameWidth = region.getRegionWidth() / frameCount;
            int frameHeight = region.getRegionHeight();

            // split horizontally into frames
            TextureRegion[] frames = new TextureRegion[frameCount];
            for (int i = 0; i < frameCount; i++) {
                frames[i] = new TextureRegion(region, i * frameWidth, 0, frameWidth, frameHeight);
            }

            Animation<TextureRegion> anim = new Animation<>(
                    texture.animationRate != -1 ? texture.animationRate : animationRate, frames);

            anim.setPlayMode(PlayMode.LOOP);
            animationMap.put(texture, anim);
        }

        currentAnimation = textures[0];
        currentFrame = animationMap.get(currentAnimation).getKeyFrame(stateTime);

        currentDelay = currentAnimation.delay != -1 ? currentAnimation.delay : 0;

        GCStage.get().addActor(pauser);
    }

    public AnimationManager delay(float defaultDelay) {
        this.defaultDelay = defaultDelay;
        return this;
    }

    public AnimationManager playOnce(boolean playOnce) {
        this.playOnce = playOnce;
        animationMap.get(currentAnimation).setPlayMode(playOnce ? PlayMode.NORMAL : PlayMode.LOOP);
        return this;
    }

    public AnimationManager shouldNotDoFirstPlay() {
        shouldNotDoFirstPlay = true;
        return this;
    }

    /** updates currentFrame state */
    public void updateAnimation(float delta) {
        pauser.act(delta);

        if (paused || finishedOnce || (shouldNotDoFirstPlay && !updatedOnce))
            return;

        stateTime += delta; // Advances the animation
        Animation<TextureRegion> ani = animationMap.get(currentAnimation);

        // Handle delayed pause at first frame
        if (currentDelay != 0 && ani.getKeyFrameIndex(stateTime) == 0 && !alreadyPausedOnce) {
            pauser.addAction(
                    Actions.sequence(
                            Actions.delay(currentDelay),
                            Actions.run(() -> {
                                paused = false;
                                stateTime = 0;
                            })));
            alreadyPausedOnce = true;
            paused = true;
        }

        // Reset flag when we move off frame 0
        if (ani.getKeyFrameIndex(stateTime) != 0) {
            alreadyPausedOnce = false;
        }

        // Stop if animation should only play once and has finished
        if (playOnce && ani.isAnimationFinished(stateTime)) {
            finishedOnce = true;
            currentFrame = ani.getKeyFrame(ani.getAnimationDuration() - 0.0001f); // freeze on last frame
            return;
        }

        currentFrame = ani.getKeyFrame(stateTime, playOnce);
    }

    public void setCurrentAnimation(TextureEnum ani) {
        currentAnimation = ani;
        currentDelay = ani.delay != -1 ? ani.delay : defaultDelay;
        pauser.clearActions();
        stateTime = 0;
        paused = false;
        alreadyPausedOnce = false;
        finishedOnce = false;
        updatedOnce = true;

        Animation<TextureRegion> animation = animationMap.get(currentAnimation);
        animation.setPlayMode(playOnce ? PlayMode.NORMAL : PlayMode.LOOP);
    }

    /**
     * @return true if the animation has completed (only relevant if playOnce =
     *         true)
     */
    public boolean isFinishedOnce() {
        return finishedOnce;
    }

    /** @return current frame in the animation */
    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public float getWidth() {
        return animationMap.get(currentAnimation).getKeyFrame(stateTime).getRegionWidth();
    }

    public float getHeight() {
        return animationMap.get(currentAnimation).getKeyFrame(stateTime).getRegionHeight();
    }
}
