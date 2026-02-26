package com.mygdx.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.Data;
import com.mygdx.GCStage;
import com.mygdx.camera.CameraController;
import com.mygdx.map.TileMapCollisionsManager;

/**
 * RealtimeMovement for the overworld
 */
public class PlayerMovement {

    private final Player player;
    private String lastDirection;

    public PlayerMovement() {
        this.player = GCStage.get().getPlayer();
        lastDirection = "IDLE_DOWN";
    }

    /**
     * moves the player and returns the correct direction of the body
     */
    public String move() {
        if(Data.dialogueActive) return lastDirection;
        return player.isFighting() ? moveWhileFighting() : moveWhileNotFighting();
    }

    /***
     * Called when fucking while mobbing
     */
    private String moveWhileFighting() {
        float angle = CameraController.getMouseAngle();
        String idleDir = "-";

        if (angle > 55 && angle <= 125)
            idleDir = "IDLE_UP";
        else if (angle > 125 && angle <= 235)
            idleDir = "IDLE_LEFT";
        else if (angle > 235 && angle <= 305)
            idleDir = "IDLE_DOWN";
        else if (angle > 305 || angle <= 55)
            idleDir = "IDLE_RIGHT";
        String direction = "WALK_" + idleDir.substring(5);

        Vector2 finalPosition = new Vector2(0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (direction.equals("WALK_RIGHT"))
                finalPosition.x += 40;
            finalPosition.x += 100;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (direction.equals("WALK_LEFT"))
                finalPosition.x -= 40;
            finalPosition.x -= 100;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (direction.equals("WALK_UP"))
                finalPosition.y += 40;
            finalPosition.y += 100;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (direction.equals("WALK_DOWN"))
                finalPosition.y -= 40;
            finalPosition.y -= 100;
        }
        
        if (TileMapCollisionsManager.getCurrentTileProprieties() == null)
            return "";

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
            finalPosition.scl(10);

        finalPosition.scl(Gdx.graphics.getDeltaTime());
        if (TileMapCollisionsManager.canMove(player.getX() + finalPosition.x, player.getY() + finalPosition.y)) {
            player.setX(player.getX() + finalPosition.x);
            player.setY(player.getY() + finalPosition.y);
        }

        lastDirection = direction;

        return direction;
    }

    /***
     * Called when not fucking while mobbing
     */
    private String moveWhileNotFighting() {
        String direction = "IDLE_DOWN";
        Vector2 finalPosition = new Vector2(0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            finalPosition.x += 100;
            if (!lastDirection.equals("WALK_UP") && !lastDirection.equals("WALK_DOWN")) {
                direction = "WALK_RIGHT";
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            finalPosition.x -= 100;
            if (!lastDirection.equals("WALK_UP") && !lastDirection.equals("WALK_DOWN")) {
                direction = "WALK_LEFT";
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            finalPosition.y += 100;
            if (!lastDirection.equals("WALK_RIGHT") && !lastDirection.equals("WALK_LEFT")) {
                direction = "WALK_UP";
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            finalPosition.y -= 100;
            if (!lastDirection.equals("WALK_RIGHT") && !lastDirection.equals("WALK_LEFT")) {
                direction = "WALK_DOWN";
            }
        }
        if (finalPosition.x == 0 && finalPosition.y == 0) {
            direction = "IDLE_" + lastDirection.split("_")[1];
        }
        if (TileMapCollisionsManager.getCurrentTileProprieties() == null)
            return "";

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
            finalPosition.scl(10);

        finalPosition.scl(Gdx.graphics.getDeltaTime());
        if (TileMapCollisionsManager.canMove(player.getX() + finalPosition.x, player.getY() + finalPosition.y)) {
            player.setX(player.getX() + finalPosition.x);
            player.setY(player.getY() + finalPosition.y);
        }

        lastDirection = direction;

        return direction;
    }
}
