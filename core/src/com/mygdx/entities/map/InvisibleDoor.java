package com.mygdx.entities.map;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.Data;
import com.mygdx.GCStage;
import com.mygdx.entities.map.doors.Door;
import com.mygdx.hitboxes.Hitbox;
import com.mygdx.hitboxes.Tags;
import com.mygdx.screens.Screens;
import com.mygdx.screens.ScreensManager;

public class InvisibleDoor extends Door {

    public InvisibleDoor(String name, String dst, String dir, float x, float y, float width,float height) {
        super(name, dst, dir, x, y);

        setSize(width, height);
        setOrigin(getWidth() / 2, getHeight() / 2);
        center.x = getX() + getOriginX();
        center.y = getY() + getOriginY();

        insideCoords = center.cpy().sub(16, 8);
        outsideCoords = switch (dir) {
            case "u" -> center.cpy().add(0, -48);
            case "d" -> center.cpy().add(0, 32);
            case "l" -> center.cpy().add(34, 0);
            case "r" -> center.cpy().add(-34, 0);
            default -> center.cpy();
        };
        outsideCoords.sub(16, 8);
        
        hitbox = new Hitbox(new Vector2(x + width / 2, y + height / 2), width, height, true);
        hitbox.setTags(Tags.DOOR);
        hitbox.setOnHit((collider) -> {
            if (Data.exiting)
                return;

            GCStage.get().getPlayer().movAbs(insideCoords, () -> {
                Data.exiting = true;
                ScreensManager.setScreen(Screens.valueOf(dst.split("-")[0]), dst);
            });
        });
        hitbox.register();
        setPosition(x, y);

        if (Data.debug)
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
