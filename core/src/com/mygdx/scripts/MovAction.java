package com.mygdx.scripts;

import com.mygdx.entities.helpers.ScriptableActor;

public class MovAction implements ScriptAction {
    private final float x;
    private final float y;
    private final boolean relative;

    public MovAction(float x, float y, boolean relative) {
        this.x = x;
        this.y = y;
        this.relative = relative;
    }


    @Override
    public void perform(ScriptableActor actor) {
        if (relative) actor.movRel(x, y);
        else actor.movAbs(x, y);
    }
}
