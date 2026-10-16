package com.mygdx.path;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.GCStage;
import com.mygdx.entities.helpers.GameActor;
import com.mygdx.entities.helpers.ScriptableActor;
import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Path {
    private final List<GridPoint2> path;
    private final Group debugGroup = new Group();

    private Path(List<GridPoint2> path) {
        this.path = path;
        GCStage.get().addActor(debugGroup);
    }

    public static Path of(List<GridPoint2> path) {
        return new Path(path);
    }

    public static Path of(GridPoint2... path) {
        return new Path(new ArrayList<>(Arrays.asList(path)));
    }

    public void advance(ScriptableActor entity) {
        if (path.isEmpty()) {
            debugGroup.clear();
            debugGroup.remove();
            return;
        }
        GridPoint2 curr = path.remove(0);
        entity.movAbs(curr.x * 32, curr.y * 32);

        if (path.isEmpty()) {
            debugGroup.clear();
            debugGroup.remove();
        }
    }

    public GridPoint2 peek() {
        return path.get(0);
    }

    public boolean running() {
        return !path.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (GridPoint2 curr : path) {
            sb.append(curr + "\n");
        }
        return sb.toString();
    }

    public void debugPath() {
        Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        pixmap.setColor(MathUtils.random(), MathUtils.random(), MathUtils.random(), 200);
        pixmap.fill();

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        for (GridPoint2 curr : path) {
            Image square = new Image(texture);
            square.setPosition(curr.x * 32, curr.y * 32);
            debugGroup.addActor(square);
        }
    }
}
