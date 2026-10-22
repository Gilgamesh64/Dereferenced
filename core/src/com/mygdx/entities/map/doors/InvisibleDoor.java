package com.mygdx.entities.map;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.Data;
import com.mygdx.GCStage;
import com.mygdx.entities.map.doors.Door;
import com.mygdx.hitboxes.Hitbox;
import com.mygdx.hitboxes.Tags;
import com.mygdx.screens.ScreenEnum;
import com.mygdx.screens.ScreenManager;

public class InvisibleDoor extends Door {

    public InvisibleDoor(String name, String dst, String dir, float x, float y, float width, float height) {
        super(name, dst, dir, x, y);

        setSize(width, height);

        insideCoords = center.cpy();
        outsideCoords = switch (dir) {
            case "u" -> center.cpy().add(0, -36);
            case "d" -> center.cpy().add(0, 48);
            case "l" -> center.cpy().add(34, 0);
            case "r" -> center.cpy().add(-34, 0);
            default -> center.cpy();
        };

        hitbox = new Hitbox(new Vector2(x + width / 2, y + height / 2), width, height, true);
        hitbox.setTags(Tags.DOOR);
        hitbox.setOnHit((collider) -> {
            if (ScreenManager.isPlayerExitingDoor)
                return;

            ScreenManager.isPlayerExitingDoor = true;
            ScreenManager.doorName = dst;
            GCStage.get().getPlayer().movAbs(insideCoords, () -> {
                ScreenManager.setScreen(ScreenEnum.valueOf(dst.split("-")[0]));
                GCStage.get().getPlayer().exitFromDoor();
            });
        });
        hitbox.register();
        setPosition(x, y);

        if (Data.HITBOX_DEBUG)
            debug();
    }

    @Override
    public String toString() {
        return "InvisibleDoor{" + '\n' +
                "\tname=" + name + '\n' +
                "\tdst=" + dst + '\n' +
                "\tdir=" + dir + '\n' +
                "\tinsideCoords=" + insideCoords + '\n' +
                "\toutsideCoords=" + outsideCoords + '\n' +
                "}";

    }
}
