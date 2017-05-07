package ru.mikroacse.rolespell.app.controller.game.states;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.controller.game.GameController;
import ru.mikroacse.rolespell.app.controller.game.InputAdapter;
import ru.mikroacse.rolespell.app.model.game.GameModel;
import ru.mikroacse.rolespell.app.model.game.entities.DroppedItem;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.model.game.inventory.ItemList;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.app.model.game.items.weapons.WoodenSword;
import ru.mikroacse.rolespell.app.model.game.world.World;
import ru.mikroacse.rolespell.app.view.game.GameRenderer;
import ru.mikroacse.rolespell.app.view.game.inventory.ItemListView;
import ru.mikroacse.rolespell.app.view.game.items.ItemView;
import ru.mikroacse.rolespell.app.view.game.ui.GameCursor;

/**
 * Created by MikroAcse on 01-May-17.
 */
public class InventoryStateProcessor extends StateProcessor {
    public InventoryStateProcessor(GameController controller) {
        super(controller);
    }

    public void process(GameRenderer.State state) {
        if (state != GameRenderer.State.INVENTORY) {
            return;
        }

        InputAdapter input = getController().getInput();
        GameRenderer renderer = getController().getRenderer();
        GameModel model = getController().getModel();

        InputAdapter.Button mouseLeft = input.getButton(Input.Buttons.LEFT);

        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        Vector2 mouse = new Vector2(mouseX, mouseY);

        Entity controllable = model.getControllable();
        Inventory inventory = controllable.getComponent(InventoryComponent.class).getInventory();
        ItemList inventoryItems = inventory.getItems();
        ItemList hotbarItems = inventory.getHotbar();

        ItemListView inventoryView = renderer.getInventoryView();
        ItemListView hotbarView = renderer.getHotbarView();

        Vector2 inventoryMouse = inventoryView.stageToLocalCoordinates(mouse.cpy());
        Vector2 hotbarMouse = hotbarView.stageToLocalCoordinates(mouse.cpy());

        int inventoryCell = inventoryView.getCellIndex(inventoryMouse);
        int hotbarCell = hotbarView.getCellIndex(hotbarMouse);

        ItemView itemView = null;
        if (inventoryCell != -1) {
            itemView = inventoryView.getItemView(inventoryCell);
        }

        if (renderer.getDragItem() != null) {
            renderer.setCursor(GameCursor.Type.DRAG);
        } else if (itemView != null) {
            renderer.setCursor(GameCursor.Type.TAKE);
        } else {
            renderer.setCursor(GameCursor.Type.POINTER);
        }

        // TODO: remove (test, adds an item to the inventory)
        if (input.getButton(Input.Keys.E).justPressed) {
            inventory.getItems().addItem(new WoodenSword());
        }

        // drag started
        if (mouseLeft.justPressed && itemView != null) {
            startDrag(itemView);
        }

        // dragging
        if (mouseLeft.isDown && renderer.getDragItem() != null) {
            updateDrag();
        }

        // drag ended
        if (mouseLeft.justReleased && renderer.getDragItem() != null) {
            Item dragItem = renderer.getDragItem().getItem();

            // item moved from non-inventory location (i.e. map)
            if (!inventory.getItems().hasItem(dragItem)) {
                // only if item moved on empty cell in inventory
                if (inventoryCell == -1 || itemView != null) {
                    drop(dragItem);

                    renderer.setDragItem(null);
                    renderer.setState(GameRenderer.State.GAME);
                    return;
                }
            }

            // if moved on cell in inventory
            if (inventoryCell != -1) {
                if (itemView != null) {
                    inventoryItems.swapItems(itemView.getItem(), dragItem);
                }

                inventoryItems.setItem(inventoryCell, dragItem);
            }
            // if moved on cell in hotbar
            else if (hotbarCell != -1) {
                hotbarItems.setItem(hotbarCell, dragItem);
            }
            // moved outside inventory (dropped)
            else {
                drop(dragItem);
            }

            renderer.setDragItem(null);
        }
    }

    public void drop(Item item) {
        GameModel model = getController().getModel();
        InputAdapter input = getController().getInput();

        GameRenderer renderer = getController().getRenderer();

        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

        IntVector2 cell = renderer.getWorldRenderer().stageToCell(mouseX, mouseY);

        drop(item, cell.x, cell.y);
    }

    // TODO: move to model
    public void drop(Item item, int x, int y) {
        GameModel model = getController().getModel();
        World world = model.getWorld();
        Entity controllable = model.getControllable();

        MovementComponent movement = controllable.getComponent(MovementComponent.class);

        IntVector2 cell = new IntVector2(x, y);
        // TODO: magic number (maximum drop distance, why is this != max pickup distance?)
        cell.shorten(movement.getPosition(), 2);

        DroppedItem<Item> droppedItem = new DroppedItem<>(item, world);
        world.addEntity(droppedItem);

        droppedItem
                .getComponent(MovementComponent.class)
                .getPosition()
                .set(cell.x, cell.y);

        Inventory inventory = controllable.getComponent(InventoryComponent.class).getInventory();

        ItemList inventoryItems = inventory.getItems();
        inventoryItems.removeItem(item);
    }

    private void updateDrag() {
        InputAdapter input = getController().getInput();
        GameRenderer renderer = getController().getRenderer();

        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

        renderer.setDragPosition(mouseX, mouseY);
    }

    public void startDrag(ItemView itemView) {
        GameRenderer renderer = getController().getRenderer();

        renderer.setDragItem(itemView);

        updateDrag();
    }
}
