package com.mygdx.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.Money;
import com.mygdx.resources.RM;
import com.ray3k.stripe.PopTable;

import java.util.Map;

public class Inventory {

    public PopTable table;
    private final Stage stage;
    private final Label moneyLabel;

    public Inventory(Stage stage) {
        this.stage = stage;

        table = new PopTable();
        table.setFillParent(true);
        table.setTouchable(Touchable.enabled);
        table.defaults().expand();

        table.setStageBackground(RM.get().skin().getDrawable("inventory"));

        Image moneyIcon = new Image(RM.get().skin().getDrawable("money"));
        moneyLabel = new Label(Money.get() + "", RM.get().skin());

        Button b = new Button(RM.get().skin());
        b.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Button clicked!");
            }
        });
        table.add(b).top().right().pad(40, 0, 0, 40).row();

        Table moneyGroup = new Table();
        moneyGroup.add(moneyIcon);
        moneyGroup.add(moneyLabel).padLeft(10);

        table.add(moneyGroup).bottom();

    }

    public void toggle() {
        if (table.isHidden())
            table.show(stage);
        else
            table.hide();
    }

    public void update() {

        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            toggle();
        }

        moneyLabel.setText(Money.get());
    }

    @SuppressWarnings ("unused")
    private void printTableData(Table printableTable) {
        Map<String, Float> data = Map.ofEntries(
                Map.entry("width", printableTable.getWidth()),
                Map.entry("height", printableTable.getHeight()),
                Map.entry("x", printableTable.getX()),
                Map.entry("y", printableTable.getY()),
                Map.entry("align", (float) printableTable.getAlign()),
                Map.entry("maxWidth", printableTable.getMaxWidth()),
                Map.entry("maxHeight", printableTable.getMaxHeight()),
                Map.entry("minWidth", printableTable.getMinWidth()),
                Map.entry("minHeight", printableTable.getMinHeight()),
                Map.entry("prefWidth", printableTable.getPrefWidth()),
                Map.entry("prefHeight", printableTable.getPrefHeight()));
        data.forEach((k, v) -> System.out.println(k + " = " + v));
    }
}