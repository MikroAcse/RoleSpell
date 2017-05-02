package ru.mikroacse.rolespell.app.view.game.status;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.core.NumericParameter;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.core.Parameter;
import ru.mikroacse.rolespell.media.AssetManager;

/**
 * Created by MikroAcse on 01-May-17.
 */
public class ParameterView extends Image {
    private NumericParameter parameter;
    
    public ParameterView(NumericParameter parameter) {
        super(RoleSpell.getAssetManager()
                       .getBundle(AssetManager.Bundle.GAME)
                       .getTexture("hud/statusbar"));
        
        setParameter(parameter);
    }
    
    public NumericParameter getParameter() {
        return parameter;
    }
    
    public void setParameter(NumericParameter parameter) {
        this.parameter = parameter;
    
        switch (parameter.getType()) {
            case HEALTH:
                setColor(Color.SCARLET);
                break;
            case MANA:
                setColor(Color.CYAN);
                break;
            case STAMINA:
                setColor(Color.CHARTREUSE);
                break;
            case EXPERIENCE:
                setColor(Color.WHITE);
                break;
        }
    }
}
