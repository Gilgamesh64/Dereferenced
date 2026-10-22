package com.mygdx.dialogues;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.bladecoder.ink.runtime.Choice;
import com.bladecoder.ink.runtime.Story;
import com.mygdx.Data;
import com.mygdx.entities.helpers.GameActor;
import com.mygdx.hud.Hud;
import com.mygdx.resources.RM;

import java.util.List;

public class Dialogue extends Actor {
    public static boolean dialogueActive = false;
    private final Story story;
    private final Table table;
    private final TypewriterEffect typer;
    private boolean running = true;

    public Dialogue(GameStory gs, GameActor actor) {
        this.story = gs.getStory();
        gs.setActor(actor);

        try {
            story.resetState();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Pixmap tableColor = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        tableColor.setColor(0, 0, 0, 0.5f);
        tableColor.fill();
        Texture tableTexture = new Texture(tableColor);
        tableColor.dispose();
        TextureRegionDrawable bg = new TextureRegionDrawable(new TextureRegion(tableTexture));

        Label dialogueLabel = new Label("", RM.get().skin().get("dialogue", Label.LabelStyle.class));
        dialogueLabel.setWrap(true);
        dialogueLabel.setAlignment(Align.center);

        typer = new TypewriterEffect(dialogueLabel);

        table = new Table();
        table.setFillParent(true);
        table.defaults().expand().fillX();

        table.setBackground(bg);

        table.add(dialogueLabel)
                .center().top()
                .colspan(100)
                .pad(100, 80, 20, 80);
        table.row();

        Hud.stage().addActor(table);

        Gdx.app.postRunnable(this::continueStory); //Does this the next frame

        Dialogue.dialogueActive = true;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Skip typewriter if running
        if (typer.isRunning() && Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
            typer.skip();
        }
    }

    public void continueStory() {
        if (!story.canContinue()) {
            removeDialogue();
            return;
        }

        clearChoices();

        try {
            // Continue story if possible
            if (story.canContinue()) {
                String text = story.Continue().trim();

                typer.start(text, 0.02f, () -> {
                    // After typewriter finishes, wait for click if no choices
                    if (story.getCurrentChoices().isEmpty()) {
                        waitForClickToContinue();
                    }
                });
            }

            // Show choices if any
            if (!story.getCurrentChoices().isEmpty()) {
                showChoices(story.getCurrentChoices());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void waitForClickToContinue() {
        // Full-screen transparent actor that catches a single click
        Image clickCatcher = new Image();
        clickCatcher.setFillParent(true);
        clickCatcher.setTouchable(Touchable.enabled);
        clickCatcher.debug();

        Gdx.input.setInputProcessor(getStage());
        clickCatcher.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickCatcher.remove();
                continueStory();
            }
        });

        Hud.stage().addActor(clickCatcher);
    }

    private void showChoices(List<Choice> choicesList) {
        for (int i = 0; i < choicesList.size(); i++) {
            final int index = i;
            Choice choice = choicesList.get(i);

            Label choiceLabel = new Label(choice.getText(), RM.get().skin().get("choice", Label.LabelStyle.class));
            choiceLabel.setTouchable(Touchable.enabled);
            choiceLabel.setAlignment(Align.center);

            choiceLabel.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    try {
                        story.chooseChoiceIndex(index);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    continueStory();
                }
            });

            table.add(choiceLabel)
                    .width((table.getWidth() / choicesList.size()) - 80)
                    .pad(20);
        }
    }

    private void clearChoices() {
        if (table.getChildren().size > 1) {
            table.getChildren().removeRange(1, table.getChildren().size - 1);
        }
    }

    private void removeDialogue() {
        Dialogue.dialogueActive = false;
        running = false;
        table.remove();
        remove();
    }

    public boolean isRunning() {
        return running;
    }

}
