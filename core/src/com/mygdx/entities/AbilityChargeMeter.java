package com.mygdx.entities;

public class AbilityChargeMeter {

    private static final int MAX_CHARGES = 3;
    private static final float CHARGE_PER_USE = 1f / MAX_CHARGES;
    private static float secondsToFull = 10;

    private static float charge = 0f;
    private static float chargeRate = 1f / secondsToFull;

    public static void update(float delta) {
        charge = Math.min(1f, charge + chargeRate * delta);
    }

    public static int getAvailableCharges() {
        return (int)(charge / CHARGE_PER_USE);
    }

    public static boolean canUseAbility() {
        return getAvailableCharges() > 0;
    }

    public static void useAbility() {
        if (!canUseAbility()) return;

        charge -= CHARGE_PER_USE;
        charge = Math.max(0f, charge);
    }

    public static float getChargePercent() {
        return charge;
    }

    public static void setSecondsToFull(float secondsToFull) {
        AbilityChargeMeter.secondsToFull = secondsToFull;
    }

    public static float getTimeUntilNextCharge() {
        float remainder = charge % CHARGE_PER_USE;
        if (remainder == 0f && canUseAbility()) return 0f;

        float needed = CHARGE_PER_USE - remainder;
        return needed / chargeRate;
    }
}