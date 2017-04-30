package ru.mikroacse.rolespell.app.controller.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ru.mikroacse.engine.util.Vector2;
import ru.mikroacse.rolespell.app.model.game.GameModel;
import ru.mikroacse.rolespell.app.model.game.entities.components.ai.BehaviorAi;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.rolespell.app.model.game.items.ItemStack;
import ru.mikroacse.rolespell.app.model.game.items.weapons.WoodenSword;
import ru.mikroacse.rolespell.app.model.game.world.World;
import ru.mikroacse.rolespell.app.view.game.GameRenderer;
import ru.mikroacse.engine.util.Priority;

import java.util.List;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameController {
    private GameRenderer renderer;
    private GameModel model;
    
    private GameInputAdapter input;
    
    public GameController(GameRenderer renderer, GameModel model) {
        this.renderer = renderer;
        this.model = model;
        
        input = new GameInputAdapter();
        Gdx.input.setInputProcessor(input);
    }
    
    public void update(float delta) {
        GameRenderer.State state = renderer.getState();
        
        GameInputAdapter.Button mouseLeft = input.getButton(Input.Buttons.LEFT);
        GameInputAdapter.Button mouseRight = input.getButton(Input.Buttons.RIGHT);
        GameInputAdapter.Button inventoryButton = input.getButton(Input.Keys.I);
        
        if (mouseLeft.justPressed) {
            if (state == GameRenderer.State.GAME) {
                routeAction(input.getMouseX(), input.getMouseY());
            }
            
            if (state == GameRenderer.State.INVENTORY) {
                renderer.getInventoryView()
                        .setDragItem(new ItemStack<WoodenSword>(WoodenSword.class, new WoodenSword()));
            }
        }
        
        if (mouseLeft.isDown) {
            if (state == GameRenderer.State.INVENTORY) {
                renderer.getInventoryView()
                        .getDragPosition()
                        .set(input.getMouseX(), Gdx.graphics.getHeight() - input.getMouseY());
            }
        }
        
        if (mouseRight.justPressed) {
            attackAction(input.getMouseX(), input.getMouseY());
        }
        
        if (inventoryButton.justPressed) {
            if (state == GameRenderer.State.GAME) {
                renderer.setState(GameRenderer.State.INVENTORY);
            } else {
                renderer.setState(GameRenderer.State.GAME);
            }
        }
        
        model.update(delta);
        input.update();
    }
    
    // TODO: these methods should be in loader
    
    private void attackAction(int x, int y) {
        Entity controllable = model.getControllable();
        World world = model.getWorld();
        PathMovementComponent movement = controllable.getComponent(PathMovementComponent.class);
        
        Vector2 coordinates = renderer.globalToLocal(x, y);
        Vector2 cell = world.getCellPosition(coordinates.x, coordinates.y);
        
        if (world.isValidPosition(cell)) {
            List<Entity> entities = world.getEntitiesAt(cell);
            
            BehaviorAi behaviorAi = controllable.getComponent(BehaviorAi.class);
            
            if (entities.isEmpty()) {
                behaviorAi.clearTargets();
            } else {
                behaviorAi.setTarget(entities.get(0));
            }
        }
    }
    
    private void routeAction(int x, int y) {
        // TODO: change player to 'controllable'
        Entity controllable = model.getControllable();
        World world = model.getWorld();
        PathMovementComponent movement = controllable.getComponent(PathMovementComponent.class);
        
        Vector2 coordinates = renderer.globalToLocal(x, y);
        Vector2 cell = world.getCellPosition(coordinates.x, coordinates.y);
        
        if (world.isValidPosition(cell)) {
            // TODO: bad and magic
            // looking for the nearest passable cells
            List<Vector2> passableCells = world.getPassableCells(
                    cell.x,
                    cell.y,
                    false,
                    0,
                    5,
                    false);
            
            // checking passable cells for available paths
            boolean routeFound = tryRouteTo(passableCells, Priority.HIGH);
            
            // no path found to any of nearest cells
            if (!routeFound) {
                System.out.println("Path not found!");
            }
        }
    }
    
    public boolean tryRouteTo(Vector2 destination, Priority priority) {
        Entity entity = model.getControllable();
        PathMovementComponent movement = entity.getComponent(PathMovementComponent.class);
        
        // TODO: magic numbers
        return movement.tryRouteTo(destination, priority, 10, 15);
    }
    
    public boolean tryRouteTo(List<Vector2> destinations, Priority priority) {
        Entity entity = model.getControllable();
        PathMovementComponent movement = entity.getComponent(PathMovementComponent.class);
        
        // TODO: magic numbers
        return movement.tryRouteTo(destinations, priority, 10, 15);
    }
}
