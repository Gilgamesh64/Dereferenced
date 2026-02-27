package com.mygdx.resources.enums;

public enum TextureEnum {
    PLAYER_IDLE_DOWN,
    PLAYER_IDLE_UP,
    PLAYER_IDLE_LEFT,
    PLAYER_IDLE_RIGHT,
    PLAYER_WALK_DOWN(3),
    PLAYER_WALK_UP(3),
    PLAYER_WALK_LEFT(1),
    PLAYER_WALK_RIGHT(1),

    // NPCS
    JERKINS_IDLE_DOWN(3),
    JERKINS_IDLE_UP(1),
    JERKINS_IDLE_LEFT(3),
    JERKINS_IDLE_RIGHT(3),
    JERKINS_WALK_DOWN(3),
    JERKINS_WALK_UP(3),
    JERKINS_WALK_LEFT(3),
    JERKINS_WALK_RIGHT(3),

    BLACKMARKETEER_IDLE_DOWN(3),
    BLACKMARKETEER_IDLE_UP(1),
    BLACKMARKETEER_IDLE_LEFT(3),
    BLACKMARKETEER_IDLE_RIGHT(3),
    BLACKMARKETEER_WALK_DOWN(3),
    BLACKMARKETEER_WALK_UP(3),
    BLACKMARKETEER_WALK_LEFT(3),
    BLACKMARKETEER_WALK_RIGHT(3),
    BLACKMARKETEER_SLEEPING(3, 0.01f, 0),

    TEST,

    // PROJECTILES
    STONE,

    BULLET,

    // WEAPONS
    DEFAULT,

    CHAINGUN,

    USBLADE,
    SHIELD,

    // BUILDINGS
    ABANDONED_TURNED_OFF(4),
    ABANDONED_TURNED_ON(4),

    ELECTRO(11),

    ENERGYPLANT_TURNED_OFF(2),
    ENERGYPLANT_TURNED_ON(2),

    GLUTTONY_DONUT(2),

    MARMOT_PIZZA(5),

    SKYSCRAPER_1_BACK,
    SKYSCRAPER_1_FRONT,
    SKYSCRAPER_1_SIDE,

    SKYSCRAPER_2_BACK,
    SKYSCRAPER_2_FRONT,

    SKYSCRAPER_3_FRONT,

    SKYSCRAPER_HIGH,

    SKYSCRAPER_L,

    SKYSCRAPER_MEDIUM,

    SKYSCRAPER_SMALL,

    SKYSCRAPER_U,

    SYNTH_TURNED_OFF(4),
    SYNTH_TURNED_ON(4),
    SKYSCRAPER_PROTOTYPE,

    // MAP COMPONENTS
    LAMP(2),
    LONG_LAMP,
    TRAFFIC_LIGHT,
    GLASS_DOOR(9, 0.1f, 3),
    MARCO_LAMP(4),
    SIDE_WALK(1),
    RUBBISH(3),
    DOUBLE_DOOR_OPENING(32, 0.02f, 0f),
    DOUBLE_DOOR_CLOSING(32, 0.02f, 0f),
    GLASS_DOOR_OPENING(32, 0.01f, 0f),
    GLASS_DOOR_CLOSING(32, 0.01f, 0),
    SAVING_TREE,

    // EFFECTS
    EXPLOSION(8, 0.05f, 0),

    // OTHERS
    MARKER,
    SCOPE;

    public String path;
    public float animationRate = -1;
    public float delay = -1;
    public int frameCount = 1;

    /**
     * Animation included in a textureatlas
     * Path is trivial, should always be the same name as the enum
     * 
     * @param animationRate
     * @param delay
     * @param frameCount
     */
    TextureEnum(int frameCount, float animationRate, float delay) {
        this.path = name().toLowerCase();
        this.animationRate = animationRate;
        this.delay = delay;
        this.frameCount = frameCount;
    }

    /**
     * Animation included in a textureatlas with default rate and delay
     * Lable is trivial, should always be the same name as the enum
     * 
     * @param animationRate
     * @param delay
     * @param frameCount
     */
    TextureEnum(int frameCount) {
        this.path = name().toLowerCase();
        this.frameCount = frameCount;
    }

    TextureEnum() {
        this(1);
    }

}
