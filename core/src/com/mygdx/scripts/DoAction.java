package com.mygdx.scripts;

import com.mygdx.entities.helpers.ScriptableActor;
import com.mygdx.resources.enums.ScriptEnum;

public class DoAction implements ScriptAction {
    private final ScriptEnum script;

    public DoAction(ScriptEnum script) {
        this.script = script;
    }


    @Override
    public void perform(ScriptableActor actor) {
        actor.doScript(script);
    }
}
