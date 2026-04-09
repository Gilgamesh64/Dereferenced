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
    private int spawnX = Data.TILE * 9 + 24, spawnY = Data.TILE;

    private NPCBuilder poorPeopleBuilder = NPCBuilder.create(AnimationEnum.JERKINS, spawnX, spawnY)
                .autoStartedScript(ScriptEnum.CANTEEN);

    private int getSpawnRate(){
        return (int)(Math.random() * 10) + 5;
    }

    private Action spawnNPC = Actions.forever(Actions.sequence(
        Actions.run(this::getSpawnRate),
        Actions.delay(getSpawnRate()),
        Actions.run(() -> stage.addActor(poorPeopleBuilder.build()))
    ));

    public Inside() {
        super(MapEnum.INSIDE);
        stage.addAction(spawnNPC);
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