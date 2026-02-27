package com.mygdx.gunControls.guns;

import com.mygdx.GCStage;
import com.mygdx.gunControls.GunController;
import com.mygdx.gunControls.projectiles.Stone;
import com.mygdx.resources.RM;
import com.mygdx.resources.enums.AtlasEnum;
import com.mygdx.resources.enums.TextureEnum;

public class Slingshot extends BaseGun {

    public Slingshot() {
        super(RM.get().getSpriteFromAtlas(AtlasEnum.WEAPONS, TextureEnum.DEFAULT), GCStage.get().getPlayer().center, 0);
        flip(false, true);
    }

    public void leftTrigger() {
        GCStage.get().addActor(new Stone(GCStage.get().getPlayer().center, getWidth(), true));
        GunController.get().setCooldown(10);
        GunController.get().resetCooldown();
    }

    public void rightTrigger() {
        GCStage.get().addActor(new Stone(GCStage.get().getPlayer().center, getWidth(), true));
        GunController.get().setCooldown(20);
    }
}
