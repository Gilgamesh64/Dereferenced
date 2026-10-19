package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.Logger;
import com.mygdx.resources.RM;
import com.mygdx.resources.ResourceManager;
import com.mygdx.savings.SavingsManager;
import com.mygdx.screens.ScreenEnum;
import com.mygdx.screens.ScreenManager;

public class GarbageCollection extends Game {

    private static GarbageCollection instance;
    private ResourceManager manager;

    public GarbageCollection() {
        instance = this;
    }

    public static GarbageCollection getInstance() {
        return instance;
    }

    public ResourceManager getManager() {
        return manager;
    }

    @Override
    public void create() {
        manager = new ResourceManager();

        SavingsManager.loadDefaultIfNeeded();
        SavingsManager.load();
        Logger.init();

        setScreen(ScreenManager.getScreen(ScreenEnum.MENU_SCREEN));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        RM.get().dispose();
    }
}
