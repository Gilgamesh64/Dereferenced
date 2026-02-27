package com.mygdx.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.mygdx.gunControls.GunController;
import com.mygdx.hitboxes.Collider;
import com.mygdx.hitboxes.Hitbox;
import com.mygdx.hitboxes.Tags;
import com.mygdx.messages.MSG;
import com.mygdx.resources.enums.AnimationEnum;
import com.mygdx.resources.enums.AtlasEnum;
import com.mygdx.resources.enums.TextureEnum;
import com.mygdx.AnimationManager;
import com.mygdx.Data;
import com.mygdx.GCStage;
import com.mygdx.camera.CameraController;
import com.mygdx.entities.AbilityChargeMeter;
import com.mygdx.entities.MovementAbilityHelper;
import com.mygdx.entities.helpers.ScriptableActor;

public class Player extends ScriptableActor {

    private final PlayerMovement movement;
    private final AnimationManager animationManager;
    private Collider collider = new Collider();
    private Hitbox hitbox = new Hitbox();
    private boolean fighting;

    public Player(Vector2 coordinates) {
        GCStage.get().setPlayer(this);
        setTouchable(Touchable.enabled);

        setSize(30, 32);
        setOrigin(getWidth() / 2, getHeight() / 2);

        collider = new Collider(center, getWidth(), getHeight());
        collider.setTags(Tags.PLAYER);
        collider.setSearchTags(Tags.NPC, Tags.BUILDING, Tags.PROJECTILE, Tags.SCOPE, Tags.DOOR);
        collider.register();

        hitbox = new Hitbox(center, getWidth(), getHeight(), 0, true);
        hitbox.setTags(Tags.PLAYER);
        hitbox.setOnHit((collider) -> {
            System.out.println("Colpito!");
        });
        hitbox.register();

        setPosition(coordinates.x, coordinates.y);

        GunController.get().loadGuns();
        CameraController.calculateMouseAngle(center);

        animationManager = new AnimationManager(AtlasEnum.PLAYER, AnimationEnum.PLAYER);

        if (Data.debug)
            debug();

        movement = new PlayerMovement();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(animationManager.getCurrentFrame(), getX(), getY());
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.polygon(collider.getTransformedVertices());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        CameraController.calculateMouseAngle(center);

        if (Gdx.input.isKeyJustPressed(Keys.C)) {
            if (AbilityChargeMeter.canUseAbility()) {
                AbilityChargeMeter.useAbility();
                MovementAbilityHelper.dash(this, CameraController.getMouseDirection(), 32);
            }
        }

        animationManager.setCurrentAnimation(
                autoMovementManager.update() ? TextureEnum.valueOf("PLAYER_" + autoMovementManager.getOrientation())
                        : TextureEnum.valueOf("PLAYER_" + movement.move()));

        animationManager.updateAnimation(delta);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        collider.setPosition();
        hitbox.setPosition();
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if (msg.message == MSG.SWAP_FIGHT_STATE.code)
            swapFightingState();

        return true;
    }

    public boolean isFighting() {
        return fighting;
    }

    public void swapFightingState() {
        if (fighting)
            GunController.get().remove();
        else
            GCStage.get().addActor(GunController.get());

        fighting = !fighting;
    }

    public Vector2 getCenter() {
        return center;
    }
}