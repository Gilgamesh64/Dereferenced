package com.mygdx.screens;

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

    private static final HashMap<ScreenEnum, GenericScreen> map = new HashMap<>();
    private static ScreenEnum lastPlayableActiveScreenEnum;

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
            lastPlayableActiveScreenEnum = name;
        return map.get(name);
    }

    public static PlayableScreen getPlayableScreen(ScreenEnum screenName) {
        return (PlayableScreen) getScreen(screenName);
    }

    public static boolean isNull(ScreenEnum screenName) {
        return map.get(screenName) == null;
    }

    public static PlayableScreen getCurrentPlayableScreen() {
        return getPlayableScreen(getLastPlayableActiveScreenEnum());
    }

    public static ScreenEnum getLastPlayableActiveScreenEnum() {
        return lastPlayableActiveScreenEnum;
    }

    public static void setScreen(ScreenEnum s) {
        RM.get().stopAllAudio();
        GarbageCollection.getInstance().setScreen(ScreenManager.getScreen(s));
    }

    public static void setScreen(ScreenEnum s, String doorName) {
        RM.get().stopAllAudio();
        GarbageCollection.getInstance().setScreen(ScreenManager.getScreen(s));
        if (GarbageCollection.getInstance().getScreen() instanceof PlayableScreen ps) {
            ps.exitFrom(doorName);
        }
    }
}
