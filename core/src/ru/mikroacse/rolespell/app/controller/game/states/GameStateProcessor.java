package ru.mikroacse.rolespell.app.controller.game.states;

import com.badlogic.gdx.Input;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.controller.game.GameController;
import ru.mikroacse.rolespell.app.controller.game.InputAdapter;
import ru.mikroacse.rolespell.app.model.game.GameModel;
import ru.mikroacse.rolespell.app.model.game.entities.DroppedItem;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.view.game.GameRenderer;
import ru.mikroacse.rolespell.app.view.game.items.ItemView;
import ru.mikroacse.rolespell.app.view.game.ui.GameCursor;

/**
 * Created by MikroAcse on 01-May-17.
 */
public class GameStateProcessor extends StateProcessor {
    public GameStateProcessor(GameController controller) {
        super(controller);
    }

    @Override
    public void process(GameRenderer.State state) {
        if (state != GameRenderer.State.GAME) {
            return;
        }

        InputAdapter input = getController().getInput();
        GameRenderer renderer = getController().getRenderer();
        GameModel model = getController().getModel();

        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

        InputAdapter.Button mouseLeft = input.getButton(Input.Buttons.LEFT);

        IntVector2 cell = renderer.getWorldRenderer().stageToCell(mouseX, mouseY);

        renderer.getWorldRenderer().setSelectorPosition(cell.x, cell.y);

        Entity entity = model.getWorld().getEntityAt(cell);

        if (entity != null) {
            if (entity.getType() == EntityType.DROPPED_ITEM) {
                if (mouseLeft.isDown) {
                    renderer.setCursor(GameCursor.Type.DRAG);
                } else {
                    renderer.setCursor(GameCursor.Type.TAKE);
                }
            } else if (entity.getType() == EntityType.NPC) {
                renderer.setCursor(GameCursor.Type.ATTACK);
            } else {
                renderer.setCursor(GameCursor.Type.POINTER);
            }
        } else {
            renderer.setCursor(GameCursor.Type.POINTER);
        }

        if (mouseLeft.justPressed) {
            model.stopAttacking();

            // TODO: better solution
            if (entity != null) {
                if (entity.getType() == EntityType.NPC) {
                    model.tryAttack(cell.x, cell.y);
                } else if (entity.getType() == EntityType.DROPPED_ITEM) {
                    DroppedItem droppedItem = (DroppedItem) entity;

                    MovementComponent movement = model.getControllable().getComponent(MovementComponent.class);
                    MovementComponent entityMovement = entity.getComponent(MovementComponent.class);

                    // TODO: magic number (maximum pickup distance)
                    if (movement.getPosition().distance(entityMovement.getPosition()) < 3) {
                        InventoryStateProcessor inventoryState = getController().getInventoryState();
                        ItemView droppedItemView = new ItemView(droppedItem.getItem());

                        droppedItem.remove();

                        inventoryState.startDrag(droppedItemView);

                        renderer.setState(GameRenderer.State.INVENTORY);
                    } else {
                        model.tryRouteTo(cell.x, cell.y);
                    }
                }
            } else {
                model.tryRouteTo(cell.x, cell.y);
            }
        }
    }
}
