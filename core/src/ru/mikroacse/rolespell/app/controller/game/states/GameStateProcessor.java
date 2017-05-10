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
import ru.mikroacse.rolespell.app.model.game.inventory.ItemList;
import ru.mikroacse.rolespell.app.view.game.GameRenderer;
import ru.mikroacse.rolespell.app.view.game.inventory.ItemListView;
import ru.mikroacse.rolespell.app.view.game.items.ItemView;
import ru.mikroacse.rolespell.app.view.game.ui.GameCursor;

/**
 * Created by MikroAcse on 01-May-17.
 */
public class GameStateProcessor extends StateProcessor {
    private int hotbarSelected;

    public GameStateProcessor(GameController controller) {
        super(controller);

        hotbarSelected = 0;
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

        IntVector2 cell = renderer.getWorldRenderer().getMapRenderer().stageToCell(mouseX, mouseY);

        renderer.getWorldRenderer().setSelectorPosition(cell.x, cell.y);

        Entity entity = model.getWorld().getEntityAt(cell);

        if (entity != null) {
            if (entity.getType() == EntityType.DROPPED_ITEM) {
                renderer.setCursor(GameCursor.Cursor.TAKE);
            } else if (entity.getType() == EntityType.NPC) {
                renderer.setCursor(GameCursor.Cursor.ATTACK); // maduk ghost
            } else {
                renderer.setCursor(GameCursor.Cursor.POINTER);
            }
        } else {
            renderer.setCursor(GameCursor.Cursor.POINTER);
        }

        if (mouseRight.justPressed) {
            model.tryRouteTo(cell.x, cell.y);
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

                        getController().setState(GameRenderer.State.INVENTORY);

                        droppedItem.remove();

                        inventoryState.startDrag(droppedItemView);
                    } else {
                        model.tryRouteTo(cell.x, cell.y);
                    }
                }
            } else {
                model.tryRouteTo(cell.x, cell.y);
            }
        }

        processHotbar();
    }

    private void processHotbar() {
        InputAdapter input = getController().getInput();
        GameRenderer renderer = getController().getRenderer();
        GameModel model = getController().getModel();

        ItemListView hotbarView = renderer.getHotbarView();
        ItemList hotbar = hotbarView.getItemList();

        int key = Input.Keys.NUM_1;
        for (int i = 0; i < hotbar.getSize(); i++) {
            if(input.getButton(key).justPressed) {
                hotbarSelected = i;
                updateHotbar();
                break;
            }

            key++;
        }
    }

    private void updateHotbar() {
        GameRenderer renderer = getController().getRenderer();
        ItemListView hotbarView = renderer.getHotbarView();

        hotbarView.select(hotbarSelected, true);
    }

    @Override
    public void resume() {
        super.resume();

        updateHotbar();
    }

    @Override
    public void pause() {
        super.pause();

        GameRenderer renderer = getController().getRenderer();
        renderer.setCursor(GameCursor.Cursor.POINTER);
    }
}
