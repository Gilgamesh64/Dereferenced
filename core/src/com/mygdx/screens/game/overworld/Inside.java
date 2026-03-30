package com.mygdx.screens.game.overworld;

import com.mygdx.Data;
import com.mygdx.entities.npcs.NPCBuilder;
import com.mygdx.quest.Quests;
import com.mygdx.resources.enums.AnimationEnum;
import com.mygdx.resources.enums.MapEnum;
import com.mygdx.screens.generic.PlayableScreen;

public class Inside extends PlayableScreen {

    public Inside() {
        super(MapEnum.INSIDE);

        stage.addAll(
            NPCBuilder.create(AnimationEnum.JERKINS, Data.TILE * 6, Data.TILE * 6)
                .interaction(npc -> {
                    if(Quests.BANDITS.get().equals("PROCEEDING")){
                        Quests.BANDITS.set("COMPLETED");
                    }
                    else{
                        Quests.BANDITS.set("STARTED");
                    }
                    
                    System.out.println(Quests.BANDITS.get());
                })
                .update(npc -> {
                })
                .build()
        );

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