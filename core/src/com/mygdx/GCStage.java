package com.mygdx;

import java.util.stream.Stream;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.entities.map.doors.Door;
import com.mygdx.entities.player.Player;
import com.mygdx.messages.MSG;

public class GCStage extends Stage {
    private MessageDispatcher stageMsg;

    private Player player;

    private static GCStage instance;

    public static GCStage get() {
        return instance;
    }

    public static void set(GCStage stage) {
        instance = stage;
    }

    public GCStage(Viewport v) {
        super(v);
        stageMsg = new MessageDispatcher();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stageMsg.update();
    }

    public void subscribe(Telegraph a, MSG... msgs) {
        for (MSG msg : msgs) {
            stageMsg.addListener(a, msg.code);
        }
    }

    public void unSubscribe(Telegraph a, MSG... msgs) {
        for (MSG msg : msgs) {
            stageMsg.removeListener(a, msg.code);
        }
    }

    public void send(MSG... msgs) {
        for (MSG msg : msgs) {
            stageMsg.dispatchMessage(msg.code);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void addAll(Actor... actors) {
        for (Actor actor : actors) {
            addActor(actor);
        }
    }

    public Door getDoor(String doorName) {
        return Stream.of(getActors().items)
                .filter(Door.class::isInstance)
                .map(Door.class::cast)
                .filter(d -> d.getName().equals(doorName))
                .findAny().get();
    }
}