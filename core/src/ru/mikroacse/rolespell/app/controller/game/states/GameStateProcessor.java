package ru.mikroacse.rolespell.app.controller.game.states;

import com.badlogic.gdx.Input;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.controller.game.GameController;
import ru.mikroacse.rolespell.app.controller.game.InputAdapter;
import ru.mikroacse.rolespell.app.model.game.GameModel;
import ru.mikroacse.rolespell.app.view.game.GameRenderer;

/**
 * Created by MikroAcse on 01-May-17.
 */
public class GameStateProcessor extends StateProcessor {
    public GameStateProcessor(GameController controller) {
        super(controller);
    }
    
    @Override
    public void process() {
        InputAdapter input = getController().getInput();
        GameRenderer renderer = getController().getRenderer();
        GameModel model = getController().getModel();
        
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
    
        InputAdapter.Button mouseLeft = input.getButton(Input.Buttons.LEFT);
        InputAdapter.Button mouseRight = input.getButton(Input.Buttons.RIGHT);
    
        IntVector2 cell = renderer.mapToCell(renderer.stageToMap(mouseX, mouseY));
    
        if (mouseLeft.justPressed) {
            model.tryRouteTo(cell.x, cell.y);
        }
    
        if (mouseRight.justPressed) {
            model.tryAttack(cell.x, cell.y);
        }
    }
}
