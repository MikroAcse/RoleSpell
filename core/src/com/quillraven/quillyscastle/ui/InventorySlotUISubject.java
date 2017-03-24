package com.quillraven.quillyscastle.ui;

import com.badlogic.gdx.utils.Array;

public abstract class InventorySlotUISubject {
    private final Array<InventorySlotUIObserver> observers;

    protected InventorySlotUISubject() {
        observers = new Array<InventorySlotUIObserver>();
    }

    public void addObserver(InventorySlotUIObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(InventorySlotUIObserver observer) {
        observers.removeValue(observer, true);
    }

    public void removeAllObservers() {
        observers.clear();
    }

    protected void fireItemMoved(InventorySlotUI sourceSlot, InventorySlotUI targetSlot) {
        for (InventorySlotUIObserver observer : observers) {
            observer.onItemMoved(sourceSlot, targetSlot);
        }
    }

    protected void fireItemSold(InventorySlotUI sourceSlot, InventorySlotUI targetSlot) {
        for (InventorySlotUIObserver observer : observers) {
            observer.onItemSold(sourceSlot, targetSlot);
        }
    }

    protected void fireItemPurchased(InventorySlotUI sourceSlot, InventorySlotUI targetSlot) {
        for (InventorySlotUIObserver observer : observers) {
            observer.onItemPurchased(sourceSlot, targetSlot);
        }
    }
}
