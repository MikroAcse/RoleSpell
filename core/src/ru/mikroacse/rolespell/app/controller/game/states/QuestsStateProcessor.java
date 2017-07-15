package ru.mikroacse.rolespell.app.controller.game.states;

import ru.mikroacse.rolespell.app.controller.game.GameController;
import ru.mikroacse.rolespell.app.controller.shared.InputAdapter;
import ru.mikroacse.rolespell.app.model.game.GameModel;
import ru.mikroacse.rolespell.app.view.game.GameRenderer;

/**
 * Created by Vitaly Rudenko on 07-Jun-17.
 */
public class QuestsStateProcessor extends StateProcessor {
    public QuestsStateProcessor(GameController controller) {
        super(controller);
    }

    @Override
    public void process() {
        InputAdapter input = InputAdapter.instance;
        GameRenderer renderer = getController().getRenderer();
        GameModel model = getController().getModel();

        if (input.hasScrolled()) {
            int scrollDelta = input.getScrollDelta() > 0 ? 1 : -1;

            // TODO: magic number (scroll amount)
            renderer.getQuestsView().scrollBy(scrollDelta * 30);
        }
    }
}
