package com.mygdx.screens.menus;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.resources.RM;
import com.mygdx.savings.SavingsManager;
import com.mygdx.screens.ScreenEnum;
import com.mygdx.screens.ScreenManager;
import com.mygdx.screens.generic.GuiScreen;

public class PauseScreen extends GuiScreen {

    public PauseScreen() {
        super();

        var bg = new Image(RM.get().skin(), "background_2");
        stage.getActors().insert(0, bg);

        var pauseLabel = new Label("PAUSED", RM.get().skin());
        table.add(pauseLabel).top().row();

        var playButton = new ImageButton(RM.get().skin().getDrawable("play"));
        var settingsButton = new ImageButton(RM.get().skin().getDrawable("settings"));
        var quitButton = new ImageButton(RM.get().skin().getDrawable("quit"));

        table.add(playButton).padBottom(20);
        table.row();
        playButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.setScreen(SavingsManager.getSavings().getLastRoom());
                return true;
            }
        });

        table.add(settingsButton).padBottom(20);
        table.row();
        settingsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.setScreen(ScreenEnum.SETTINGS);
                return true;
            }
        });

        table.add(quitButton).padBottom(20);
        table.row();
        quitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.setScreen(ScreenEnum.MENU_SCREEN);
                return true;
            }
        });

    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }


}
