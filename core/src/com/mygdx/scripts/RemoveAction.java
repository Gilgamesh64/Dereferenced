package com.mygdx.scripts;

import com.mygdx.entities.helpers.ScriptableActor;

public class RemoveAction implements ScriptAction{

    @Override
    public void perform(ScriptableActor actor) {
        actor.remove();
    }
}
