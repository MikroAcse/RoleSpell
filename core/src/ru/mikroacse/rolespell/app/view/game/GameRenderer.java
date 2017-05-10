package ru.mikroacse.rolespell.app.view.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ru.mikroacse.rolespell.app.model.game.GameModel;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.view.game.inventory.ItemListView;
import ru.mikroacse.rolespell.app.view.game.items.ItemView;
import ru.mikroacse.rolespell.app.view.game.status.StatusView;
import ru.mikroacse.rolespell.app.view.game.ui.GameCursor;
import ru.mikroacse.rolespell.app.view.game.world.MapRenderer;
import ru.mikroacse.rolespell.app.view.game.world.WorldRenderer;

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

    private GameCursor cursor;

    public GameRenderer(GameModel gameModel) {
        super(new ScreenViewport());

        this.gameModel = gameModel;

        cursor = new GameCursor();

        Inventory inventory = gameModel.getObservable().getComponent(InventoryComponent.class).getInventory();

        inventoryView = new ItemListView(6);
        inventoryView.setItemList(inventory.getItems());

        hotbarView = new ItemListView(3);
        hotbarView.setItemList(inventory.getHotbar());

        statusView = new StatusView();
        statusView.setStatus(gameModel.getObservable().getComponent(StatusComponent.class));

        worldRenderer = new WorldRenderer(gameModel.getWorld());
        worldRenderer.setZoom(2f);

        worldRenderer.setObservable(gameModel.getObservable());

        //addActor(worldRenderer);
        addActor(inventoryView);
        addActor(hotbarView);
        addActor(statusView);
        addActor(cursor);
    }

    @Override
    public void draw() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        MapRenderer mapRenderer = worldRenderer.getMapRenderer();

        // TODO: top layer above entities and path, but under entity names

        worldRenderer.updateCamera(gameModel.getObservable());
        mapRenderer.drawBottomLayers();

        getBatch().begin();
        worldRenderer.draw(getBatch(), 1f);
        getBatch().end();

        mapRenderer.drawTopLayers();


        cursor.toFront();

        super.draw();
    }

    public void update() {
        inventoryView.setPosition(5f, 5f);

        hotbarView.setPosition(getWidth() - hotbarView.getRealWidth() - 5f, 5f);

        statusView.setPosition(5f, 5f);
    }


    public void resize(int width, int height) {
        getViewport().update(width, height, true);
        worldRenderer.resizeViewport(width, height);

        update();
    }

    public void setCursorVisibility(boolean visible) {
        cursor.setVisible(visible);
    }

    public void setCursor(GameCursor.Cursor cursor) {
        this.cursor.setCursor(cursor);
    }

    public void setCursorPosition(int x, int y) {
        float cursorWidth = cursor.getWidth() * cursor.getScaleX();
        float cursorHeight = cursor.getHeight() * cursor.getScaleY();

        cursor.setX((int) (x - cursorWidth / 2));
        cursor.setY((int) (y - cursorHeight / 2));
    }

    public void setDragPosition(float x, float y) {
        dragItem.setX(x - dragItem.getWidth() * dragItem.getScaleX() / 2);
        dragItem.setY(y - dragItem.getHeight() * dragItem.getScaleY() / 2);
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

    public boolean setState(State state) {
        if(this.state == state) {
            return false;
        }

        this.state = state;

        //worldRenderer.setSelectorVisible(false);
        statusView.setVisible(false);
        inventoryView.setVisible(false);
        hotbarView.setVisible(false);
        setDragItem(null);

        if (state == State.INVENTORY) {
            inventoryView.setVisible(true);
            hotbarView.setVisible(true);
        }

        if (state == State.GAME) {
            //worldRenderer.setSelectorVisible(true);
            statusView.setVisible(true);
            hotbarView.setVisible(true);
        }

        if (state == State.QUESTS) {

        }

        return true;
    }

    public ItemView getDragItem() {
        return dragItem;
    }

    public void setDragItem(ItemView dragItem) {
        if (this.dragItem != null) {
            this.dragItem.remove();
            inventoryView.update();
        }

        this.dragItem = dragItem;

        if (dragItem != null) {
            addActor(dragItem);

            dragItem.setScale(2); // TODO: magic number
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
