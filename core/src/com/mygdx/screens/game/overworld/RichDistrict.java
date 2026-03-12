package com.mygdx.screens.game.overworld;

import com.mygdx.Data;
import com.mygdx.GCStage;
import com.mygdx.entities.npcs.NPC;
import com.mygdx.entities.npcs.NPCBuilder;
import com.mygdx.resources.enums.AnimationEnum;
import com.mygdx.resources.enums.DialogueEnum;
import com.mygdx.resources.enums.MapEnum;
import com.mygdx.resources.enums.ScriptEnum;
import com.mygdx.screens.generic.PlayableScreen;

public class RichDistrict extends PlayableScreen {

        private NPC particularNPC2 = NPCBuilder.create(AnimationEnum.BLACKMARKETEER, Data.TILE * 55, Data.TILE * 25)
                        .story(DialogueEnum.ADEPTUS_1)
                        .autoStartedScript(ScriptEnum.BLACKMARKETEER_1)
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