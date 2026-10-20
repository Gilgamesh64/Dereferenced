package com.mygdx.map;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PointMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.Data;
import com.mygdx.GCStage;
import com.mygdx.entities.map.Building;
import com.mygdx.entities.map.Component;
import com.mygdx.entities.map.InvisibleDoor;
import com.mygdx.entities.map.TextureDoor;
import com.mygdx.resources.RM;
import com.mygdx.resources.enums.AtlasEnum;
import com.mygdx.resources.enums.MapEnum;
import com.mygdx.resources.enums.TextureEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MapManager implements Telegraph {
    private static MapManager instance;
    private final HashMap<String, Vector2> markersMap = new HashMap<>();
    private final TiledMapRenderer tiledMapRenderer;
    private final TiledMap map;
    private final ArrayList<TileReplacementManager> tileReplace = new ArrayList<>();
    private final TiledMapTileLayer background;

    private boolean fightTransitionInCooldown = false;

    public MapManager(MapEnum e) {
        instance = this;
        map = RM.get().getMap(e);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
        background = getTileLayer("background");

        MapLayer doorLayer = map.getLayers().get("doors"); //explicitly not using getLayer because i don't want throw on null
        if (doorLayer != null)
            loadDoors(doorLayer);
        else if (Data.PRINT_DEBUG)
            System.out.println("Are you aware there is no door layer right?");


        MapLayer buildingsLayer = map.getLayers().get("buildings");
        if (buildingsLayer != null)
            loadBuildings(buildingsLayer);
        else if(Data.PRINT_DEBUG)
            System.out.println("Are you aware there is no buildings layer right?");


        MapLayer componentsLayer = map.getLayers().get("components");
        if (componentsLayer != null)
            loadComponents(componentsLayer);
        else if(Data.PRINT_DEBUG)
            System.out.println("Are you aware there is no components layer right?");


        MapLayer markersLayer = map.getLayers().get("markers");
        if (markersLayer != null)
            loadMarkers(markersLayer);
        else if(Data.PRINT_DEBUG)
            System.out.println("Are you aware there is no markers layer right?");


        loadReplacers();
    }

    public static MapManager get() {return instance;}

    public static void set(MapManager mapManager) { MapManager.instance = mapManager; }

    public void render(OrthographicCamera camera) {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    public TiledMap getMap() { return map; }
    public int getCols(){ return background.getWidth(); }
    public int getRows(){ return background.getHeight(); }

    private void loadBuildings(MapLayer buildingsLayer) throws IllegalStateException{

        for (MapObject obj : buildingsLayer.getObjects()) {
            if (obj instanceof RectangleMapObject rectObj) {
                Rectangle rect = rectObj.getRectangle();

                TextureEnum texture = TextureEnum.valueOf(obj.getName());

                TextureRegion region = RM.get().getAtlas(AtlasEnum.BUILDINGS).findRegion(texture.path);
                if (region == null) {
                    throw new IllegalStateException("ResourceEnum: " + texture +
                            " not found in region, \nthe .png file should be in the folder: assets/raw/"
                            + AtlasEnum.BUILDINGS
                            + " \ncheck your spelling and pack all the assets");
                }
                GCStage.get().addActor(new Building(rect.getX(), rect.getY(), texture));
            }
        }

    }

    private void loadComponents(MapLayer componentsLayer) {

        for (MapObject obj : componentsLayer.getObjects()) {
            if (obj instanceof RectangleMapObject rectObj) {
                Rectangle rect = rectObj.getRectangle();

                TextureEnum texture = TextureEnum.valueOf(obj.getName());

                TextureRegion region = RM.get().getAtlas(AtlasEnum.COMPONENTS).findRegion(texture.path);
                if (region == null) {
                    throw new RuntimeException("ResourceEnum: " + texture +
                            " not found in region, \nthe .png file should be in the folder: assets/raw/"
                            + AtlasEnum.COMPONENTS
                            + " \ncheck your spelling and pack all the assets");
                }
                GCStage.get().addActor(new Component(rect.getX(), rect.getY(), texture));
            }
        }
    }

    private String verifyDoorName(String name){
        if (name == null) {
            throw new IllegalArgumentException("Door has no name");
        }

        String[] parts = name.split("-", -1);

        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new IllegalArgumentException(
                    "Invalid door name '" + name + "'. Expected format: source-destination"
            );
        }

        return parts[1] + "-" + parts[0];
    }

    private void loadDoors(MapLayer layer) {
        for (MapObject obj : layer.getObjects()) {
            if (obj instanceof RectangleMapObject rectObj) {
                Rectangle rect = rectObj.getRectangle();
                String name = obj.getName();
                String dst = verifyDoorName(name);
                String dir = Objects.requireNonNull(obj.getProperties().get("dir")).toString();
                float x = rect.getX(), y = rect.getY();
                float width = rect.getWidth(), height = rect.getHeight();
                GCStage.get().addActor(new InvisibleDoor(name, dst, dir, x, y, width, height));

            } else if (obj instanceof PointMapObject pointObj) {
                String name = obj.getName();
                String dst = verifyDoorName(name);
                String dir = Objects.requireNonNull(obj.getProperties().get("dir")).toString();
                int size = (int) Objects.requireNonNullElse(obj.getProperties().get("size"), 1);
                float x = pointObj.getPoint().x - (16 * size), y = pointObj.getPoint().y;
                String texture = Objects.requireNonNull(obj.getProperties().get("texture")).toString();
                GCStage.get().addActor(new TextureDoor(name, dst, dir, x, y, size, texture));
            }
        }
    }

    private void loadReplacers() {

        for (TiledMapTile tile : map.getTileSets().getTileSet(0)) {

            var prop = tile.getProperties();

            if (prop.get("category") == null || prop.get("blocker") == null)
                continue;

            tileReplace.add(new TileReplacementManager(map,
                    TileReplacementEnum.valueOf(prop.get("category").toString()), tile));
        }
    }

    private void loadMarkers(MapLayer markersLayer) {
        for (MapObject marker : markersLayer.getObjects()) {
            if (marker instanceof PointMapObject pointMarker) {
                Vector2 coords = pointMarker.getPoint();
                markersMap.put(marker.getName(), coords);
            }
        }
    }

    public Vector2 getMarker(String name){
        Vector2 marker = markersMap.get(name);

        if (marker == null) {
            throw new IllegalArgumentException(
                    "Unknown marker: " + name
            );
        }

        return marker.cpy();
    }

    public Vector2 getRandomMarker(){
        if (markersMap.isEmpty()) {
            throw new IllegalStateException("No markers loaded");
        }

        int index = MathUtils.random(markersMap.size() - 1);

        return new ArrayList<>(markersMap.values())
                .get(index)
                .cpy();
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        for (TileReplacementManager m : tileReplace) {
            if (msg.message == m.getCategory().getMsg().code) {
                m.handle();
                System.out.println("handled");
            }
        }

        return true;
    }

    /**
     * @param x
     * @param y
     * @param layer
     * @return tile associated to layer
     * @throws IllegalStateException if anything is null
     */
    public TiledMapTile getTile(float x, float y, TiledMapTileLayer layer) throws IllegalStateException {
        if (layer == null)
            throw new IllegalStateException("layer is null in method: getTile");

        int col = MathUtils.floor(x / Data.TILE);
        int row = MathUtils.floor(y / Data.TILE);
        Cell cell = layer.getCell(col, row);

        if (cell == null || cell.getTile() == null) {
            throw new IllegalStateException(
                    "No tile at world position (" + x + ", " + y
                            + "), cell (" + col + ", " + row + ")"
            );
        }
        return cell.getTile();
    }


    public TiledMapTileLayer getTileLayer(String name){
        MapLayer layer = map.getLayers().get(name);
        if(layer == null)
            throw new IllegalArgumentException("layer not found for name:" + name);
        if(layer instanceof TiledMapTileLayer tmtl){
            return tmtl;
        }
        throw new IllegalStateException("layer with name: " + name + ". Is not TiledMapTileLayer");
    }
    public MapLayer getLayer(String name){
        MapLayer layer = map.getLayers().get(name);
        if(layer == null)
            throw new IllegalArgumentException("layer not found for name:" + name);
        return layer;
    }

    /**
     * @param x
     * @param y
     * @return true if the tile at the given coordinates is walkable
     * @throws IllegalStateException if anything is null
     */
    public boolean isWalkable(float x, float y) throws IllegalStateException {
        TiledMapTile tile = getTile(x, y, background);

        return tile.getProperties().get("blocked") == null;
    }

    public boolean changeFightingState() {
        TiledMapTile playerTile = getTile(GCStage.get().getPlayer().getX(), GCStage.get().getPlayer().getY(), background);
        if (!fightTransitionInCooldown && playerTile.getProperties().containsKey("transition")) {
            fightTransitionInCooldown = true;
            return true;
        }

        if (!playerTile.getProperties().containsKey("transition") && fightTransitionInCooldown) {
            fightTransitionInCooldown = false;
        }
        return false;
    }
}
