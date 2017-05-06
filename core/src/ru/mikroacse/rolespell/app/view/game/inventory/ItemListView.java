package ru.mikroacse.rolespell.app.view.game.inventory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import ru.mikroacse.engine.actors.RealActor;
import ru.mikroacse.engine.util.GroupUtil;
import ru.mikroacse.rolespell.app.model.game.inventory.ItemList;
import ru.mikroacse.rolespell.app.model.game.items.Item;
import ru.mikroacse.rolespell.app.view.game.items.ItemView;

/**
 * Created by MikroAcse on 30-Apr-17.
 */
public class ItemListView extends Group implements RealActor {
    private static final int CELL_OFFSET = 5;
    private static final int CELL_WIDTH = 64;
    private static final int CELL_HEIGHT = 64;

    private ItemView[] itemViews;
    private InventoryCell[] cells;

    private ItemList itemList;

    private int maxWidth;
    private int maxHeight;

    private ItemList.Listener itemListListener;

    public ItemListView(int maxWidth, int maxHeight) {
        super();

        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;

        // TODO: bad
        itemListListener = new ItemList.Listener() {
            @Override
            public void itemSet(ItemList itemList, int index, Item item, Item prev) {
                retouchItemList();
            }

            @Override
            public void sizeChanged(ItemList itemList, int size) {
                retouchItemList();
            }
        };
    }

    public ItemListView(int maxSize, boolean vertical) {
        this(vertical ? Integer.MAX_VALUE : maxSize,
                vertical ? maxSize : Integer.MAX_VALUE);
    }

    public ItemListView(int maxWidth) {
        this(maxWidth, false);
    }

    public void update() {
        if (itemList == null) {
            return;
        }

        int x = 0;
        int y = 0;

        for (int i = 0; i < itemList.getSize(); i++) {
            ItemView itemView = itemViews[i];
            InventoryCell cell = cells[i];

            float cellX = x * (CELL_WIDTH + CELL_OFFSET);
            float cellY = y * (CELL_HEIGHT + CELL_OFFSET);

            x++;
            if (x >= maxWidth) {
                x = 0;
                y++;

                if (y >= maxHeight) {
                    // other items are cut
                    break;
                }
            }

            cell.setSize(CELL_WIDTH, CELL_HEIGHT);
            cell.setPosition(cellX, cellY);

            if (itemView != null) {
                itemView.setSize(CELL_WIDTH, CELL_HEIGHT);
                itemView.setPosition(cellX, cellY);

                addActor(itemView);
            }
        }
    }

    // TODO: this is bad
    public void retouchItemList() {
        detachItemList(itemList);
        attachItemList(itemList);
    }

    private void attachItemList(ItemList itemList) {
        itemList.addListener(itemListListener);

        itemViews = new ItemView[itemList.getSize()];
        cells = new InventoryCell[itemList.getSize()];

        for (int i = 0; i < itemList.getSize(); i++) {
            Item item = itemList.getItem(i);

            cells[i] = new InventoryCell();
            addActor(cells[i]);

            if (item != null) {
                itemViews[i] = new ItemView(item);

                addActor(itemViews[i]);
            }
        }

        update();
    }

    private void detachItemList(ItemList itemList) {
        clearChildren();

        itemViews = null;
        cells = null;

        itemList.removeListener(itemListListener);
    }

    public ItemList getItemList() {
        return itemList;
    }

    public void setItemList(ItemList itemList) {
        if (this.itemList != null) {
            detachItemList(this.itemList);
        }

        this.itemList = itemList;

        if (itemList != null) {
            attachItemList(itemList);
        }
    }

    public ItemView getItemView(int index) {
        return itemViews[index];
    }

    public int getCellIndex(Vector2 position) {
        return getCellIndex(position.x, position.y);
    }

    public int getCellIndex(float x, float y) {
        for (int i = 0; i < cells.length; i++) {
            InventoryCell cell = cells[i];

            if (x >= cell.getX() && x <= cell.getX() + cell.getWidth()
                    && y >= cell.getY() && y < +cell.getY() + cell.getHeight()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public float getRealWidth() {
        return GroupUtil.getWidth(this);
    }

    @Override
    public float getRealHeight() {
        return GroupUtil.getHeight(this);
    }
}
