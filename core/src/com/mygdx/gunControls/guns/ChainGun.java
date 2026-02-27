package com.mygdx.gunControls.guns;

import com.mygdx.hud.Hud;
import com.mygdx.GCStage;
import com.mygdx.camera.CameraController;
import com.mygdx.gunControls.GunController;
import com.mygdx.gunControls.projectiles.Projectile;
import com.mygdx.delay.DelayManager;
import com.mygdx.resources.RM;
import com.mygdx.resources.enums.AtlasEnum;
import com.mygdx.resources.enums.TextureEnum;

public class ChainGun extends BaseGun {
    private int bullets = 100, mag = 100, reloadAmount = 1;
    private float waitingTime = 90f, timeOn = 10f, timeOff = 3f, cooldown = 3f;
    private Object reload = new Object();

    /**
     * ChainGun class
     */
    public ChainGun() {
        super(RM.get().getSpriteFromAtlas(AtlasEnum.WEAPONS, TextureEnum.CHAINGUN), GCStage.get().getPlayer().center, 0);
        setOffset(30, 0, 0);
        flip(true, false);

        // waitingTime = time initiate reload
        DelayManager.registerObject(this, waitingTime);
    }

    @Override
    public void onCurrent() {
        super.onCurrent();

        // timeOn = time for bullet while having chainGun as current
        DelayManager.registerObject(reload, timeOn);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        DelayManager.updateDelay(this);
        DelayManager.updateDelay(reload);
        Hud.get().setDebugSting(bullets + "");

        if (!DelayManager.isDelayOver(this))
            return;

        if (!DelayManager.isDelayOver(reload))
            return;

        if (bullets < mag) {
            bullets += reloadAmount;
            DelayManager.resetDelay(reload);
        }
    }

    @Override
    public void onSwitched() {
        super.onSwitched();

        // timeOn = time for bullet while having chainGun in background
        DelayManager.registerObject(reload, timeOff);
    }

    public void leftTrigger() {
        super.leftTrigger();
        Projectile proj = new Projectile(GCStage.get().getPlayer().center, getWidth(), CameraController.getMouseAngle() + angleOffset, true);
        GCStage.get().addActor(proj);
        bullets -= 1;

        CameraController.applyShakeEffect();

        // cooldown = cooldown between projectiles
        GunController.get().setCooldown(cooldown);
        DelayManager.resetDelay(this);
    }

    public void rightTrigger() {
        super.rightTrigger();
        Projectile proj = new Projectile(GCStage.get().getPlayer().center, getWidth(), CameraController.getMouseAngle() + angleOffset, true);
        GCStage.get().addActor(proj);

        GunController.get().setCooldown(50);
    }

    public void middleTrigger() {
        super.middleTrigger();
    }

    public int getBullets() {
        return bullets;
    }

    @Override
    public void destroy() {
        DelayManager.unregisterObject(this);
    }
}
