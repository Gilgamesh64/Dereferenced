package com.mygdx.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.dialogues.GameStory;
import com.mygdx.resources.enums.*;
import com.ray3k.stripe.FreeTypeSkinLoader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Objects;
import java.util.stream.Stream;

public class ResourceManager {

    private final AssetManager manager;
    private final EnumMap<DialogueEnum, GameStory> dialogueMap;
    private final EnumMap<MapEnum, TiledMap> maps;

    public ResourceManager() {
        manager = new AssetManager();
        dialogueMap = new EnumMap<>(DialogueEnum.class);
        maps = new EnumMap<>(MapEnum.class);
        loadManager();
    }

    private void loadManager() {
        manager.setLoader(Skin.class, new FreeTypeSkinLoader(manager.getFileHandleResolver()));
        manager.load("assets/skin/ui_v2.json", Skin.class);
        manager.finishLoadingAsset("assets/skin/ui_v2.json");

        Stream.of(AtlasEnum.values()).forEach(atlas -> manager.load(atlas.path, TextureAtlas.class));
        Stream.of(DialogueEnum.values()).forEach(this::loadDialogue);
        Stream.of(MapEnum.values()).forEach(map -> maps.put(map, new TmxMapLoader().load(map.path)));
        Stream.of(SoundEnum.values()).forEach(music -> manager.load(music.path, Music.class));
    }

    public void update() {
        manager.update();
    }

    public void dispose() {
        manager.dispose();
    }

    public TextureAtlas getAtlas(AtlasEnum atlas) {
        manager.finishLoadingAsset(atlas.path);
        return manager.get(atlas.path);
    }

    public Skin skin() {
        return manager.get("assets/skin/ui_v2.json");
    }

    public Sprite getSpriteFromAtlas(AtlasEnum atlas, TextureEnum texture) {
        return getAtlas(atlas).createSprite(texture.path);
    }

    public TiledMap getMap(MapEnum map) {
        return maps.get(map);
    }

    public GameStory getStory(DialogueEnum dial) {
        return dialogueMap.get(dial);
    }

    public Music getAudio(SoundEnum sound) {
        manager.finishLoadingAsset(sound.path);
        return manager.get(sound.path);
    }

    public void playAudio(SoundEnum sound) {
        Music audio = getAudio(sound);
        if (!audio.isPlaying()) {
            audio.setVolume(0.05f);
            audio.play();
            audio.setLooping(true);
        }
    }

    public void stopAudio(SoundEnum sound) {
        if (getAudio(sound).isPlaying())
            getAudio(sound).stop();
    }

    public void stopAllAudio() {
        Stream.of(SoundEnum.values())
                .map(this::getAudio)
                .filter(Music::isPlaying)
                .forEach(Music::stop);
    }

    // helper for dialogue loading
    public void loadDialogue(DialogueEnum dialogue) {

        InputStream systemResourceAsStream = ClassLoader
                .getSystemResourceAsStream("dialogues/" + LangEnum.getCurrent() + "/" + dialogue.path);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(systemResourceAsStream), StandardCharsets.UTF_8))) {
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            String json = sb.toString().replace('\uFEFF', ' ');
            dialogueMap.put(dialogue, new GameStory(json));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateLang() {
        dialogueMap.clear();
        for (DialogueEnum dial : DialogueEnum.values()) {
            loadDialogue(dial);
        }
    }
}
