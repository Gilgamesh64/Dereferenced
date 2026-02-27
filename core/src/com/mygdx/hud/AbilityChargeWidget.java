package com.mygdx.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.mygdx.entities.AbilityChargeMeter;
import com.mygdx.entities.helpers.GameActor;
import com.mygdx.resources.RM;

public class AbilityChargeWidget extends GameActor {

    private final Drawable background;
    private final Drawable fill;
    private final Drawable segment;

    public AbilityChargeWidget() {
        this.background = RM.get().skin().getDrawable("ability_bar_bg");
        this.fill = RM.get().skin().getDrawable("ability_bar_fill");
        this.segment = RM.get().skin().getDrawable("ability_bar_segment");

        setSize(background.getMinWidth(), background.getMinHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        background.draw(batch, getX(), getY(), getWidth(), getHeight());

        float filled = getWidth() * AbilityChargeMeter.getChargePercent();
        fill.draw(batch, getX(), getY(), filled, getHeight());

        float segmentWidth = getWidth() / 3f;
        for (int i = 1; i < 3; i++) {
            segment.draw(batch,
                    getX() + i * segmentWidth - 1,
                    getY(),
                    2,
                    getHeight());
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        AbilityChargeMeter.update(delta);
    }
}