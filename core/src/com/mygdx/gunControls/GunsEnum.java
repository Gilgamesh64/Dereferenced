package com.mygdx.gunControls;

import com.mygdx.gunControls.guns.BaseGun;
import com.mygdx.gunControls.guns.ChainGun;
import com.mygdx.gunControls.guns.Usblade;
import com.mygdx.savings.SavingsManager;

public enum GunsEnum {
    CHAINGUN(new ChainGun(), SavingsManager.getSavings().getSelectedGun(0)),
    USBLADE(new Usblade(), SavingsManager.getSavings().getSelectedGun(1));

    private final BaseGun gun;
    private final boolean active;

    GunsEnum(BaseGun gun, boolean active) {
        this.gun = gun;
        this.active = active;
    }

    public BaseGun gun() {
        return gun;
    }

    public boolean isActive() {
        return active;
    }
}