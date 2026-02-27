package com.mygdx.gunControls.guns;


import com.mygdx.GCStage;
import com.mygdx.camera.CameraController;
import com.mygdx.gunControls.GunController;
import com.mygdx.gunControls.projectiles.UsbladeProj;
import com.mygdx.resources.RM;
import com.mygdx.resources.enums.AtlasEnum;
import com.mygdx.resources.enums.TextureEnum;

public class Usblade extends BaseGun {
    public Usblade() {
        super(RM.get().getSpriteFromAtlas(AtlasEnum.WEAPONS, TextureEnum.USBLADE), GCStage.get().getPlayer().center, 45);
        setOffset(35, 0, 0);
    }

    public void leftTrigger() {
        super.leftTrigger();
        UsbladeProj proj = new UsbladeProj(
            GCStage.get().getPlayer().center,
            movement.position.x + movement.center.x,
            CameraController.getMouseAngle() + angleOffset,
            flipped);
        GCStage.get().addActor(proj);

        GunController.get().setCooldown(50);
    }
}
