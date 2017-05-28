package ru.mikroacse.rolespell.app.model.game.inventory;

import ru.mikroacse.rolespell.app.model.game.items.Item;

/**
 * Created by MikroAcse on 06-May-17.
 */
public class ItemListListener implements ItemList.Listener {
    @Override
    public void itemAdded(ItemList itemList, int index) {
        updated(itemList);

        itemSet(itemList, index, itemList.getItem(index));
    }

    @Override
    public void itemRemoved(ItemList itemList, Item item, int index) {
        updated(itemList);

        itemSet(itemList, index, null);
    }

    @Override
    public void itemMoved(ItemList itemList, int index, int prevIndex) {
        updated(itemList);

        itemSet(itemList, prevIndex, null);
        itemSet(itemList, index, itemList.getItem(index));
    }

    @Override
    public void itemsSwapped(ItemList itemList, int index1, int index2) {
        updated(itemList);
    }

    public void itemSet(ItemList itemList, int index, Item item) {

    }

    @Override
    public void sizeChanged(ItemList itemList, int size) {
        updated(itemList);
    }

    public void updated(ItemList itemList) {

    }
}

