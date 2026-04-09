package com.mygdx.entities.npcs;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.resources.enums.AnimationEnum;
import com.mygdx.resources.enums.DialogueEnum;
import com.mygdx.resources.enums.ScriptEnum;
import com.mygdx.resources.enums.TextureEnum;

public class NPCBuilder {
    protected Vector2 coordinates, size = new Vector2(16, 32);
    protected AnimationEnum anim;
    protected DialogueEnum story;
    protected ScriptEnum autoStartedScript;
    protected TextureEnum startingAnimation;
    protected NpcConstruction construction;
    protected NpcInteraction interaction;
    protected NpcUpdate update;

    public static NPCBuilder create(AnimationEnum anim, int x, int y) {
        return new NPCBuilder(anim, new Vector2(x, y));
    }

    private NPCBuilder(AnimationEnum anim, Vector2 coords) {
        this.anim = anim;
        this.coordinates = coords;
    }

    public NPCBuilder size(float x, float y) {
        this.size = new Vector2(x, y);
        return this;
    }

    public NPCBuilder story(DialogueEnum e) {
        this.story = e;
        return this;
    }

    public NPCBuilder autoStartedScript(ScriptEnum e) {
        this.autoStartedScript = e;
        return this;
    }

    public NPCBuilder startingAnimation(TextureEnum e) {
        this.startingAnimation = e;
        return this;
    }

    public NPCBuilder onConstruction(NpcConstruction construction){
        this.construction = construction;
        return this;
    }

    public NPCBuilder onInteraction(NpcInteraction interaction) {
        this.interaction = interaction;
        return this;
    }

    public NPCBuilder onUpdate(NpcUpdate update) {
        this.update = update;
        return this;
    }

    public NPC build() {
        return new NPC(this);
    }
}
