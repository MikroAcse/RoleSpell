package ru.mikroacse.rolespell.app.controller.game.states;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.app.controller.game.GameController;
import ru.mikroacse.rolespell.app.controller.game.InputAdapter;
import ru.mikroacse.rolespell.app.model.game.GameModel;
import ru.mikroacse.rolespell.app.model.game.entities.DroppedItem;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.app.model.game.items.weapons.WoodenSword;
import ru.mikroacse.rolespell.app.model.game.world.World;
import ru.mikroacse.rolespell.app.view.game.GameRenderer;
import ru.mikroacse.rolespell.app.view.game.items.ItemView;
import ru.mikroacse.rolespell.app.view.game.inventory.ItemListView;

/**
 * Created by MikroAcse on 01-May-17.
 */
public class InventoryStateProcessor extends StateProcessor {
    private Vector2 dragOffset;
    
    public InventoryStateProcessor(GameController controller) {
        super(controller);
        
        dragOffset = new Vector2();
    }
    
    public void process() {
        InputAdapter input = getController().getInput();
        GameRenderer renderer = getController().getRenderer();
        GameModel model = getController().getModel();
        
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        Vector2 mouse = new Vector2(mouseX, mouseY);
    
        Entity controllable = model.getControllable();
        Inventory inventory = controllable.getComponent(InventoryComponent.class).getInventory();
    
        ItemListView inventoryView = renderer.getInventoryView();
        ItemListView hotbarView = renderer.getHotbarView();
    
        Vector2 inventoryMouse = inventoryView.stageToLocalCoordinates(mouse.cpy());
        Vector2 hotbarMouse = hotbarView.stageToLocalCoordinates(mouse.cpy());
    
        int inventoryCell = inventoryView.getCellIndex(inventoryMouse);
        int hotbarCell = hotbarView.getCellIndex(hotbarMouse);
    
        InputAdapter.Button mouseLeft = input.getButton(Input.Buttons.LEFT);
    
        // TODO: remove
        InputAdapter.Button test = input.getButton(Input.Keys.E);
    
        if(test.justPressed) {
            inventory.getItems().addItem(new WoodenSword());
        }
    
        // drag started
        if (mouseLeft.justPressed) {
            if(inventoryCell != -1) {
                ItemView item = inventoryView.getItemView(inventoryCell);
            
                if(item != null) {
                    renderer.setDragItem(item);
                    
                    Vector2 itemCoordinates = new Vector2(item.getX(), item.getY());
                    inventoryView.localToStageCoordinates(itemCoordinates);
                    
                    dragOffset.x = itemCoordinates.x - mouseX;
                    dragOffset.y = itemCoordinates.y - mouseY;
                }
            }
        }
    
        // dragging
        if (mouseLeft.isDown && renderer.getDragItem() != null) {
            float dragX = mouseX + dragOffset.x;
            float dragY = mouseY + dragOffset.y;
        
            renderer.getDragItem().setPosition(dragX, dragY);
        }
    
        // drag ended
        if(mouseLeft.justReleased && renderer.getDragItem() != null) {
            Item item = renderer.getDragItem().getItem();
        
            if(inventoryCell != -1) {
                inventory.getItems().setItem(inventoryCell, item);
            
                inventoryView.retouchItemList();
            } else if (hotbarCell != -1) {
                inventory.getHotbar().setItem(hotbarCell, item);
            
                hotbarView.retouchItemList();
            } else {
                // TODO: looks bad
                IntVector2 cell = renderer.mapToCell(renderer.stageToMap(mouseX, mouseY));
                
                MovementComponent movement = controllable.getComponent(MovementComponent.class);
                IntVector2 position = movement.getPosition();
                
                // TODO: magic number (maximum drop distance)
                cell.shorten(position, 2);
                
                World world = model.getWorld();
                DroppedItem<Item> droppedItem = new DroppedItem<>(item, world);
                droppedItem.getComponent(MovementComponent.class)
                           .getPosition()
                           .set(cell.x, cell.y);
                
                world.addEntity(droppedItem);
                
                inventory.removeItem(item);
            }
        
            renderer.setDragItem(null);
        }
    }
}
