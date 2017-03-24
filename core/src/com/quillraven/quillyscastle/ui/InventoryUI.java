package com.quillraven.quillyscastle.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.quillraven.quillyscastle.inventory.Item.ItemType;
import com.quillraven.quillyscastle.inventory.Item.ItemUseType;
import com.quillraven.quillyscastle.utils.Utils;

import java.util.EnumSet;

public class InventoryUI extends Window {
    public static final String PLAYER_INVENTORY = "playerInventory";
    private static final int SLOT_WIDTH = 52;
    private static final int SLOT_HEIGHT = 52;
    private static final int SLOTS_PER_ROW = 10;
    private final InventorySlotTooltip tooltip;
    private final InventorySlotTooltipListener tooltipListener;
    private final InventoryDragAndDrop dragAndDrop;
    private Table inventoryTable;
    private Table equipTable;

    public InventoryUI(Stage stage) {
        super("Inventory", Utils.UI_SKIN);
        this.getTitleTable().setBackground(Utils.UI_SKIN.getDrawable("default-window-title"));
        this.pad(this.getPadTop() + 10, 0, 0, 0);

        tooltip = new InventorySlotTooltip();
        tooltip.pack();
        tooltipListener = new InventorySlotTooltipListener(tooltip);
        dragAndDrop = new InventoryDragAndDrop();

        this.defaults().expand().fill();

        equipTable = new Table();
        add(equipTable).pad(10, 10, 10, 10).center();
        // we use the name of the table to identify if an item is sold/bought within the StoreUI logic
        equipTable.setName(PLAYER_INVENTORY);

        inventoryTable = new Table();
        add(inventoryTable).spaceTop(30).pad(10, 10, 10, 10);
        // we use the name of the table to identify if an item is sold/bought within the StoreUI logic
        inventoryTable.setName(PLAYER_INVENTORY);

        this.pack();

        setVisible(false);
        setPosition(stage.getWidth() / 2 - getWidth() / 2, getHeight());
        stage.addActor(this);
        stage.addActor(tooltip);
    }

    @Override
    public void setVisible(boolean visible) {
        // InventoryUI is also used for the StoreUI to display the entities's inventory
        // therefore the equipTable and inventoryTable change their parent to either InventoryUI or StoreUI
        //
        // depending on which UI is currently visible we need to add the tables again and update the dragAndDrop
        super.setVisible(visible);

        if (visible) {
            if (!getChildren().contains(equipTable, true)) {
                add(equipTable);
                add(inventoryTable);
                pack();
            }

            dragAndDrop.addItemSlotTable(equipTable);
            dragAndDrop.addItemSlotTable(inventoryTable);
        } else {
            dragAndDrop.clear();
        }
    }

    private InventorySlotUI createSlot(EnumSet<ItemUseType> allowedItemUseTypes) {
        final InventorySlotUI slot;
        if (allowedItemUseTypes.contains(ItemUseType.NONE)) {
            slot = new InventorySlotUI(null, allowedItemUseTypes);
        } else if (allowedItemUseTypes.contains(ItemUseType.ARMOR_HELMET)) {
            slot = new InventorySlotUI("inventory-slot-helmet", allowedItemUseTypes);
        } else if (allowedItemUseTypes.contains(ItemUseType.AMULET)) {
            slot = new InventorySlotUI("inventory-slot-amulet", allowedItemUseTypes);
        } else if (allowedItemUseTypes.contains(ItemUseType.ARMOR_SHIELD)) {
            slot = new InventorySlotUI("inventory-slot-shield", allowedItemUseTypes);
        } else if (allowedItemUseTypes.contains(ItemUseType.WEAPON_ONEHAND)) {
            slot = new InventorySlotUI("inventory-slot-weapon", allowedItemUseTypes);
        } else if (allowedItemUseTypes.contains(ItemUseType.ARMOR_CHEST)) {
            slot = new InventorySlotUI("inventory-slot-armor", allowedItemUseTypes);
        } else if (allowedItemUseTypes.contains(ItemUseType.RING)) {
            slot = new InventorySlotUI("inventory-slot-ring", allowedItemUseTypes);
        } else {
            slot = new InventorySlotUI("inventory-slot-boots", allowedItemUseTypes);
        }

        slot.addListener(tooltipListener);

        return slot;
    }

    private void updateSlot(Table whichTable, int slotIndex, ItemType itemType, String itemGraphicAtlasID, String itemDescription, int numItems,
                            ItemUseType useType) {
        final InventorySlotUI slot = ((InventorySlotUI) whichTable.getChildren().get(slotIndex));

        if (itemGraphicAtlasID == null) {
            slot.clearItems();
        } else {
            slot.setItem(itemType, itemGraphicAtlasID, itemDescription, useType);
            slot.setNumItems(numItems);
        }
    }

    public void clearInventoryAndEquipment() {
        for (Actor slot : inventoryTable.getChildren()) {
            ((InventorySlotUI) slot).clearItems();
        }
        for (Actor slot : equipTable.getChildren()) {
            ((InventorySlotUI) slot).clearItems();
        }
    }

    public void updateInventorySlot(int slotIndex, EnumSet<ItemUseType> allowedItemUseTypes, ItemType itemType, String itemGraphicAtlasID,
                                    String itemDescription, int numItems, ItemUseType useType) {
        final InventorySlotUI slot;
        if (inventoryTable.getChildren().size <= slotIndex) {
            slot = createSlot(allowedItemUseTypes);
            inventoryTable.add(slot).size(SLOT_WIDTH, SLOT_HEIGHT);

            if (inventoryTable.getCells().size % SLOTS_PER_ROW == 0) {
                inventoryTable.row();
            }
        } else {
            slot = (InventorySlotUI) inventoryTable.getChildren().get(slotIndex);
        }

        updateSlot(inventoryTable, slotIndex, itemType, itemGraphicAtlasID, itemDescription, numItems, useType);
    }

    public void updateEquipmentSlot(int slotIndex, EnumSet<ItemUseType> allowedItemUseTypes, ItemType itemType, String itemGraphicAtlasID,
                                    String itemDescription, int numItems, ItemUseType useType) {
        if (equipTable.getChildren().size == 0) {
            equipTable.add();
        }

        final InventorySlotUI slot;
        if (equipTable.getChildren().size <= slotIndex) {
            slot = createSlot(allowedItemUseTypes);
            equipTable.add(slot).size(SLOT_WIDTH, SLOT_HEIGHT);

            if (equipTable.getCells().size % 3 == 0) {
                equipTable.row();
            }
        } else {
            slot = (InventorySlotUI) equipTable.getChildren().get(slotIndex);
        }

        updateSlot(equipTable, slotIndex, itemType, itemGraphicAtlasID, itemDescription, numItems, useType);
    }

    public Table getEquipTable() {
        return equipTable;
    }

    public Table getInventoryTable() {
        return inventoryTable;
    }

    public void addObserver(InventorySlotUIObserver observer) {
        dragAndDrop.addObserver(observer);
    }

    public void removeObserver(InventorySlotUIObserver observer) {
        dragAndDrop.removeObserver(observer);
    }

    public void removeAllObservers() {
        dragAndDrop.removeAllObservers();
    }
}
