package com.mygdx.scripts;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.entities.helpers.ScriptableActor;
import com.mygdx.messages.MSG;

public class ListenAction implements ScriptAction {
    private final MSG msg;
    private int maxWaitingSeconds = 30;

    public ListenAction(MSG m) {
        msg = m;
    }

    public ListenAction(MSG m, int maxTime) {
        msg = m;
        maxWaitingSeconds = maxTime;
    }

    @Override
    public void perform(ScriptableActor actor) {
        System.out.println("Listening to:" + msg);
        actor.listen(msg);
        actor.addAction(
                Actions.sequence(
                        Actions.delay(maxWaitingSeconds),
                        Actions.run(() -> {
                            if (actor.listeningMSG == msg) actor.resetListen();
                        })
                )
        );
    }
}
