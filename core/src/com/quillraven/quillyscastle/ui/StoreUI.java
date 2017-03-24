package com.quillraven.quillyscastle.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.quillraven.quillyscastle.inventory.Item.ItemType;
import com.quillraven.quillyscastle.inventory.Item.ItemUseType;
import com.quillraven.quillyscastle.utils.Utils;

import java.util.EnumSet;

public class StoreUI extends Window {
    public static final String STORE_INVENTORY_NAME = "storeInventory";
    private static final String GOLD = "Gold: ";
    private static final String STORE = "Store: ";
    private static final int SLOT_WIDTH = 52;
    private static final int SLOT_HEIGHT = 52;
    private static final int SLOTS_PER_ROW = 10;
    private final InventoryUI playerInventoryUI;
    private final InventorySlotTooltip tooltip;
    private final InventorySlotTooltipListener tooltipListener;
    private final StoreDragAndDrop dragAndDrop;
    private Label goldLabel;
    private Label infoLabel;
    private Table storeTable;

    public StoreUI(Stage stage, InventoryUI playerInventory) {
        super("Store", Utils.UI_SKIN);
        this.getTitleTable().setBackground(Utils.UI_SKIN.getDrawable("default-window-title"));
        this.pad(this.getPadTop() + 10, 0, 0, 0);

        this.playerInventoryUI = playerInventory;
        tooltip = new InventorySlotTooltip();
        tooltip.pack();
        tooltipListener = new InventorySlotTooltipListener(tooltip);

        TextButton closeButton = new TextButton("x", Utils.UI_SKIN);
        add(closeButton).align(Align.topRight).colspan(2).row();

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                StoreUI.this.setVisible(false);
            }
        });

        this.defaults().expand().fill();
        dragAndDrop = new StoreDragAndDrop();
        Label lbl = new Label(STORE, Utils.UI_SKIN);
        lbl.setAlignment(Align.left);
        add(lbl).pad(10, 10, 10, 10).colspan(2).row();

        storeTable = new Table();
        storeTable.setName(STORE_INVENTORY_NAME);

        for (int i = 0; i < 20; ++i) {
            InventorySlotUI slot = createStoreSlot();
            storeTable.add(slot).size(SLOT_WIDTH, SLOT_HEIGHT);

            if (storeTable.getCells().size % SLOTS_PER_ROW == 0) {
                storeTable.row();
            }
        }

        add(storeTable).pad(10, 10, 10, 10).spaceBottom(30).colspan(2);
        row();

        goldLabel = new Label(GOLD, Utils.UI_SKIN);
        goldLabel.setAlignment(Align.left);
        add(goldLabel).pad(10, 10, 10, 10).colspan(2).row();

        infoLabel = new Label("", Utils.UI_SKIN);
        infoLabel.setAlignment(Align.left);
        infoLabel.setColor(255, 0, 0, 255);
        add(infoLabel).pad(10, 10, 10, 10).colspan(2).row();

        this.pack();
        setVisible(false);
        setPosition(stage.getWidth() / 2 - getWidth() / 2, getHeight());
        stage.addActor(this);
        stage.addActor(tooltip);
    }

    @Override
    public void setVisible(boolean visible) {
        // InventoryUI is also used for the StoreUI to display the entities's inventory
        // therefore the equipTable and inventoryTable of the InventoryUI change their parent to either InventoryUI or StoreUI
        //
        // depending on which UI is currently visible we need to add the tables again and update the dragAndDrop
        super.setVisible(visible);

        if (visible) {
            if (!getChildren().contains(playerInventoryUI.getEquipTable(), true)) {
                add(playerInventoryUI.getEquipTable());
                add(playerInventoryUI.getInventoryTable());
                pack();
            }

            dragAndDrop.addItemSlotTable(this, storeTable);
            dragAndDrop.addItemSlotTable(this, playerInventoryUI.getEquipTable());
            dragAndDrop.addItemSlotTable(this, playerInventoryUI.getInventoryTable());
        } else {
            dragAndDrop.clear();
        }
    }

    private InventorySlotUI createStoreSlot() {
        InventorySlotUI slot = new InventorySlotUI(null, EnumSet.allOf(ItemUseType.class));

        slot.addListener(tooltipListener);

        return slot;
    }

    public void updateStoreInventorySlot(int slotIndex, ItemType itemType, String itemGraphicAtlasID, String itemDescription, int numItems,
                                         ItemUseType useType) {
        final InventorySlotUI slot;
        if (storeTable.getChildren().size <= slotIndex) {
            slot = createStoreSlot();
            storeTable.add(slot).size(SLOT_WIDTH, SLOT_HEIGHT);

            if (storeTable.getCells().size % SLOTS_PER_ROW == 0) {
                storeTable.row();
            }
        } else {
            slot = (InventorySlotUI) storeTable.getChildren().get(slotIndex);
        }

        if (itemGraphicAtlasID == null) {
            slot.clearItems();
        } else {
            slot.setItem(itemType, itemGraphicAtlasID, itemDescription, useType);
            slot.setNumItems(numItems);
        }
    }

    public void clearStore() {
        for (Actor slot : storeTable.getChildren()) {
            ((InventorySlotUI) slot).clearItems();
        }

        updateInfoText("");
    }

    public void updateGoldValue(int goldValue) {
        goldLabel.setText(GOLD + goldValue);
        this.pack();
    }

    public void updateInfoText(String text) {
        infoLabel.setText(text);
        this.pack();
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
