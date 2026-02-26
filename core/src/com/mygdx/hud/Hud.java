package com.mygdx.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.Data;
import com.mygdx.Money;
import com.mygdx.entities.AbilityChargeMeter;
import com.mygdx.resources.RM;

public class Hud implements Disposable {
    private static Hud instance;

    public static Hud get() {
        return instance;
    }

    public static void set(Hud h) {
        instance = h;
    }

    public static Stage stage() {
        return get().stage;
    }

    private final Stage stage;

    private Table table;
    private Label fps;
    private Label moneyPopup;
    private Label debugData;
    private Image healthBar;

    private Inventory inventory;
    private AbilityChargeWidget abilityBar;

    public Hud(InputMultiplexer mux) {
        FitViewport viewport = new FitViewport(Data.VIEWPORT_X, Data.VIEWPORT_Y, new OrthographicCamera());
        stage = new Stage(viewport);
        mux.addProcessor(0, stage);


        inventory = new Inventory(stage);

        table = new Table();
        table.setFillParent(true);
        table.defaults().expand();
        stage.addActor(table);

        fps = new Label("", RM.get().skin());
        table.add(fps).top().left();

        initMoneyLabel();
        table.add(moneyPopup).top().left();

        table.row();

        healthBar = new Image(RM.get().skin(), "health");

        table.add(healthBar).bottom().left();

        abilityBar = new AbilityChargeWidget(new AbilityChargeMeter(10));
        table.add(abilityBar).bottom().right();

        //debugData = new Label("Debug: ", RM.get().skin());
        //table.add(debugData).bottom().right();
    }

    public void draw() {
        stage.draw();
    }

    public void update() {
        stage.act(Gdx.graphics.getDeltaTime());
        inventory.update();
        
        fps.setText("Current FPS: " + Gdx.graphics.getFramesPerSecond());
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void setDebugSting(String debugSting) {
        debugData.setText(debugSting);
    }

    private void showMoneyPopup() {
        Label temp = new Label(moneyPopup.getText(), moneyPopup.getStyle());
        temp.setPosition(moneyPopup.getX(), moneyPopup.getY());
        temp.getColor().a = 0f;
        stage.addActor(temp);

        float startX = temp.getX();
        float startY = temp.getY();
        float endY = startY - 100;

        temp.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveTo(startX, endY, 0.5f, Interpolation.fade),
                        Actions.fadeIn(0.5f)),
                Actions.delay(1f),
                Actions.parallel(
                        Actions.moveTo(startX, startY, 0.5f, Interpolation.fade),
                        Actions.fadeOut(0.5f)),
                Actions.run(temp::remove)));

    }

    private void initMoneyLabel() {
        Pixmap borderPixmap = new Pixmap(3, 3, Pixmap.Format.RGBA8888);
        borderPixmap.setColor(0f, 0f, 0f, 1f);
        borderPixmap.fill();
        borderPixmap.setColor(0f, 1f, 0f, 1f);
        borderPixmap.drawRectangle(0, 0, 3, 3);
        Texture borderTexture = new Texture(borderPixmap);
        borderPixmap.dispose();

        NinePatchDrawable drawable = new NinePatchDrawable(new NinePatch(borderTexture, 1, 1, 1, 1));

        Label.LabelStyle baseStyle = RM.get().skin().get("default", Label.LabelStyle.class);
        Label.LabelStyle moneyStyle = new Label.LabelStyle(baseStyle);
        moneyStyle.fontColor = new Color(0f, 1f, 0f, 1f);
        moneyStyle.background = drawable;

        moneyPopup = new Label("Money: ", moneyStyle);
        moneyPopup.getColor().a = 0f;
        moneyPopup.setText(0);
    }

    /**
     * Show the money popup label with the given amount
     * 
     * @param money
     * @param gained
     */
    public void setMoney(int money, boolean gained) {
        moneyPopup.setText(Money.get());
        showMoneyPopup();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }
}
