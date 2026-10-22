package com.mygdx.screens;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.GCStage;
import com.mygdx.entities.map.doors.Door;
import com.mygdx.game.GarbageCollection;
import com.mygdx.resources.RM;
import com.mygdx.screens.game.arenas.ReflectionArena;
import com.mygdx.screens.game.overworld.Inside;
import com.mygdx.screens.game.overworld.Park;
import com.mygdx.screens.game.overworld.RichDistrict;
import com.mygdx.screens.game.overworld.Slums;
import com.mygdx.screens.generic.GenericScreen;
import com.mygdx.screens.generic.PlayableScreen;
import com.mygdx.screens.menus.MenuScreen;
import com.mygdx.screens.menus.PauseScreen;
import com.mygdx.screens.menus.SettingsScreen;

import java.util.HashMap;

public class ScreenManager {
    public static boolean isPlayerExitingDoor = false;
    public static String doorName = "";
    private static final HashMap<ScreenEnum, GenericScreen> map = new HashMap<>();
    private static ScreenEnum lastPlayable;

    public static GenericScreen getScreen(ScreenEnum name) {
        if (map.get(name) == null) {
            switch (name) {
                case MENU_SCREEN -> {
                    map.put(ScreenEnum.MENU_SCREEN, new MenuScreen());
                }
                case PAUSE_SCREEN -> {
                    map.put(ScreenEnum.PAUSE_SCREEN, new PauseScreen());
                }
                case SETTINGS -> {
                    map.put(ScreenEnum.SETTINGS, new SettingsScreen());
                }
                case REFLECTION_ARENA -> {
                    map.put(ScreenEnum.REFLECTION_ARENA, new ReflectionArena());
                }
                case SLUMS -> {
                    map.put(ScreenEnum.SLUMS, new Slums());
                }
                case PARK -> {
                    map.put(ScreenEnum.PARK, new Park());
                }
                case RICH_DISTRICT -> {
                    map.put(ScreenEnum.RICH_DISTRICT, new RichDistrict());
                }
                case INSIDE -> {
                    map.put(ScreenEnum.INSIDE, new Inside());
                }
            }
        }
        if (map.get(name) instanceof PlayableScreen)
            lastPlayable = name;
        return map.get(name);
    }

    public static PlayableScreen getPlayable(ScreenEnum screenName) {
        return (PlayableScreen) getScreen(screenName);
    }

    public static boolean isNull(ScreenEnum screenName) {
        return map.get(screenName) == null;
    }

    public static PlayableScreen getCurrentPlayable() {
        return getPlayable(getLastPlayable());
    }

    public static ScreenEnum getLastPlayable() {
        return lastPlayable;
    }

    public static void setScreen(ScreenEnum s) {
        RM.get().stopAllAudio();
        GarbageCollection.getInstance().setScreen(ScreenManager.getScreen(s));
    }
}
