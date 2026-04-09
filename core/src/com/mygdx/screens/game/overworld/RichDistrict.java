package com.mygdx.screens.game.overworld;

import com.mygdx.Data;
import com.mygdx.GCStage;
import com.mygdx.entities.npcs.NPC;
import com.mygdx.entities.npcs.NPCBuilder;
import com.mygdx.quest.Quests;
import com.mygdx.resources.enums.AnimationEnum;
import com.mygdx.resources.enums.DialogueEnum;
import com.mygdx.resources.enums.MapEnum;
import com.mygdx.screens.generic.PlayableScreen;

public class RichDistrict extends PlayableScreen {

        private NPC particularNPC2 = NPCBuilder.create(AnimationEnum.BLACKMARKETEER, Data.TILE * 55, Data.TILE * 25)
                        .onInteraction(npc -> {
                                if(Quests.BANDITS.get().equals("STARTED")){
                                        Quests.BANDITS.set("PROCEEDING");
                                        System.out.println(Quests.BANDITS.get());
                                        npc.tell(DialogueEnum.ADEPTUS_1);
                                }
                        })
                        .build();

        public RichDistrict() {
                super(MapEnum.RICH_DISTRICT);
                GCStage.get().addAll(particularNPC2);
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