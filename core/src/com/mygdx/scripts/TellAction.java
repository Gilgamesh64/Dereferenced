package com.mygdx.scripts;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.mygdx.Data;
import com.mygdx.entities.helpers.ScriptableActor;
import com.mygdx.resources.enums.DialogueEnum;

public class TellAction implements ScriptAction {
    private final DialogueEnum story;

    public TellAction(DialogueEnum story) {
        this.story = story;
    }

    @Override
    public void perform(ScriptableActor actor) {
        if (!Data.dialogueActive) {
            actor.tell(story);
            actor.proceed();
            return;
        }

        actor.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                if (!Data.dialogueActive && getActor() instanceof ScriptableActor sa) {
                    sa.tell(story);
                    sa.proceed();
                    return true;
                }
                return false;
            }
        });

    }
}
