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
import com.mygdx.Data;
import com.mygdx.entities.helpers.GameActor;
import com.mygdx.map.TileMapCollisionsManager;
import com.mygdx.screens.ScreensManager;

public class PathFinder {
    public Path find(Vector2 start, Vector2 end) {
        return Path.of(
                new Vector2(Data.TILE * 50, Data.TILE * 25),
                new Vector2(Data.TILE * 40, Data.TILE * 40),
                new Vector2(Data.TILE * 60, Data.TILE * 30),
                new Vector2(end.x, end.y)
        );
    }

    private final GameActor from;
    private final GameActor to;

    private final int rows;
    private final int cols;

    private final float tileWidth;
    private final float tileHeight;

    private static final int[][] DIRECTIONS = {
        { 0, 1 },   // Up
        { 0, -1 },  // Down
        { -1, 0 },  // Left
        { 1, 0 }    // Right
    };

    public PathFinder(
            GameActor from,
            GameActor to,
            int rows,
            int cols,
            float tileWidth,
            float tileHeight) {

        this.from = from;
        this.to = to;
        this.rows = rows;
        this.cols = cols;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public List<GridPoint2> findPath() {
        GridPoint2 start = actorToGrid(from);
        GridPoint2 goal = actorToGrid(to);

        if (!isInsideMap(start) || !isInsideMap(goal)) {
            return Collections.emptyList();
        }

        if (!TileMapCollisionsManager.isWalkable(start.y, start.x)
                || !TileMapCollisionsManager.isWalkable(goal.y, goal.x)) {
            return Collections.emptyList();
        }

        PriorityQueue<Node> openSet =
                new PriorityQueue<>(
                    Comparator.comparingDouble(n -> n.f)
                );

        Map<GridPoint2, Float> gScore = new HashMap<>();
        Map<GridPoint2, GridPoint2> cameFrom = new HashMap<>();
        Set<GridPoint2> closedSet = new HashSet<>();

        gScore.put(start, 0f);
        openSet.add(new Node(start, heuristic(start, goal)));

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();
            GridPoint2 current = currentNode.position;

            // Ignore obsolete queue entries.
            if (closedSet.contains(current)) {
                continue;
            }

            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }

            closedSet.add(current);

            for (int[] direction : DIRECTIONS) {
                int nextCol = current.x + direction[0];
                int nextRow = current.y + direction[1];

                if (!TileMapCollisionsManager.isWalkable(nextRow, nextCol)) {
                    continue;
                }

                GridPoint2 next =
                        new GridPoint2(nextCol, nextRow);

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

        // No route was found.
        return Collections.emptyList();
    }

    private GridPoint2 actorToGrid(GameActor actor) {
        float centerX = actor.getX() + actor.getWidth() / 2f;
        float centerY = actor.getY() + actor.getHeight() / 2f;

        int col = MathUtils.floor(centerX / tileWidth);
        int row = MathUtils.floor(centerY / tileHeight);

        return new GridPoint2(col, row);
    }

    private boolean isInsideMap(GridPoint2 point) {
        return point.x >= 0 && point.x < cols
                && point.y >= 0 && point.y < rows;
    }

    private float heuristic(GridPoint2 a, GridPoint2 b) {
        return Math.abs(a.x - b.x)
                + Math.abs(a.y - b.y);
    }

    private List<GridPoint2> reconstructPath(
            Map<GridPoint2, GridPoint2> cameFrom,
            GridPoint2 current) {

        List<GridPoint2> path = new ArrayList<>();

        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);
        }

        Collections.reverse(path);
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


    public static Vector2 getMarker(String name) {
        return ScreensManager.getCurrentPlayableScreen().tileSetManager.markersMap.get(name);
    }
}
