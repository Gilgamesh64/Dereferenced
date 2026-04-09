package com.mygdx.entities.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.AnimationManager;
import com.mygdx.Data;
import com.mygdx.GCStage;
import com.mygdx.entities.map.doors.Door;
import com.mygdx.hitboxes.Hitbox;
import com.mygdx.hitboxes.Tags;
import com.mygdx.resources.RM;
import com.mygdx.resources.enums.AtlasEnum;
import com.mygdx.resources.enums.TextureEnum;
import com.mygdx.screens.Screens;
import com.mygdx.screens.ScreensManager;

public class TextureDoor extends Door {

    public TextureDoor(String name, String dst, String dir, float x, float y, int size, String textureBaseName) {
        super(name, dst, dir, x, y);
        String opening = textureBaseName + "_OPENING";
        String closing = textureBaseName + "_CLOSING";
        TextureEnum openingEnum = TextureEnum.valueOf(opening);
        TextureEnum closingEnum = TextureEnum.valueOf(closing);

        var texture = RM.get().getAtlas(AtlasEnum.COMPONENTS).findRegion(opening.toLowerCase());
        float width = texture.getRegionWidth() / openingEnum.frameCount * size;
        float height = texture.getRegionHeight();
        animationManager = new AnimationManager(AtlasEnum.COMPONENTS, 0.1f, size, 1,  openingEnum, closingEnum).playOnce(true);
        animationManager.shouldNotDoFirstPlay();

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
            if (Data.exiting){
                animationManager.setCurrentAnimation(closingEnum);
                return;
            }
            animationManager.setCurrentAnimation(openingEnum);
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
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(animationManager.getCurrentFrame(), getX(), getY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        animationManager.updateAnimation(delta);
    }

    @Override
    public String toString() {
        return "TexureDoor{" + '\n' +
                "\tname=" + name + '\n' +
                "\tdst=" + dst + '\n' +
                "\tdir=" + dir + '\n' +
                "\tinsideCoords=" + insideCoords + '\n' +
                "\toutsideCoords=" + outsideCoords + '\n' +
                "}";

    }
}
