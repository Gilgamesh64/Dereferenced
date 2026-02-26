package com.mygdx.entities.npcs;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.GCStage;
import com.mygdx.camera.CameraController;
import com.mygdx.delay.DelayManager;
import com.mygdx.entities.Scope;
import com.mygdx.entities.npcs.states.StateController;
import com.mygdx.gunControls.projectiles.Projectile;
import com.mygdx.hitboxes.Tags;
import com.mygdx.map.TileMapCollisionsManager;
import com.mygdx.messages.MSG;
import com.mygdx.resources.enums.TextureEnum;

import java.util.Random;

public class Reflection extends NPC {
    private Scope scope;

    private StateController stateController;

    public Reflection(NPCBuilder builder) {
        super(builder);
        stateController = new StateController();
        stateController.setMovementState(StateController.MovementState.FOLLOW_PLAYER);
        GCStage.get().subscribe(this, MSG.SHOT);
        scope = new Scope(getCoords());
        GCStage.get().addActor(scope);
        DelayManager.registerObject(scope, 100f);

        hitbox.setOnHit(collider -> {
            if (collider.containsTag(Tags.PROJECTILE)) {
                Integer dmg = collider.getExtraInfo().getIntegerInfo("damage");
                if (dmg != null)
                    lf -= dmg;
                else
                    System.out.println("no damage value");

                if (lf <= 0) {
                    this.remove();
                    scope.remove();
                    hitbox.unregister();
                }
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        DelayManager.updateDelay(scope);

        float angler = CameraController.getAngle(center, scope.center);
        String direction = "-";

        if (angler > 55 && angler <= 125)
            direction = "WALK_UP";
        else if (angler > 125 && angler <= 235)
            direction = "WALK_LEFT";
        else if (angler > 235 && angler <= 305)
            direction = "WALK_DOWN";
        else if (angler > 305 || angler <= 55)
            direction = "WALK_RIGHT";
        animationManager.setCurrentAnimation(TextureEnum.valueOf(name + "_" + direction));
        animationManager.updateAnimation(delta);

        Vector2 playerPos = GCStage.get().getPlayer().getCoords();

        if (!GCStage.get().getPlayer().isFighting()) {
            getActions().clear();
            return;
        }

        switch (stateController.getMovState()) {
            case STILL -> {
                getActions().clear();
            }
            case FOLLOW_PLAYER -> {
                if (TileMapCollisionsManager.canMove(playerPos.x, playerPos.y)) {
                    Action movement = Actions.moveTo(playerPos.x, playerPos.y, 5);
                    addAction(movement);
                }
                if (GCStage.get().getPlayer().getCoords().dst(getCoords()) < 100) {
                    stateController.setMovementState(StateController.MovementState.FLEE);
                    getActions().clear();
                }
            }
            case CIRLE_AROUND -> {
                if (TileMapCollisionsManager.canMove(playerPos.x, playerPos.y)) {

                }
                System.out.println("CIRCLING");
            }
            case FLEE -> {
                CameraController.calculateThowardsPos(playerPos, getCoords());
                float angle = CameraController.getXAngle();
                float rand = new Random().nextFloat() % 0.8f;
                Vector2 mov = new Vector2(1, rand).setAngleDeg(angle).scl(2);

                if (TileMapCollisionsManager.canMove(getX() + mov.x, getY() + mov.y)) {
                    setX(getX() + mov.x);
                    setY(getY() + mov.y);
                }

                if (GCStage.get().getPlayer().getCoords().dst(getCoords()) > 500) {
                    stateController.setMovementState(StateController.MovementState.FOLLOW_PLAYER);
                    getActions().clear();
                }
            }
            case WANDER -> {
                System.out.println("WANDERING");
            }
            default -> {
                System.out.println("NOT IN A STATE");
            }
        }
        if (scope.hitplayer && DelayManager.isDelayOver(scope)) {
            System.out.println("SHOOTING");
            getStage().addActor(
                    new Projectile(center, 0, CameraController.getAngle(getCoords(), scope.getCoords()), false));
            DelayManager.resetDelay(scope);
        }
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if (msg.message == MSG.SHOT.code) {
            System.out.println("HEY");
        }
        return true;
    }
}
