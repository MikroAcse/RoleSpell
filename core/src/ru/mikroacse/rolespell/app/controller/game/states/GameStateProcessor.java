package ru.mikroacse.rolespell.app.controller.game.states;

import com.badlogic.gdx.Input;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.controller.game.GameController;
import ru.mikroacse.rolespell.app.controller.shared.InputAdapter;
import ru.mikroacse.rolespell.app.model.game.GameModel;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.objects.DroppedItem;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.model.game.inventory.ItemList;
import ru.mikroacse.rolespell.app.view.game.GameRenderer;
import ru.mikroacse.rolespell.app.view.game.inventory.ItemListView;
import ru.mikroacse.rolespell.app.view.game.items.ItemView;
import ru.mikroacse.rolespell.app.view.shared.ui.Cursor;
import ru.mikroacse.rolespell.app.view.shared.ui.Cursor.Type;

/**
 * Created by MikroAcse on 01-May-17.
 */
public class GameStateProcessor extends StateProcessor {
    public GameStateProcessor(GameController controller) {
        super(controller);
    }

    @Override
    public void process() {
        InputAdapter input = InputAdapter.instance;
        GameRenderer renderer = getController().getRenderer();
        GameModel model = getController().getModel();

        if(model.getWorld() == null) {
            return;
        }

        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

        InputAdapter.Button mouseLeft = input.getButton(Input.Buttons.LEFT);
        InputAdapter.Button mouseRight = input.getButton(Input.Buttons.RIGHT);

        IntVector2 cell = renderer.getWorldRenderer().getMapRenderer().stageToCell(mouseX, mouseY);

        renderer.getWorldRenderer().setSelectorPosition(cell.x, cell.y);

        Entity entity = model.getWorld().getEntityAt(cell);

        Cursor.Type type = Type.POINTER;

        if (entity != null) {
            if (entity.getType() == EntityType.DROPPED_ITEM) {
                type = Cursor.Type.TAKE;
            } else if (entity.hasParameter(Entity.Parameter.VULNERABLE)) {
                if (entity.getType() != EntityType.PLAYER) {
                    type = Cursor.Type.ATTACK;
                }
            }
        }

        renderer.setCursor(type);

        if (mouseRight.justPressed) {
            model.tryRouteTo(cell.x, cell.y);
        }

        if (mouseLeft.justPressed) {
            model.stopAttacking();

            boolean route = true;
            // TODO: better solution
            if (entity != null) {
                if (entity.getParameters().contains(Entity.Parameter.VULNERABLE)) {
                    model.tryAttack(cell.x, cell.y);

                    route = false;
                } else if (entity.getType() == EntityType.DROPPED_ITEM) {
                    DroppedItem droppedItem = (DroppedItem) entity;

                    IntVector2 position = model.getControllable().getPosition();

                    // TODO: magic number (maximum pickup distance)
                    if (droppedItem.getItem().isPickable() && position.distance(entity.getPosition()) < 3) {
                        InventoryStateProcessor inventoryState = getController().getInventoryState();
                        ItemView droppedItemView = new ItemView(droppedItem.getItem());

                        getController().setState(GameRenderer.State.INVENTORY);

                        droppedItem.remove();

                        inventoryState.startDrag(droppedItemView);

                        route = false;
                    }
                }
            }

            if (route) {
                model.tryRouteTo(cell.x, cell.y);
            }
        }

        processHotbar();
    }

    private void processHotbar() {
        InputAdapter input = InputAdapter.instance;
        GameRenderer renderer = getController().getRenderer();
        GameModel model = getController().getModel();

        Inventory inventory = model.getControllable().getComponent(InventoryComponent.class).getInventory();

        ItemListView hotbarView = renderer.getHotbarView();
        ItemList hotbar = hotbarView.getItemList();

        int key = Input.Keys.NUM_1;
        for (int i = 0; i < hotbar.getSize(); i++) {
            if (input.getButton(key).justPressed) {
                inventory.setSelected(i);
                break;
            }

            key++;
        }

        if (hotbarView.getItemList() != null) {
            hotbarView.select(inventory.getSelected(), true);
        }
    }

    @Override
    public void pause() {
        super.pause();

        GameRenderer renderer = getController().getRenderer();
        renderer.setCursor(Cursor.Type.POINTER);
    }
}
