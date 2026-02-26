package com.mygdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class AbilityChargeMeter {

    private static final int MAX_CHARGES = 3;
    private static final float CHARGE_PER_USE = 1f / MAX_CHARGES;

    private float charge = 0f;
    private float chargeRate;

    public AbilityChargeMeter(float secondsToFull) {
        this.chargeRate = 1f / secondsToFull;
    }

    public void update(float delta) {
        charge = Math.min(1f, charge + chargeRate * delta);

        if(Gdx.input.isKeyJustPressed(Keys.F)){
            useAbility();
        }
    }

    public int getAvailableCharges() {
        return (int)(charge / CHARGE_PER_USE);
    }

    public boolean canUseAbility() {
        return getAvailableCharges() > 0;
    }

    public void useAbility() {
        if (!canUseAbility()) return;

        charge -= CHARGE_PER_USE;
        charge = Math.max(0f, charge);
    }

    public float getChargePercent() {
        return charge;
    }

    public float getTimeUntilNextCharge() {
        float remainder = charge % CHARGE_PER_USE;
        if (remainder == 0f && canUseAbility()) return 0f;

        float needed = CHARGE_PER_USE - remainder;
        return needed / chargeRate;
    }
}