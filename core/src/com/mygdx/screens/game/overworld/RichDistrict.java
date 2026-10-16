package com.mygdx.screens.game.overworld;

import com.mygdx.Data;
import com.mygdx.GCStage;
import com.mygdx.entities.npcs.NPC;
import com.mygdx.entities.npcs.NPCBuilder;
import com.mygdx.path.PathFinder;
import com.mygdx.resources.enums.AnimationEnum;
import com.mygdx.resources.enums.MapEnum;
import com.mygdx.screens.ScreensManager;
import com.mygdx.screens.generic.PlayableScreen;
import com.mygdx.path.Path;

public class RichDistrict extends PlayableScreen {

    private final NPC gigi = NPCBuilder.create(AnimationEnum.BLACKMARKETEER, Data.TILE * 55, Data.TILE * 25)
            .build();
    private final NPC tony = NPCBuilder.create(AnimationEnum.JERKINS, Data.TILE * 63, Data.TILE * 31)
            .build();

    public RichDistrict() {
        super(MapEnum.RICH_DISTRICT);
        GCStage.get().addAll(gigi, tony);
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