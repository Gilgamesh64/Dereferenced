package com.mygdx.screens.game.overworld;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.mygdx.Data;
import com.mygdx.entities.npcs.NPCBuilder;
import com.mygdx.entities.npcs.NPC;
import com.mygdx.map.MapManager;
import com.mygdx.path.Path;
import com.mygdx.path.PathFinder;
import com.mygdx.resources.enums.AnimationEnum;
import com.mygdx.resources.enums.MapEnum;
import com.mygdx.resources.enums.ScriptEnum;
import com.mygdx.screens.generic.PlayableScreen;

public class Inside extends PlayableScreen {
    private final int spawnX = Data.TILE * 9 + 24;
    private final int spawnY = Data.TILE;

    private final Path queue;

    private final NPCBuilder poorPeopleBuilder = NPCBuilder.create(AnimationEnum.JERKINS, spawnX, spawnY);

    public Inside() {
        super(MapEnum.INSIDE);
        queue = Path.joining(
                PathFinder.find(MapManager.get().getMarker("spawn"), MapManager.get().getMarker("p1")),
                PathFinder.find(MapManager.get().getMarker("p1"), MapManager.get().getMarker("p2")),
                PathFinder.find(MapManager.get().getMarker("p2"), MapManager.get().getMarker("p3")),
                PathFinder.find(MapManager.get().getMarker("p3"), MapManager.get().getMarker("p4")),
                PathFinder.find(MapManager.get().getMarker("p4"), MapManager.get().getMarker("p5")),
                PathFinder.find(MapManager.get().getMarker("p6"), MapManager.get().getMarker("p6")),
                PathFinder.find(MapManager.get().getMarker("p6"), MapManager.get().getMarker("spawn"))
            );

        stage.addAction(Actions.forever(Actions.sequence(
            Actions.run(this::getSpawnRate),
            Actions.delay(getSpawnRate()),
            Actions.run(() -> {
                NPC dude = poorPeopleBuilder.build();
                dude.runPath(Path.joining(queue));
                stage.addActor(dude);
            }))));
        
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