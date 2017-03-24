package com.quillraven.quillyscastle.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.quillraven.quillyscastle.GameWorld;
import com.quillraven.quillyscastle.entity.Entity;
import com.quillraven.quillyscastle.entity.EntityConfig;
import com.quillraven.quillyscastle.inventory.InventorySlot;
import com.quillraven.quillyscastle.inventory.Item;
import com.quillraven.quillyscastle.inventory.Item.ItemType;
import com.quillraven.quillyscastle.inventory.ItemLocation;
import com.quillraven.quillyscastle.map.Map;

public abstract class InventoryComponent extends ComponentSubject implements Component {
    private static final String TAG = InventoryComponent.class.getSimpleName();

    protected Array<InventorySlot> inventory;
    protected int maxInventoryItems;

    public InventoryComponent() {
        inventory = null;
        maxInventoryItems = 0;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void receiveMessage(MessageType type, Object... args) {
        switch (type) {
            case LOAD_INVENTORY:
                if (args.length > 0) {
                    final EntityConfig config = (EntityConfig) args[0];

                    loadItems(config.getInventory());
                }

                break;
            default:
                break;
        }
    }

    protected void loadItems(Array<ItemLocation> itemLocations) {
        if (inventory == null) {
            // init inventory
            if (maxInventoryItems == 0) {
                maxInventoryItems = itemLocations.size;
            }

            inventory = new Array<InventorySlot>(maxInventoryItems);
            for (int i = 0; i < maxInventoryItems; ++i) {
                inventory.add(new InventorySlot());
            }
        }

        if (itemLocations != null) {
            // add items to inventory
            for (ItemLocation itemLocation : itemLocations) {
                final ItemType itemType = itemLocation.getType();
                Item item = Item.getItem(itemType);
                InventorySlot slot = null;

                if (itemLocation.getLocationIndex() >= 0) {
                    slot = inventory.get(itemLocation.getLocationIndex());
                } else {
                    slot = getInventorySlotForItem(item);
                }

                for (int i = 0; i < itemLocation.getNumItems(); ++i) {
                    slot.addItem(item);

                    if (!item.isStackable() && i < itemLocation.getNumItems() - 1) {
                        // there are still items to be added to the inventory but the item
                        // is not stackable -> get next slot to add the next item and
                        // create a new copy of this item's type
                        slot = getInventorySlotForItem(item);
                        item = Item.getItem(itemType);
                    }
                }
            }
        }
    }

    protected InventorySlot getInventorySlotForItem(Item item) {
        InventorySlot result = null;
        if (item.isStackable()) {
            // check if there is a slot that already contains this item
            result = getInventorySlotOfItem(item);
        }

        if (result == null) {
            // there was no slot with this item or item is not stackable
            // -> get next empty slot that supports this item
            result = getEmptyInventorySlotForItem(item);
        }

        if (result == null) {
            Gdx.app.error(TAG, "Could not find a slot to store item: " + item);
        }

        return result;
    }

    protected InventorySlot getInventorySlotOfItem(Item item) {
        for (InventorySlot slot : inventory) {
            if (slot.getItem() != null && slot.getItem().getType() == item.getType()) {
                return slot;
            }
        }

        return null;
    }

    protected InventorySlot getEmptyInventorySlotForItem(Item item) {
        for (InventorySlot slot : inventory) {
            if (slot.getNumItems() == 0 && slot.allowsItem(item)) {
                return slot;
            }
        }

        return null;
    }

    @Override
    public void update(Entity entity, GameWorld world, Map map, Camera camera, float deltaTime) {
    }
}
