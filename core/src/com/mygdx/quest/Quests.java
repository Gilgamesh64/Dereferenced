package com.mygdx.quest;

public enum Quests {
    REFLECTION,
    BANDITS;

    private String state = "";

    public void set(String state){
        this.state = state;
    }
    public String get(){
        return state;
    }
}
