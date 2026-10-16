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

    private final NPC n1 = NPCBuilder.create(AnimationEnum.BLACKMARKETEER, Data.TILE * 55, Data.TILE * 25)
            .build();
    private final NPC n2 = NPCBuilder.create(AnimationEnum.JERKINS, Data.TILE * 63, Data.TILE * 31)
            .build();
    private final NPC n3 = NPCBuilder.create(AnimationEnum.JERKINS, Data.TILE * 51, Data.TILE * 51)
            .build();
    private final NPC n4 = NPCBuilder.create(AnimationEnum.BLACKMARKETEER, Data.TILE * 55, Data.TILE * 25)
            .build();
    private final NPC n5 = NPCBuilder.create(AnimationEnum.JERKINS, Data.TILE * 63, Data.TILE * 31)
            .build();
    private final NPC n6 = NPCBuilder.create(AnimationEnum.JERKINS, Data.TILE * 51, Data.TILE * 51)
            .build();
    private final NPC n7 = NPCBuilder.create(AnimationEnum.BLACKMARKETEER, Data.TILE * 55, Data.TILE * 25)
            .build();
    private final NPC n8 = NPCBuilder.create(AnimationEnum.JERKINS, Data.TILE * 63, Data.TILE * 31)
            .build();
    private final NPC n9 = NPCBuilder.create(AnimationEnum.JERKINS, Data.TILE * 51, Data.TILE * 51)
            .build();

    public RichDistrict() {
        super(MapEnum.RICH_DISTRICT);
        GCStage.get().addAll(n1, n2, n3, n4, n5, n6, n7, n8, n9);
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