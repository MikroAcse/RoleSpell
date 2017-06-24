package ru.mikroacse.rolespell.app.view.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import ru.mikroacse.rolespell.app.model.game.GameModel;
import ru.mikroacse.rolespell.app.model.game.entities.Entity;
import ru.mikroacse.rolespell.app.model.game.entities.components.inventory.InventoryComponent;
import ru.mikroacse.rolespell.app.model.game.entities.components.status.StatusComponent;
import ru.mikroacse.rolespell.app.model.game.inventory.Inventory;
import ru.mikroacse.rolespell.app.view.Renderer;
import ru.mikroacse.rolespell.app.view.game.inventory.ItemListView;
import ru.mikroacse.rolespell.app.view.game.items.ItemView;
import ru.mikroacse.rolespell.app.view.game.quests.QuestsView;
import ru.mikroacse.rolespell.app.view.game.status.StatusView;
import ru.mikroacse.rolespell.app.view.game.world.MapRenderer;
import ru.mikroacse.rolespell.app.view.game.world.WorldRenderer;
import ru.mikroacse.rolespell.app.view.shared.ui.Cursor;

/**
 * Created by MikroAcse on 22.03.2017.
 */
public class GameRenderer extends Renderer {
    private GameModel gameModel;

    private State state;

    private WorldRenderer worldRenderer;

    private StatusView statusView;

    private ItemListView inventoryView;
    private ItemListView hotbarView;
    private ItemView dragItem;

    private QuestsView questsView;

    private Cursor cursor;

    public GameRenderer(GameModel gameModel) {
        super();

        this.gameModel = gameModel;

        cursor = new Cursor();

        worldRenderer = new WorldRenderer();
        worldRenderer.setZoom(2f);

        inventoryView = new ItemListView(6);
        hotbarView = new ItemListView(3);
        statusView = new StatusView();

        questsView = new QuestsView();

        addActor(questsView);
        addActor(inventoryView);
        addActor(hotbarView);
        addActor(statusView);
        addActor(cursor);

        refreshWorld();
    }

    @Override
    public void draw() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        MapRenderer mapRenderer = worldRenderer.getMapRenderer();
        Batch batch = getBatch();

        // TODO: top layer above entities and path, but under entity names

        worldRenderer.moveCamera();

        mapRenderer.drawBottomLayers();

        batch.begin();
        worldRenderer.draw(batch, 1f);
        batch.end();

        mapRenderer.drawTopLayers();

        cursor.toFront();

        super.draw();
    }

    // TODO: beautify
    public void refreshWorld() {
        if (gameModel.getWorld() == null) {
            statusView.setVisible(false);
            inventoryView.setVisible(false);
            hotbarView.setVisible(false);

            worldRenderer.setWorld(null);
            return;
        }

        worldRenderer.setWorld(gameModel.getWorld());

        worldRenderer.setObservable(gameModel.getObservable());

        Entity controllable = gameModel.getControllable();

        // TODO: update on observable change
        if (controllable.hasComponent(InventoryComponent.class)) {
            Inventory inventory = controllable.getComponent(InventoryComponent.class).getInventory();

            inventoryView.setItemList(inventory.getItems());

            hotbarView.setItemList(inventory.getHotbar());
        }

        if (controllable.hasComponent(StatusComponent.class)) {
            statusView.setStatus(controllable.getComponent(StatusComponent.class));
        }

        worldRenderer.setObservable(gameModel.getObservable());

        resize(getViewport().getScreenWidth(), getViewport().getScreenHeight());

        setState(State.GAME);
    }

    public void update() {
        if (inventoryView != null) {
            inventoryView.setPosition(5f, 5f);
        }

        if (hotbarView != null) {
            hotbarView.setPosition(getWidth() - hotbarView.getRealWidth() - 5f, 5f);
        }

        if (statusView != null) {
            statusView.setPosition(5f, 5f);
        }

        questsView.setSize(getWidth(), getHeight());
        questsView.update();
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
        worldRenderer.resizeViewport(width, height);

        update();
    }

    public void setCursorVisibility(boolean visible) {
        cursor.setVisible(visible);
    }

    public void setCursor(Cursor.Type type) {
        this.cursor.setType(type);
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

    public QuestsView getQuestsView() {
        return questsView;
    }

    public State getState() {
        return state;
    }

    public boolean setState(State state) {
        if (this.state == state) {
            return false;
        }

        this.state = state;

        worldRenderer.setSelectorVisible(false);
        statusView.setVisible(false);
        if (inventoryView != null) {
            inventoryView.setVisible(false);
        }
        if (hotbarView != null) {
            hotbarView.setVisible(false);
        }
        questsView.setVisible(false);

        setDragItem(null);

        switch (state) {
            case GAME:
                worldRenderer.setSelectorVisible(true);
                statusView.setVisible(true);
                if (hotbarView != null) {
                    hotbarView.setVisible(true);
                }
                break;
            case INVENTORY:
                inventoryView.setVisible(true);
                hotbarView.setVisible(true);
                break;
            case QUESTS:
                questsView.setVisible(true);
                break;
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
