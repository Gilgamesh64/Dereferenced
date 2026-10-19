package com.mygdx.entities.helpers;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.GCStage;
import com.mygdx.dialogues.Dialogue;
import com.mygdx.hud.Hud;
import com.mygdx.messages.MSG;
import com.mygdx.path.Path;
import com.mygdx.resources.RM;
import com.mygdx.resources.enums.DialogueEnum;
import com.mygdx.resources.enums.ScriptEnum;
import com.mygdx.resources.enums.TextureEnum;
import com.mygdx.scripts.Script;
import com.mygdx.scripts.ScriptAction;

public class ScriptableActor extends GameActor {
    public MSG listeningMSG;
    protected Script script;
    protected Path path;
    private final Table dialTable;
    private final Label dial = new Label("", RM.get().skin().get("small_dial", Label.LabelStyle.class));


    public ScriptableActor() {
        super();
        dialTable = new Table();
        dialTable.add(dial);
    }

    public void doScript(ScriptEnum s) {
        this.clearActions();
        script = new Script(s);
        script.proceed(this);
    }

    public void proceed() {
        script.proceed(this);
    }

    public boolean hasScript() {
        return script != null;
    }

    public boolean isRunningScriptAction() {
        return script.getCurrentScriptClass() == ScriptAction.class;
    }

    public void changeAnimation(TextureEnum texture) {
        animationManager.setCurrentAnimation(texture);
        script.proceed(this);
    }

    public void listen(MSG msg) {
        GCStage.get().subscribe(this, msg);
        this.listeningMSG = msg;
    }

    public void resetListen() {
        GCStage.get().unSubscribe(this, listeningMSG);
        listeningMSG = null;
        proceed();
    }

    public void tell(DialogueEnum story) {
        Hud.stage().addActor(new Dialogue(RM.get().getStory(story), this));
    }

    public void say(String text) {
        dial.setText(text);
        GCStage.get().addActor(dialTable);
        addAction(Actions.sequence(Actions.delay(5), Actions.run(() -> dialTable.remove())));
    }

    public void runPath(Path path) {
        this.path = path;
        path.advance(this);
    }

    public Path getPath() {
        return path;
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        super.handleMessage(msg);
        if (listeningMSG != null)
            if (listeningMSG.code == msg.message) {
                resetListen();
            }

        return true;
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        dialTable.setPosition(center.x, getY() + 50);
    }
}