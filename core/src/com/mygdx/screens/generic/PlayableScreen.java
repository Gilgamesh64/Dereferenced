package com.mygdx.screens.generic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.Data;
import com.mygdx.GCStage;
import com.mygdx.Money;
import com.mygdx.camera.CameraController;
import com.mygdx.effects.Effect;
import com.mygdx.entities.map.doors.Door;
import com.mygdx.entities.player.Player;
import com.mygdx.hitboxes.HitboxHandler;
import com.mygdx.hud.Hud;
import com.mygdx.map.MapManager;
import com.mygdx.messages.MSG;
import com.mygdx.resources.enums.MapEnum;
import com.mygdx.resources.enums.TextureEnum;
import com.mygdx.savings.SavingsManager;
import com.mygdx.screens.ScreenEnum;
import com.mygdx.screens.ScreenManager;

/**
 * generic abstract class for every playable screen
 */
public abstract class PlayableScreen extends GenericScreen {

    public MapManager mapManager;
    protected boolean stopGame = false;
    protected Hud hud;
    protected HitboxHandler hitboxHandler;

    protected Player player;

    private final MapEnum name;

    protected PlayableScreen(MapEnum map) {
        super();

        camera.zoom = 0.7f;

        hitboxHandler = new HitboxHandler();
        HitboxHandler.set(hitboxHandler);

        mapManager = new MapManager(map);

        player = new Player();

        stage.addActor(player);
        stage.setKeyboardFocus(player);

        hud = new Hud(mux);
        this.name = map;

        setGlobs();
    }

    @Override
    public void show() {
        super.show();
        setGlobs();

        GCStage.get().subscribe(mapManager, MSG.BLOCK_WALLS, MSG.SWAP_FIGHT_STATE);
        GCStage.get().subscribe(player, MSG.SWAP_FIGHT_STATE);
        if (SavingsManager.getSavings().isFightging())
            GCStage.get().send(MSG.SWAP_FIGHT_STATE, MSG.BLOCK_WALLS); //turns on combat mode
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            ScreenManager.setScreen(ScreenEnum.PAUSE_SCREEN);
            return;
        }
        if (Gdx.input.isKeyJustPressed(Keys.U)) {
            GCStage.get().addActor(new Effect(TextureEnum.EXPLOSION, player.getX() + 32, player.getY()));
        }
        if (Gdx.input.isKeyJustPressed(Keys.M)) {
            SavingsManager.save();
        }
        if (Gdx.input.isKeyJustPressed(Keys.L)) {
            Money.gain(50);
        }
        if (Gdx.input.isKeyJustPressed(Keys.H)) {
            GCStage.get().send(MSG.BLOCK_WALLS);
        }
        if (Gdx.input.isKeyJustPressed(Keys.J)) {
            GCStage.get().send(MSG.SWAP_FIGHT_STATE);
        }

        stage.getActors().sort((a, b) -> Float.compare(b.getY(), a.getY())); //solves z index problem

        stage.act(Gdx.graphics.getDeltaTime());

        CameraController.updateCamera();
        hitboxHandler.checkRegistered();

        mapManager.render(camera);

        stage.draw();

        hud.update();
        hud.draw();
    }

    private void setGlobs(){
        HitboxHandler.set(hitboxHandler);
        GCStage.get().setPlayer(player);
        Hud.set(hud);
        MapManager.set(mapManager);
        CameraController.initCamera();
        stage.getCamera().position.set(getPlayerCoordinates(), 0);
    }

    protected void stopGame() {
        stopGame = true;
    }

    protected void resumeGame() {
        stopGame = false;
    }

    public Vector2 getPlayerCoordinates() {
        return new Vector2(player.getX(), player.getY());
    }

    public String getName() {
        return this.name.name();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Hud.get().resize(width, height);
    }

}
