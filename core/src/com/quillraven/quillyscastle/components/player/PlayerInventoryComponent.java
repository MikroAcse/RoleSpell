package com.quillraven.quillyscastle.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.quillraven.quillyscastle.components.Component;
import com.quillraven.quillyscastle.components.ComponentObserver.ComponentEvent;
import com.quillraven.quillyscastle.components.InventoryComponent;
import com.quillraven.quillyscastle.entity.Entity.EntityType;
import com.quillraven.quillyscastle.entity.EntityConfig;
import com.quillraven.quillyscastle.inventory.InventorySlot;
import com.quillraven.quillyscastle.inventory.Item;
import com.quillraven.quillyscastle.inventory.Item.ItemType;
import com.quillraven.quillyscastle.inventory.Item.ItemUseType;
import com.quillraven.quillyscastle.inventory.ItemLocation;
import com.quillraven.quillyscastle.profile.ProfileManager;
import com.quillraven.quillyscastle.profile.ProfileObserver;

public class PlayerInventoryComponent extends InventoryComponent implements Component, ProfileObserver {
    private static final String TAG = PlayerInventoryComponent.class.getSimpleName();

    private static final String SAVE_KEY_INVENTORY = "inventory";
    private static final String SAVE_KEY_GOLD = "gold";
    private static final String SAVE_KEY_EQUIPMENT = "equipment";

    protected Array<InventorySlot> equipment;
    private int gold;

    public PlayerInventoryComponent() {
        maxInventoryItems = 50;

        equipment = new Array<InventorySlot>();

        equipment.add(new InventorySlot(ItemUseType.ARMOR_HELMET));
        equipment.add(new InventorySlot(ItemUseType.AMULET));
        equipment.add(new InventorySlot(ItemUseType.WEAPON_ONEHAND));
        equipment.add(new InventorySlot(ItemUseType.ARMOR_CHEST));
        equipment.add(new InventorySlot(ItemUseType.ARMOR_SHIELD, ItemUseType.WEAPON_ONEHAND));
        equipment.add(new InventorySlot(ItemUseType.RING));
        equipment.add(new InventorySlot(ItemUseType.ARMOR_BOOTS));
        equipment.add(new InventorySlot(ItemUseType.RING));

        gold = 250;

        ProfileManager.getInstance().addObserver(this);
    }

    @Override
    public void receiveMessage(MessageType type, Object... args) {
        super.receiveMessage(type, args);

        switch (type) {
            case INIT_PLAYER_HUD:
                fireComponentEvent(ComponentEvent.LOAD_INVENTORY, inventory, equipment);
                fireComponentEvent(ComponentEvent.GOLD_UPDATED, gold);
                break;
            case MOVE_ITEM: {
                final InventorySlot sourceSlot = (InventorySlot) args[0];
                final InventorySlot targetSlot = (InventorySlot) args[1];

                if (sourceSlot.getItem().isStackable() && targetSlot.getItem() != null && targetSlot.getItem().getType() == sourceSlot.getItem().getType()) {
                    // stack items of same type if they are stackable
                    for (int i = 0; i < sourceSlot.getNumItems(); ++i) {
                        targetSlot.addItem(sourceSlot.getItem());
                    }

                    sourceSlot.removeAllItems();
                    fireComponentEvent(ComponentEvent.INVENTORY_SLOT_UPDATED, sourceSlot);
                    fireComponentEvent(ComponentEvent.INVENTORY_SLOT_UPDATED, targetSlot);
                } else if (sourceSlot.swap(targetSlot)) {
                    fireComponentEvent(ComponentEvent.INVENTORY_SLOT_UPDATED, sourceSlot);
                    fireComponentEvent(ComponentEvent.INVENTORY_SLOT_UPDATED, targetSlot);
                }

                break;
            }
            case BUY_ITEM: {
                final ItemType itemType = (ItemType) args[0];
                final InventorySlot targetSlot = (InventorySlot) args[1];
                final Item item = Item.getItem(itemType);

                if (item.getGoldValue() > gold) {
                    Gdx.app.debug(TAG, "Not enough gold to buy item: " + type);
                    fireComponentEvent(ComponentEvent.NOT_ENOUGH_GOLD);
                    return;
                }

                final InventorySlot slotToInsert;
                if (targetSlot.allowsItem(item)) {
                    if (targetSlot.getItem() == null || (targetSlot.getItem().getType() == item.getType() && item.isStackable())) {
                        // target slot is empty or contains the same item and item is stackable
                        slotToInsert = targetSlot;
                    } else {
                        slotToInsert = getInventorySlotForItem(item);
                    }
                } else {
                    slotToInsert = getInventorySlotForItem(item);
                }

                if (slotToInsert == null) {
                    Gdx.app.debug(TAG, "There is not enough space in the inventory to buy more items of type: " + type);
                    fireComponentEvent(ComponentEvent.NOT_ENOUGH_INVENTORY_SPACE);
                    return;
                }

                // valid purchase
                gold -= item.getGoldValue();
                fireComponentEvent(ComponentEvent.GOLD_UPDATED, gold);

                slotToInsert.addItem(item);
                fireComponentEvent(ComponentEvent.INVENTORY_SLOT_UPDATED, slotToInsert);

                break;
            }
            case SELL_ITEM: {
                final InventorySlot slotToSell = (InventorySlot) args[0];

                gold += slotToSell.getItem().getSellValue();
                fireComponentEvent(ComponentEvent.GOLD_UPDATED, gold);

                slotToSell.removeItem();
                fireComponentEvent(ComponentEvent.INVENTORY_SLOT_UPDATED, slotToSell);

                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onSave(ProfileManager manager) {
        Array<ItemLocation> itemLocationsInventory = new Array<ItemLocation>();
        for (int i = 0; i < inventory.size; ++i) {
            final InventorySlot slot = inventory.get(i);
            if (slot.getItem() != null) {
                itemLocationsInventory.add(new ItemLocation(i, slot.getItem().getType(), slot.getNumItems()));
            }
        }

        Array<ItemLocation> itemLocationsEquipment = new Array<ItemLocation>();
        for (int i = 0; i < equipment.size; ++i) {
            final InventorySlot slot = equipment.get(i);
            if (slot.getItem() != null) {
                itemLocationsEquipment.add(new ItemLocation(i, slot.getItem().getType(), slot.getNumItems()));
            }
        }

        manager.setProperty(SAVE_KEY_INVENTORY, itemLocationsInventory);
        manager.setProperty(SAVE_KEY_EQUIPMENT, itemLocationsEquipment);
        manager.setProperty(SAVE_KEY_GOLD, gold);
    }

    @Override
    public void onLoad(ProfileManager manager) {
        @SuppressWarnings("unchecked")
        Array<ItemLocation> itemLocationsInventory = manager.getProperty(SAVE_KEY_INVENTORY, Array.class);

        if (itemLocationsInventory == null) {
            itemLocationsInventory = EntityConfig.getEntityConfig(EntityType.PLAYER).getInventory();
        }

        // clear currently loaded inventory
        for (InventorySlot slot : inventory) {
            slot.removeAllItems();
        }

        // clear currently loaded equipment
        for (InventorySlot slot : equipment) {
            slot.removeAllItems();
        }

        // load gold
        gold = manager.getProperty(SAVE_KEY_GOLD, Integer.class) == null ? 250 : manager.getProperty(SAVE_KEY_GOLD, Integer.class);

        if (itemLocationsInventory != null) {
            // load new inventory
            loadItems(itemLocationsInventory);
        }

        @SuppressWarnings("unchecked")
        Array<ItemLocation> itemLocationsEquipment = manager.getProperty(SAVE_KEY_EQUIPMENT, Array.class);
        if (itemLocationsEquipment != null) {
            // load new equipment
            for (ItemLocation itemLoc : itemLocationsEquipment) {
                equipment.get(itemLoc.getLocationIndex()).addItem(Item.getItem(itemLoc.getType()));
            }
        }

        // update UI
        fireComponentEvent(ComponentEvent.LOAD_INVENTORY, inventory, equipment);
        fireComponentEvent(ComponentEvent.GOLD_UPDATED, gold);
    }
}
