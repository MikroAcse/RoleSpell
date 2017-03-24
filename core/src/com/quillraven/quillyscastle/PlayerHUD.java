package com.quillraven.quillyscastle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.quillraven.quillyscastle.components.Component.MessageType;
import com.quillraven.quillyscastle.components.ComponentObserver;
import com.quillraven.quillyscastle.conversation.ConversationGraph;
import com.quillraven.quillyscastle.conversation.ConversationGraphObserver;
import com.quillraven.quillyscastle.entity.Entity;
import com.quillraven.quillyscastle.entity.Entity.EntityType;
import com.quillraven.quillyscastle.inventory.InventorySlot;
import com.quillraven.quillyscastle.inventory.Item;
import com.quillraven.quillyscastle.map.Map;
import com.quillraven.quillyscastle.map.MapManager;
import com.quillraven.quillyscastle.map.MapObserver;
import com.quillraven.quillyscastle.ui.*;

public class PlayerHUD implements MapObserver, ComponentObserver, ConversationGraphObserver, InventorySlotUIObserver {
    private final Entity player;
    private final Stage stage;
    private final OrthographicCamera camera;
    private final StatusUI statusUI;
    private final InventoryUI inventoryUI;
    private final ConversationUI conversationUI;
    private final StoreUI storeUI;
    private Array<InventorySlot> playerInventory;
    private Array<InventorySlot> playerEquipment;

    public PlayerHUD(Entity player) {
        // init camera and stage
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.stage = new Stage(new ScreenViewport(camera));

        // add for basic notifications
        player.addObserver(this);
        MapManager.getInstance().addObserver(this);

        // create UI elements
        inventoryUI = new InventoryUI(stage);
        inventoryUI.addObserver(this);

        storeUI = new StoreUI(stage, inventoryUI);
        storeUI.addObserver(this);

        statusUI = new StatusUI(stage);
        statusUI.getInventoryButton().addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                inventoryUI.setVisible(inventoryUI.isVisible() ? false : true);
            }
        });

        conversationUI = new ConversationUI(stage);

        // init entities hud with entities entity values
        this.player = player;
        playerInventory = playerEquipment = null;
        this.player.sendMessage(MessageType.INIT_PLAYER_HUD);
    }

    public Stage getStage() {
        return stage;
    }

    public void update(float delta) {
        stage.act(delta);
    }

    public void render() {
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        statusUI.setPosition(width / 2 - statusUI.getWidth() / 2, 0);
        inventoryUI.setPosition(width / 2 - inventoryUI.getWidth() / 2, statusUI.getHeight());
        conversationUI.setPosition(width / 2 - conversationUI.getWidth() / 2, statusUI.getHeight());
        storeUI.setPosition(width / 2 - storeUI.getWidth() / 2, statusUI.getHeight());
    }

    public void dispose() {
        stage.dispose();
    }

    private InventorySlot getPlayerInventorySlot(InventorySlotUI forUISlot) {
        if (inventoryUI.getInventoryTable().equals(forUISlot.getParent())) {
            return playerInventory.get(inventoryUI.getInventoryTable().getChildren().indexOf(forUISlot, true));
        } else {
            return playerEquipment.get(inventoryUI.getEquipTable().getChildren().indexOf(forUISlot, true));
        }
    }

    private void loadPlayerInventoryUI() {
        inventoryUI.clearInventoryAndEquipment();

        for (int slotIndex = 0; slotIndex < playerInventory.size; ++slotIndex) {
            final InventorySlot slot = playerInventory.get(slotIndex);
            final Item item = slot.getItem();

            inventoryUI.updateInventorySlot( // params
                    slotIndex, // slot index
                    slot.getAllowedItemUseTypes(), // slot item use type
                    item == null ? null : item.getType(), // item type
                    item == null ? null : item.getAtlasID(), // item graphic
                    item == null ? null : item.getDescription(), // item description
                    slot.getNumItems(), // number of items
                    item == null ? null : item.getUseType() // item use type
            );
        }

        for (int slotIndex = 0; slotIndex < playerEquipment.size; ++slotIndex) {
            final InventorySlot slot = playerEquipment.get(slotIndex);
            final Item item = slot.getItem();

            inventoryUI.updateEquipmentSlot( // params
                    slotIndex, // slot index
                    slot.getAllowedItemUseTypes(), // slot item use type
                    item == null ? null : item.getType(), // item type
                    item == null ? null : item.getAtlasID(), // item graphic
                    item == null ? null : item.getDescription(), // item description
                    slot.getNumItems(), // number of items
                    item == null ? null : item.getUseType() // item use type
            );
        }

        inventoryUI.pack();
    }

    private void updateStoreUI(Array<InventorySlot> storeInventory) {
        storeUI.clearStore();

        for (InventorySlot slot : storeInventory) {
            final Item item = slot.getItem();

            if (item != null) {
                storeUI.updateStoreInventorySlot(storeInventory.indexOf(slot, true), item.getType(), item.getAtlasID(), item.getDescription(),
                        slot.getNumItems(), item.getUseType());
            }
        }

        storeUI.pack();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onComponentEvent(ComponentEvent event, Object... args) {
        switch (event) {
            case LOAD_CONVERSATION:
                final String convFilePath = (String) args[0];
                final EntityType convTitle = (EntityType) args[1];

                if (conversationUI.getGraph() != null) {
                    conversationUI.getGraph().removeObserver(this);
                }
                conversationUI.loadConversation(convFilePath, convTitle.name());
                conversationUI.getGraph().addObserver(this);
                conversationUI.setVisible(true);
                break;
            case LOAD_INVENTORY:
                if (args.length > 1) {
                    playerInventory = (Array<InventorySlot>) args[0];
                    playerEquipment = (Array<InventorySlot>) args[1];

                    loadPlayerInventoryUI();
                } else {
                    updateStoreUI((Array<InventorySlot>) args[0]);
                }

                break;
            case INVENTORY_SLOT_UPDATED:
                final InventorySlot slot = (InventorySlot) args[0];
                final Item item = slot.getItem();

                if (playerEquipment.contains(slot, true)) {
                    inventoryUI.updateEquipmentSlot( // params
                            playerEquipment.indexOf(slot, true), // slot index
                            slot.getAllowedItemUseTypes(), // slot item use type
                            item == null ? null : item.getType(), // item type
                            item == null ? null : item.getAtlasID(), // item graphic
                            item == null ? null : item.getDescription(), // item description
                            slot.getNumItems(), // number of items
                            item == null ? null : item.getUseType() // item use type
                    );
                } else {
                    inventoryUI.updateInventorySlot( // params
                            playerInventory.indexOf(slot, true), // slot index
                            slot.getAllowedItemUseTypes(), // slot item use type
                            item == null ? null : item.getType(), // item type
                            item == null ? null : item.getAtlasID(), // item graphic
                            item == null ? null : item.getDescription(), // item description
                            slot.getNumItems(), // number of items
                            item == null ? null : item.getUseType() // item use type
                    );
                }

                inventoryUI.pack();
                break;
            case GOLD_UPDATED:
                statusUI.updateGoldValue((Integer) args[0]);
                storeUI.updateGoldValue((Integer) args[0]);
                break;
            case NOT_ENOUGH_GOLD:
                storeUI.updateInfoText("Not enough gold to purchase this item!");
                break;
            case NOT_ENOUGH_INVENTORY_SPACE:
                storeUI.updateInfoText("Not enough space in your inventory!");
                break;
            default:
                break;
        }
    }

    @Override
    public void onConversationEvent(ConversationCommandEvent event, ConversationGraph graph) {
        switch (event) {
            case EXIT_CONVERSATION:
                conversationUI.setVisible(false);
                break;
            case LOAD_STORE_INVENTORY:
                MapManager.getInstance().getSelectedEntity().sendMessage(MessageType.LOAD_INVENTORY);
                storeUI.setVisible(true);
                storeUI.toFront();
            default:
                break;
        }
    }

    @Override
    public void onMapChanged(Map currentMap, Map newMap) {
        if (currentMap != null) {
            for (Entity entity : currentMap.getEntities()) {
                entity.removeObserver(this);
            }
        }

        for (Entity entity : newMap.getEntities()) {
            entity.addObserver(this);
        }
    }

    @Override
    public void onItemMoved(InventorySlotUI sourceSlot, InventorySlotUI targetSlot) {
        player.sendMessage(MessageType.MOVE_ITEM, getPlayerInventorySlot(sourceSlot), getPlayerInventorySlot(targetSlot));
    }

    @Override
    public void onItemSold(InventorySlotUI sourceSlot, InventorySlotUI targetSlot) {
        player.sendMessage(MessageType.SELL_ITEM, getPlayerInventorySlot(sourceSlot));
    }

    @Override
    public void onItemPurchased(InventorySlotUI sourceSlot, InventorySlotUI targetSlot) {
        player.sendMessage(MessageType.BUY_ITEM, sourceSlot.getItemType(), getPlayerInventorySlot(targetSlot));
    }

}
