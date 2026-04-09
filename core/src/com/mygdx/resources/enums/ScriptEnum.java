package com.mygdx.resources.enums;

public enum ScriptEnum {
    BLACKMARKETEER_1("npcs/blackmarketeer_1"),
    CANTEEN("npcs/canteen");

    public String path;

    ScriptEnum(String path){
        this.path = "assets/scripts/" + path + ".gcs";
    }
    
}
