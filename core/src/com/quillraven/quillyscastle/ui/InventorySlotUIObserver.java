package com.quillraven.quillyscastle.ui;

public interface InventorySlotUIObserver {
    public void onItemMoved(InventorySlotUI sourceSlot, InventorySlotUI targetSlot);

    public void onItemSold(InventorySlotUI sourceSlot, InventorySlotUI targetSlot);

    public void onItemPurchased(InventorySlotUI sourceSlot, InventorySlotUI targetSlot);
}
