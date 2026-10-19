package com.mygdx.scripts;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.entities.helpers.ScriptableActor;

public class WaitAction implements ScriptAction {

    private final float time;

    public WaitAction(Float time) {
        this.time = time;
    }

    @Override
    public void perform(ScriptableActor actor) {
        actor.addAction(
                Actions.sequence(
                        Actions.delay(time),
                        Actions.run(actor::proceed)));

    }
}
