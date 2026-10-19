package com.mygdx.scripts;

import com.mygdx.entities.helpers.ScriptableActor;

public class SayAction implements ScriptAction {

    private final String text;

    public SayAction(String text) {
        this.text = text;
    }

    @Override
    public void perform(ScriptableActor actor) {
        actor.say(text);
        actor.proceed();
    }

}
