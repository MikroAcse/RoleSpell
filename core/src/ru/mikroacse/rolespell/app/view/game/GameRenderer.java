package ru.mikroacse.rolespell.app.view.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import ru.mikroacse.engine.util.Vector2;
import ru.mikroacse.rolespell.RoleSpell;
import ru.mikroacse.rolespell.media.AssetBundle;
import ru.mikroacse.rolespell.media.AssetManager;
import ru.mikroacse.rolespell.app.model.game.GameModel;
import ru.mikroacse.rolespell.app.model.game.entities.Npc;
import ru.mikroacse.rolespell.app.model.game.entities.Player;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.MovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.movement.PathMovementComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.parameters.HealthParameter;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.rolespell.app.model.game.world.World;
import ru.mikroacse.rolespell.app.view.game.ui.HotbarView;
import ru.mikroacse.rolespell.app.view.game.ui.InventoryView;
import ru.mikroacse.rolespell.app.view.game.ui.StatusHUD;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameRenderer {
    private GameModel model;
    
    private State state;
    
    private TiledMapRenderer renderer;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    
    private Texture waypoint;
    private Texture pathTexture;
    
    private StatusHUD statusHUD;
    private HotbarView hotbarView;
    private InventoryView inventoryView;
    
    private AssetBundle bundle;
    
    public GameRenderer(GameModel model) {
        this.model = model;
        
        bundle = RoleSpell.getAssetManager().getBundle(AssetManager.Bundle.GAME);
        
        renderer = new OrthogonalTiledMapRenderer(model.getWorld().getMap());
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        
        waypoint = bundle.getTexture("path/waypoint");
        pathTexture = bundle.getTexture("path/path");
        
        batch = new SpriteBatch();
        
        statusHUD = new StatusHUD();
        
        hotbarView = new HotbarView();
        inventoryView = new InventoryView();
        
        state = State.GAME;
        
        // TODO: zooming
    }
    
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if (model.getWorld() == null) {
            return;
        }
        
        World world = model.getWorld();
        Entity observable = model.getObservable();
        MovementComponent observableMovement = observable.getComponent(MovementComponent.class);
        
        Vector2 observablePosition = cellToMap(observableMovement.getPosition());
        
        com.badlogic.gdx.math.Vector2 cameraPos = new com.badlogic.gdx.math.Vector2(observablePosition.x,
                                                                                    observablePosition.y);
        
        cameraPos.x = Math.min(cameraPos.x, world.getRealWidth() - camera.viewportWidth / 2f);
        cameraPos.y = Math.min(cameraPos.y, world.getRealHeight() - camera.viewportHeight / 2f);
        
        cameraPos.x = Math.max(cameraPos.x, camera.viewportWidth / 2f);
        cameraPos.y = Math.max(cameraPos.y, camera.viewportHeight / 2f);
        
        // TODO: magic number, smoothing camera movement
        camera.position.x += (cameraPos.x - camera.position.x) / 4f;
        camera.position.y += (cameraPos.y - camera.position.y) / 4f;
        
        camera.update();
        
        renderer.setView(camera);
        renderer.render(new int[]{0, 1, 2, 3}); // TODO: magic
        
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        
        for (Entity entity : world.getEntities()) {
            MovementComponent movement = entity.getComponent(MovementComponent.class);
            
            Vector2 position = movement.getPosition();
            position = cellToMap(position);
            
            Texture texture = null;
            
            if (entity instanceof Npc) {
                texture = bundle.getTexture("entities/npc");
            }
            
            if (entity instanceof Player) {
                texture = bundle.getTexture("entities/player");
            }
            
            if (texture != null) {
                // TODO: normal damage animation and handling
                StatusComponent status = entity.getComponent(StatusComponent.class);
                HealthParameter health = status.getParameter(HealthParameter.class);
                
                if (System.currentTimeMillis() - health.getLastTimeDamaged() <= 400) {
                    batch.setColor(Color.RED);
                }
                
                batch.draw(texture, position.x, position.y);
                batch.setColor(Color.WHITE);
            }
        }
        
        // TODO: beautify
        if (observable.hasComponent(PathMovementComponent.class)) {
            LinkedList<Vector2> path = ((PathMovementComponent) observableMovement).getPath();
            
            if (!path.isEmpty()) {
                Iterator<Vector2> it = path.iterator();
                
                while (it.hasNext()) {
                    Vector2 position = it.next();
                    Vector2 mapPosition = cellToMap(position.x, position.y);
                    
                    if (it.hasNext()) {
                        batch.draw(pathTexture, mapPosition.x, mapPosition.y);
                    } else {
                        batch.draw(waypoint, mapPosition.x, mapPosition.y);
                    }
                }
            }
        }
        
        // TODO: doesn't work when uncommented
        //renderer.render(new int[]{4, 5}); // TODO: magic
        
        // TODO: magic numbers
        
        if (state == State.GAME) {
            statusHUD.draw(
                    batch,
                    observable.getComponent(StatusComponent.class),
                    camera.position.x - camera.viewportWidth / 2 + 5,
                    camera.position.y - camera.viewportHeight / 2 + 5);
        }
        
        if (observable.hasComponent(InventoryComponent.class)) {
            InventoryComponent inventory = observable.getComponent(InventoryComponent.class);
            
            if (state == State.GAME) {
                hotbarView.setInventory(inventory.getInventory());
                
                hotbarView.draw(
                        batch,
                        camera.position.x + camera.viewportWidth / 2 - hotbarView.getWidth() - 5,
                        camera.position.y - camera.viewportHeight / 2 + 5);
            }
            
            if (state == State.INVENTORY) {
                inventoryView.setInventory(inventory.getInventory());
                
                inventoryView.draw(
                        batch,
                        camera.position.x - camera.viewportWidth / 2 + 5,
                        camera.position.y - camera.viewportHeight / 2 + 5);
            }
        }
        
        batch.end();
    }
    
    public Vector2 globalToLocal(int x, int y) {
        y = (int) camera.viewportHeight - y;
        
        x += camera.position.x - camera.viewportWidth / 2f;
        y += camera.position.y - camera.viewportHeight / 2f;
        
        return new Vector2(x, y);
    }
    
    public Vector2 cellToMap(int x, int y) {
        return new Vector2(
                x * model.getWorld().getTileWidth(),
                y * model.getWorld().getTileHeight());
    }
    
    // TODO: bad method names
    
    public Vector2 cellToMap(Vector2 position) {
        return cellToMap(position.x, position.y);
    }
    
    public Vector2 mapToCell(int x, int y) {
        return new Vector2(
                x / model.getWorld().getTileWidth(),
                y / model.getWorld().getTileHeight());
    }
    
    public Vector2 mapToCell(Vector2 position) {
        return mapToCell(position.x, position.y);
    }
    
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }
    
    public StatusHUD getStatusHUD() {
        return statusHUD;
    }
    
    public HotbarView getHotbarView() {
        return hotbarView;
    }
    
    public InventoryView getInventoryView() {
        return inventoryView;
    }
    
    public State getState() {
        return state;
    }
    
    public void setState(State state) {
        this.state = state;
    }
    
    public enum State {
        GAME,
        INVENTORY,
        QUESTS
    }
}
