package com.mygdx.screens.game.arenas;

import com.mygdx.Data;
import com.mygdx.GCStage;
import com.mygdx.entities.npcs.NPCBuilder;
import com.mygdx.entities.npcs.Reflection;
import com.mygdx.map.TileMapCollisionsManager;
import com.mygdx.messages.MSG;
import com.mygdx.resources.RM;
import com.mygdx.resources.enums.AnimationEnum;
import com.mygdx.resources.enums.MapEnum;
import com.mygdx.resources.enums.SoundEnum;
import com.mygdx.screens.generic.PlayableScreen;

public class ReflectionArena extends PlayableScreen {

    public ReflectionArena() {
        super(MapEnum.REFLECTION_ARENA);
        GCStage.get().addAll(new Reflection(NPCBuilder.create(AnimationEnum.BLACKMARKETEER, Data.TILE * 10, Data.TILE * 10)));
    }

    @Override
    public void show() {
        super.show();
        if (Math.random() < 0.5f)
            RM.get().playAudio(SoundEnum.REFLECTION_1);
        else
            RM.get().playAudio(SoundEnum.REFLECTION_3);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (TileMapCollisionsManager.changeFightingState()) {
            GCStage.get().send(MSG.BLOCK_WALLS);
            GCStage.get().send(MSG.SWAP_FIGHT_STATE);

        }
    }
}
