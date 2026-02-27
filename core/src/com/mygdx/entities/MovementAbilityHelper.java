package com.mygdx.entities;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.entities.helpers.GameActor;

public class MovementAbilityHelper {
    public static void dash(GameActor actor, Vector2 angle, float scaleFactor){
        angle.scl(scaleFactor);
        actor.moveBy(angle.x, angle.y);
    }
}
