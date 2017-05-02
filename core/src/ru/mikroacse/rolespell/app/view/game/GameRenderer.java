package ru.mikroacse.rolespell.app.view.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.entities.core.Entity;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.view.game.items.ItemView;
import ru.mikroacse.rolespell.app.view.game.inventory.ItemListView;
import ru.mikroacse.rolespell.app.view.game.status.StatusView;
import ru.mikroacse.rolespell.app.view.game.world.WorldRenderer;
import ru.mikroacse.rolespell.app.model.game.GameModel;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameRenderer extends Stage {
    private GameModel gameModel;
    
    private State state;
    
    private WorldRenderer worldRenderer;
    
    private StatusView statusView;
    
    private ItemListView inventoryView;
    private ItemListView hotbarView;
    private ItemView dragItem;
    
    public GameRenderer(GameModel gameModel) {
        super(new ScreenViewport());
        
        this.gameModel = gameModel;
        
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
    
        worldRenderer = new WorldRenderer(gameModel.getWorld());
        
        setState(State.GAME);
    }
    
    @Override
    public void draw() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        worldRenderer.draw(gameModel.getObservable(), getBatch());
        
        super.draw();
    }
    
    public void update() {
        inventoryView.setPosition(5f, 5f);
        
        hotbarView.setPosition(getWidth() - hotbarView.getRealWidth() - 5f, 5f);
        
        statusView.setPosition(5f, 5f);
    }
    
    
    public void resize(int width, int height) {
        getViewport().update(width, height, true);
        worldRenderer.resize(width, height);
        
        update();
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
    
    public WorldRenderer getWorldRenderer() {
        return worldRenderer;
    }
    
    public enum State {
        GAME,
        INVENTORY,
        QUESTS
    }
}
