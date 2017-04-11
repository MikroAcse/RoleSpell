package ru.mikroacse.rolespell.view.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.mikroacse.rolespell.model.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.core.NumericParameter;
import ru.mikroacse.rolespell.model.entities.components.status.parameters.core.Parameter;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public class StatusHUD {
    private static final int STATUSBAR_WIDTH = 100;
    private static final int STATUSBAR_HEIGHT = 16;
    private static final int OFFSET = 5;

    private Texture texture;

    public StatusHUD() {
        texture = new Texture("data/hud/statusbar.png");
    }

    public void draw(StatusComponent status, SpriteBatch batch, float x, float y) {
        for (Parameter parameter : status.getParameters()) {
            if(parameter instanceof NumericParameter) {
                drawParameter((NumericParameter) parameter, batch, x, y);

                y += STATUSBAR_HEIGHT + OFFSET;
            }
        }
    }

    private void drawParameter(NumericParameter parameter, SpriteBatch batch, float x, float y) {
        Color tempColor = batch.getColor();

        switch (parameter.getType()) {
            case HEALTH:
                batch.setColor(Color.SCARLET);
                break;
            case MANA:
                batch.setColor(Color.CYAN);
                break;
            case STAMINA:
                batch.setColor(Color.CHARTREUSE);
                break;
            case EXPERIENCE:
                batch.setColor(Color.WHITE);
                break;
        }

        batch.draw(texture, x, y, (int) (STATUSBAR_WIDTH * parameter.getPercentage()), STATUSBAR_HEIGHT);

        batch.setColor(tempColor);
    }
}
