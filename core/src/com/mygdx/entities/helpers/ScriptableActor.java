package com.mygdx.entities.helpers;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.GCStage;
import com.mygdx.dialogues.Dialogue;
import com.mygdx.hud.Hud;
import com.mygdx.messages.MSG;
import com.mygdx.resources.RM;
import com.mygdx.resources.enums.DialogueEnum;
import com.mygdx.resources.enums.ScriptEnum;
import com.mygdx.resources.enums.TextureEnum;
import com.mygdx.scripts.Script;

public class ScriptableActor extends GameActor {
    protected Script script;
    private Table dialTable;
    private Label dial = new Label("", RM.get().skin().get("small_dial", Label.LabelStyle.class));
    public MSG listeningMSG;

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

    public void move(float x, float y, boolean relative) {
        if (relative)
            movAbs(getCoords().add(new Vector2(x, y)));
        else
            movAbs(x, y);
    }

    public void proceed() {
        script.proceed(this);
    }

    public void changeAnimation(TextureEnum texture, float time) {
        animationManager.setCurrentAnimation(texture);
        script.proceed(this);
    }

    public void listen(MSG msg) {
        GCStage.get().subscribe(this, msg);
        this.listeningMSG = msg;
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

    public void resetListen() {
        GCStage.get().unSubscribe(this, listeningMSG);
        listeningMSG = null;
        proceed();
    }

    public boolean hasScript() {
        return script != null;
    }

    public void tell(DialogueEnum story) {
        Hud.stage().addActor(new Dialogue(RM.get().getStory(story), this));
    }

    public void say(String text) {
        dial.setText(text);
        GCStage.get().addActor(dialTable);
        addAction(Actions.sequence(Actions.delay(5), Actions.run(() -> dialTable.remove())));
    }
}