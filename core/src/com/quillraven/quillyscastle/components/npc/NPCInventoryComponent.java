package com.quillraven.quillyscastle.components.npc;

import com.quillraven.quillyscastle.components.Component;
import com.quillraven.quillyscastle.components.ComponentObserver.ComponentEvent;
import com.quillraven.quillyscastle.components.InventoryComponent;

public class NPCInventoryComponent extends InventoryComponent implements Component {
    public NPCInventoryComponent() {
        maxInventoryItems = 20;
    }

    @Override
    public void receiveMessage(MessageType type, Object... args) {
        super.receiveMessage(type, args);

        switch (type) {
            case LOAD_INVENTORY:
                // the next line notifies the PlayerHUD to update the StoreUI
                // with the inventory of this entity
                fireComponentEvent(ComponentEvent.LOAD_INVENTORY, inventory);

                break;
            default:
                break;
        }
    }
}
