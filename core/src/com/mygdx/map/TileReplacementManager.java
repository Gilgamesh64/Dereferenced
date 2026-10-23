package com.mygdx.map;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class TileReplacementManager {
    private TiledMapTile blockerTile = null;
    private TiledMapTile blockableTile = null;
    private boolean isBlocked = false;
    private final TileReplacementEnum category;
    private final TiledMap map;

    public TileReplacementManager(TiledMap map, TileReplacementEnum category, TiledMapTile blocker) {
        this.map = map;
        this.category = category;
        this.blockerTile = blocker;
    }

    public void blockTiles() {

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("background");
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);

                if (cell == null)
                    continue;
                if (cell.getTile() == null)
                    continue;

                MapProperties properties = cell.getTile().getProperties();

                if (properties.get("blockable") == null || properties.get("category") == null) continue;

                if (!properties.get("category").equals(category.toString())) continue;

                blockableTile = cell.getTile();
                cell.setTile(blockerTile);


            }
        }
    }

    public void unBlockTiles() {

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("background");
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);

                if (cell == null)
                    continue;
                if (cell.getTile() == null)
                    continue;

                MapProperties properties = cell.getTile().getProperties();

                if (properties.get("blocker") == null || properties.get("category") == null) continue;

                if (!properties.get("category").equals(category.toString())) continue;

                cell.setTile(blockableTile);
            }
        }
    }

    public void handle() {
        /*if (isBlocked)
            unBlockTiles();
        else
            blockTiles(); */

        isBlocked = !isBlocked;
    }

    public TileReplacementEnum getCategory() {
        return category;
    }
}
