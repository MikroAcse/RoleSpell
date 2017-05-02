package ru.mikroacse.rolespell.app.view.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ru.mikroacse.engine.util.IntVector2;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.app.model.game.entities.DroppedItem;
import ru.mikroacse.rolespell.app.model.game.entities.EntityType;
import ru.mikroacse.rolespell.app.model.game.entities.Npc;
import ru.mikroacse.rolespell.app.model.game.entities.Player;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.HealthParameter;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.view.game.items.ItemView;
import ru.mikroacse.rolespell.app.view.game.inventory.ItemListView;
import ru.mikroacse.rolespell.app.view.game.status.StatusView;
import ru.mikroacse.rolespell.media.AssetBundle;
import ru.mikroacse.rolespell.media.AssetManager;
import ru.mikroacse.rolespell.app.model.game.GameModel;
import ru.mikroacse.rolespell.app.model.game.world.World;

import java.util.Iterator;
import java.util.List;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameRenderer extends Stage {
    private GameModel gameModel;
    
    private State state;
    
    private AssetBundle bundle;
    
    private TiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    
    private StatusView statusView;
    
    private ItemListView inventoryView;
    private ItemListView hotbarView;
    
    private Texture waypoint;
    private Texture pathTexture;
    
    private ItemView dragItem;
    
    public GameRenderer(GameModel gameModel) {
        super(new ScreenViewport());
        
        this.gameModel = gameModel;
        
        bundle = RoleSpell.getAssetManager().getBundle(AssetManager.Bundle.GAME);
    
        mapRenderer = new OrthogonalTiledMapRenderer(gameModel.getWorld().getMap());
        
        camera = new OrthographicCamera();
    
        waypoint = bundle.getTexture("path/waypoint");
        pathTexture = bundle.getTexture("path/path");
        
        Inventory inventory = gameModel.getObservable().getComponent(InventoryComponent.class).getInventory();
    
        inventoryView = new ItemListView(6);
        inventoryView.setItemList(inventory.getItems());
        addActor(inventoryView);
    
        hotbarView = new ItemListView(3);
        hotbarView.setItemList(inventory.getHotbar());
        addActor(hotbarView);
        
        statusView = new StatusView();
        statusView.setStatus(gameModel.getObservable().getComponent(StatusComponent.class));
        addActor(statusView);
        
        setState(State.GAME);
    }
    
    @Override
    public void draw() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    
        if (gameModel.getWorld() == null) {
            return;
        }
    
        World world = gameModel.getWorld();
        Entity observable = gameModel.getObservable();
        MovementComponent observableMovement = observable.getComponent(MovementComponent.class);
    
        Vector2 observablePosition = cellToMap(observableMovement.getPosition());
        
        Vector2 cameraPosition = observablePosition.cpy();
        
        cameraPosition.x = Math.max(cameraPosition.x, getWidth() / 2);
        cameraPosition.y = Math.max(cameraPosition.y, getHeight() / 2);
    
        cameraPosition.x = Math.min(cameraPosition.x, world.getRealWidth() - getWidth() / 2);
        cameraPosition.y = Math.min(cameraPosition.y, world.getRealHeight() - getHeight() / 2);
        
        // TODO: magic number, smoothing camera movement
        camera.position.x += (cameraPosition.x - camera.position.x) / 4f;
        camera.position.y += (cameraPosition.y - camera.position.y) / 4f;
        
        camera.update();
    
        mapRenderer.setView(camera);
        mapRenderer.render(new int[]{0, 1, 2, 3}); // TODO: magic
        
        getBatch().begin();
        getBatch().setProjectionMatrix(camera.combined);
        
        for (Entity entity : world.getEntities()) {
            MovementComponent movement = entity.getComponent(MovementComponent.class);
        
            IntVector2 position = movement.getPosition();
            Vector2 mapPosition = cellToMap(position);
        
            Texture texture = null;
            
            switch (entity.getType()) {
                case NPC:
                    texture = bundle.getTexture("entities/npc");
                    break;
                case PLAYER:
                    texture = bundle.getTexture("entities/player");
                    break;
                case DROPPED_ITEM:
                    texture = bundle.getTexture("items/weapons/wooden-sword");
                    break;
            }
            
            if (texture != null) {
                // TODO: normal damage animation and handling
                StatusComponent status = entity.getComponent(StatusComponent.class);
                
                if (status != null) {
                    HealthParameter health = status.getParameter(HealthParameter.class);
    
                    if (System.currentTimeMillis() - health.getLastTimeDamaged() <= 400) {
                        getBatch().setColor(Color.RED);
                    }
                }
    
                getBatch().draw(texture, mapPosition.x, mapPosition.y);
                getBatch().setColor(Color.WHITE);
            }
        }
    
        // TODO: beautify
        if (observable.hasComponent(PathMovementComponent.class)) {
            List<IntVector2> path = ((PathMovementComponent) observableMovement).getPath();
        
            if (!path.isEmpty()) {
                Iterator<IntVector2> it = path.iterator();
            
                while (it.hasNext()) {
                    IntVector2 position = it.next();
                    Vector2 mapPosition = cellToMap(position.x, position.y);
                
                    if (it.hasNext()) {
                        getBatch().draw(pathTexture, mapPosition.x, mapPosition.y);
                    } else {
                        getBatch().draw(waypoint, mapPosition.x, mapPosition.y);
                    }
                }
            }
        }
    
        // TODO: doesn't work when uncommented
        //renderer.render(new int[]{4, 5}); // TODO: magic
        
        getBatch().end();
        
        super.draw();
    }
    
    public void update() {
        camera.setToOrtho(false, getWidth(), getHeight());
        
        inventoryView.setPosition(5f, 5f);
        
        hotbarView.setPosition(getWidth() - hotbarView.getRealWidth() - 5f, 5f);
        
        statusView.setPosition(5f, 5f);
    }
    
    public Vector2 stageToMap(float x, float y) {
        x += camera.position.x - camera.viewportWidth / 2f;
        y += camera.position.y - camera.viewportHeight / 2f;
        
        return new Vector2(x, y);
    }
    
    public Vector2 cellToMap(int x, int y) {
        return new Vector2(
                x * gameModel.getWorld().getTileWidth(),
                y * gameModel.getWorld().getTileHeight());
    }
    
    // TODO: bad method names
    
    public Vector2 cellToMap(IntVector2 position) {
        return cellToMap(position.x, position.y);
    }
    
    public IntVector2 mapToCell(float x, float y) {
        return new IntVector2(
                (int) (x / gameModel.getWorld().getTileWidth()),
                (int) (y / gameModel.getWorld().getTileHeight())
        );
    }
    
    public IntVector2 mapToCell(Vector2 position) {
        return mapToCell(position.x, position.y);
    }
    
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }
    
    public StatusView getStatusView() {
        return statusView;
    }
    
    public ItemListView getInventoryView() {
        return inventoryView;
    }
    
    public ItemListView getHotbarView() {
        return hotbarView;
    }
    
    public State getState() {
        return state;
    }
    
    public void setState(State state) {
        this.state = state;
        
        if(state == State.INVENTORY) {
            statusView.setVisible(false);
            inventoryView.setVisible(true);
        }
        
        if(state == State.GAME) {
            setDragItem(null);
            
            statusView.setVisible(true);
            inventoryView.setVisible(false);
        }
    }
    
    public ItemView getDragItem() {
        return dragItem;
    }
    
    public void setDragItem(ItemView dragItem) {
        if(this.dragItem != null) {
            this.dragItem.remove();
            inventoryView.update();
        }
        
        this.dragItem = dragItem;
        
        if(dragItem != null) {
            addActor(dragItem);
        }
    }
    
    public enum State {
        GAME,
        INVENTORY,
        QUESTS
    }
}
