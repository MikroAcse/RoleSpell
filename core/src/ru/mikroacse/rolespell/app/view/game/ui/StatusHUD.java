package ru.mikroacse.rolespell.app.view.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.media.AssetBundle;
import ru.mikroacse.rolespell.media.AssetManager;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.core.NumericParameter;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.core.Parameter;

/**
 * Created by MikroAcse on 04.04.2017.
 */
public class StatusHUD {
    private static final int STATUSBAR_WIDTH = 100;
    private static final int STATUSBAR_HEIGHT = 16;
    private static final int OFFSET = 5;
    
    private AssetBundle bundle;
    
    private Texture statusbar;
    
    public StatusHUD() {
        bundle = RoleSpell.getAssetManager().getBundle(AssetManager.Bundle.GAME);
        
        statusbar = bundle.getTexture("hud/statusbar");
    }
    
    public void draw(SpriteBatch batch, StatusComponent status, float x, float y) {
        for (Parameter parameter : status.getParameters()) {
            if (parameter instanceof NumericParameter) {
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
        
        batch.draw(statusbar, x, y, (int) (STATUSBAR_WIDTH * parameter.getPercentage()), STATUSBAR_HEIGHT);
        
        batch.setColor(tempColor);
    }
}
