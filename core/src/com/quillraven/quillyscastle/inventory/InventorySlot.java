package com.quillraven.quillyscastle.inventory;

import com.badlogic.gdx.Gdx;
import com.quillraven.quillyscastle.inventory.Item.ItemUseType;

import java.util.EnumSet;

public class InventorySlot {
    private static final String TAG = InventorySlot.class.getSimpleName();

    private EnumSet<ItemUseType> allowedItemUseTypes;
    private int numItems;
    private Item item;

    public InventorySlot() {
        this(EnumSet.allOf(ItemUseType.class));
    }

    public InventorySlot(ItemUseType... itemUseTypes) {
        this(EnumSet.of(itemUseTypes[0], itemUseTypes));
    }

    private InventorySlot(EnumSet<ItemUseType> allowedItemUseTypes) {
        this.allowedItemUseTypes = allowedItemUseTypes;
        this.numItems = 0;
        this.item = null;
    }

    public boolean addItem(Item item) {
        if (this.item != null) {
            if (this.item.getType() != item.getType()) {
                Gdx.app.debug(TAG, "Trying to add an item to a slot that already has an item!");
                return false;
            } else if (!this.item.isStackable()) {
                Gdx.app.debug(TAG, "Trying to stack a non-stackable item!");
                return false;
            }
        }

        if (!allowsItem(item)) {
            Gdx.app.debug(TAG, "Slot does not support item of type: " + item.getUseType());
            return false;
        }

        // item can be placed in slot
        numItems++;
        if (this.item == null) {
            this.item = item;
        }

        // item successfully added
        return true;
    }

    public void removeItem() {
        if (numItems > 0) {
            // there are still items in the slot
            // -> remove one
            --numItems;
            if (numItems == 0) {
                // no more items available -> remove item from slot
                item = null;
            }
        }
    }

    public void removeAllItems() {
        numItems = 0;
        item = null;
    }

    public boolean swap(InventorySlot slot) {
        final Item otherSlotItem = slot.item;
        if (otherSlotItem != null && !allowsItem(otherSlotItem)) {
            Gdx.app.debug(TAG, "Cannot swap slots because this slot does not support the other slot's item.");
            return false;
        } else if (item != null && !slot.allowsItem(item)) {
            Gdx.app.debug(TAG, "Cannot swap slots because other slot does not support this slot's item.");
            return false;
        }

        // swap is possible because either of the two slots is empty or both of
        // them support each other's items
        final int otherSlotNumItems = slot.numItems;
        slot.item = item;
        slot.numItems = numItems;
        item = otherSlotItem;
        numItems = otherSlotNumItems;

        // successfully swapped items
        return true;
    }

    public boolean allowsItem(Item item) {
        return allowedItemUseTypes.contains(item.getUseType());
    }

    public int getNumItems() {
        return numItems;
    }

    public Item getItem() {
        return item;
    }

    public EnumSet<ItemUseType> getAllowedItemUseTypes() {
        return allowedItemUseTypes;
    }
}
