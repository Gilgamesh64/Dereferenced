package com.mygdx.gunControls;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.gunControls.guns.BaseGun;
import com.mygdx.gunControls.guns.DummyGun;
import com.mygdx.delay.DelayManager;

public class GunController extends Actor {
    private static GunController instance;
    private final ArrayList<BaseGun> gunlist;
    private int gunIndex;
    private BaseGun currentGun, dummyGun;

    public static GunController get() {
        if (instance == null)
            instance = new GunController();
        return instance;
    }

    private GunController() {
        gunlist = new ArrayList<>();
        dummyGun = new DummyGun();
        DelayManager.registerObject(this, 10f);
        init();
    }

    private void init() {
        if (gunlist.isEmpty())
            gunlist.add(dummyGun);
        gunIndex = 0;
        currentGun = gunlist.get(0);
        currentGun.onCurrent();
    }

    /**
     * Gets the guns from the GunsEnum to add them to the Gunlist.
     */
    public void loadGuns() {
        gunlist.clear();
        loadGuns(GunsEnum.values());
    }

    /**
     * Adds a chain of guns to the Gunlist.
     * 
     * @param gunEnums The GunsEnum instances to add.
     */
    public void loadGuns(GunsEnum... gunEnums) {
        gunlist.remove(dummyGun);
        for (GunsEnum gunEnum : gunEnums) {
            if (!gunEnum.isActive())
                continue;
            gunlist.add(gunEnum.gun());
        }

        init();
    }

    public void removeGun(BaseGun gun) {
        gunlist.remove(gun);

        init();
    }

    public void setCooldown(float cooldown) {
        DelayManager.registerObject(this, cooldown);
        resetCooldown();
    }

    public void resetCooldown() {
        DelayManager.resetDelay(this);
    }

    public boolean isCurrent(BaseGun gun) {
        return gun.equals(currentGun);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        currentGun.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        DelayManager.updateDelay(this);

        BaseGun nextGun = gunlist.get(gunIndex);
        if (!isCurrent(nextGun)) {
            currentGun.onSwitched();
            currentGun = nextGun;
            currentGun.onCurrent();
            setCooldown(50);
        }
        updateGuns(delta);

        if (gunlist.isEmpty() || !DelayManager.isDelayOver(this))
            return;

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            currentGun.leftTrigger();
        } else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            currentGun.rightTrigger();
        }
        if(Gdx.input.isKeyJustPressed(Keys.E)){
            advance(1);
        }
    }

    public void advance(int amount){
        gunIndex = (gunIndex + amount) % gunlist.size();
    }

    public void updateGuns(float delta) {
        for (BaseGun gun : gunlist) {
            gun.act(delta);
        }
    }
}