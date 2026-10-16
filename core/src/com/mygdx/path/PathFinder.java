package com.mygdx.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.entities.helpers.GameActor;
import com.mygdx.screens.ScreensManager;

public class PathFinder {

    private static final int[][] DIRECTIONS = {
        { 0, 1 },   // Up
        { 0, -1 },  // Down
        { -1, 0 },  // Left
        { 1, 0 }    // Right
    };

    public static Path find(GameActor from, Vector2 to){
        GridPoint2 start = actorToGrid(from);
        GridPoint2 goal = vecToGrid(to);

        if (IsOutsideMap(start) || IsOutsideMap(goal)) {
            return Path.of();
        }

        if (!ScreensManager.getCurrentPlayableScreen().getMapManager().isWalkable(start.x, start.y)
                || !ScreensManager.getCurrentPlayableScreen().getMapManager().isWalkable(goal.x, goal.y)) {
            return Path.of();
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> n.f));

        Map<GridPoint2, Float> gScore = new HashMap<>();
        Map<GridPoint2, GridPoint2> cameFrom = new HashMap<>();
        Set<GridPoint2> closedSet = new HashSet<>();

        gScore.put(start, 0f);
        openSet.add(new Node(start, heuristic(start, goal)));

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();
            GridPoint2 current = currentNode.position;

            if (closedSet.contains(current)) {
                continue;
            }

            if (current.equals(goal)) {
                return Path.of(reconstructPath(cameFrom, current));
            }

            closedSet.add(current);

            for (int[] direction : DIRECTIONS) {
                int nextCol = current.x + direction[0];
                int nextRow = current.y + direction[1];

                if (!ScreensManager.getCurrentPlayableScreen().getMapManager().isWalkable(nextCol * 32, nextRow * 32)) {
                    continue;
                }

                GridPoint2 next = new GridPoint2(nextCol, nextRow);

                if (closedSet.contains(next)) {
                    continue;
                }

                float tentativeG = gScore.get(current) + 1f;
                Float oldG = gScore.get(next);

                if (oldG == null || tentativeG < oldG) {
                    cameFrom.put(next, current);
                    gScore.put(next, tentativeG);

                    float f = tentativeG + heuristic(next, goal);
                    openSet.add(new Node(next, f));
                }
            }
        }

        return Path.of();
    }

    public static Path find(GameActor from, GameActor to) {
        return find(from, to.center);
    }

    private static GridPoint2 actorToGrid(GameActor actor) {
        int col = MathUtils.floor(actor.center.x / 32);
        int row = MathUtils.floor(actor.center.y / 32);

        return new GridPoint2(col, row);
    }

    private static GridPoint2 vecToGrid(Vector2 vec){
        int col = MathUtils.floor(vec.x / 32);
        int row = MathUtils.floor(vec.y / 32);

        return new GridPoint2(col, row);
    }

    private static boolean IsOutsideMap(GridPoint2 point) {
        return point.x < 0 || point.x >= ScreensManager.getCurrentPlayableScreen().getMapManager().getRows()
                || point.y < 0 || point.y >= ScreensManager.getCurrentPlayableScreen().getMapManager().getCols();
    }

    private static float heuristic(GridPoint2 a, GridPoint2 b) {
        return Math.abs(a.x - b.x)
                + Math.abs(a.y - b.y);
    }

    private static List<GridPoint2> reconstructPath( Map<GridPoint2, GridPoint2> cameFrom, GridPoint2 current) {

        List<GridPoint2> path = new ArrayList<>();

        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);
        }

        Collections.reverse(path);
        if(!path.isEmpty()) path.remove(0);
        return path;
    }

    private static class Node {
        final GridPoint2 position;
        final float f;

        Node(GridPoint2 position, float f) {
            this.position = new GridPoint2(position);
            this.f = f;
        }
    }
}
