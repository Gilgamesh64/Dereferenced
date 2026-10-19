package com.mygdx.screens.game.overworld;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.Data;
import com.mygdx.entities.npcs.NPCBuilder;
import com.mygdx.resources.enums.AnimationEnum;
import com.mygdx.resources.enums.MapEnum;
import com.mygdx.resources.enums.ScriptEnum;
import com.mygdx.screens.generic.PlayableScreen;

public class Inside extends PlayableScreen {
    private final int spawnX = Data.TILE * 9 + 24;
    private final int spawnY = Data.TILE;

    private final NPCBuilder poorPeopleBuilder = NPCBuilder.create(AnimationEnum.JERKINS, spawnX, spawnY)
            .autoStartedScript(ScriptEnum.CANTEEN);
    private final Action spawnNPC = Actions.forever(Actions.sequence(
            Actions.run(this::getSpawnRate),
            Actions.delay(getSpawnRate()),
            Actions.run(() -> stage.addActor(poorPeopleBuilder.build()))
    ));

    public Inside() {
        super(MapEnum.INSIDE);
        stage.addAction(spawnNPC);
    }

    private int getSpawnRate() {
        return (int) (Math.random() * 10) + 5;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}