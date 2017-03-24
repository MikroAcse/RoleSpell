package com.quillraven.quillyscastle.inventory;

import com.quillraven.quillyscastle.inventory.Item.ItemType;

/**
 * This class is used to store inventory information within the save file. It is used for
 * basic JSON read/write operations.
 */
public class ItemLocation {
    // inventory slot location index
    private int locationIndex;
    // item type of inventory slot
    private ItemType type;
    // number of items of inventory slot
    private int numItems;

    public ItemLocation() {
        locationIndex = -1;
        type = null;
        numItems = 0;
    }

    public ItemLocation(int locationIndex, ItemType type, int numItems) {
        this.locationIndex = locationIndex;
        this.type = type;
        this.numItems = numItems;
    }

    public int getLocationIndex() {
        return locationIndex;
    }

    public void setLocationIndex(int locationIndex) {
        this.locationIndex = locationIndex;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public int getNumItems() {
        return numItems;
    }

    public void setNumItems(int numItems) {
        this.numItems = numItems;
    }
}