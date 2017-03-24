package com.quillraven.quillyscastle.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.quillraven.quillyscastle.utils.Utils;

public class InventorySlotTooltip extends Window {
    private final Label description;

    public InventorySlotTooltip() {
        super("", Utils.UI_SKIN);

        description = new Label("", Utils.UI_SKIN, "tooltip");

        this.add(description).fill();
        this.padLeft(5).padRight(5);
        this.pack();
        this.setVisible(false);

    }

    public void setVisible(InventorySlotUI inventorySlot, boolean visible) {
        super.setVisible(visible);

        if (inventorySlot == null) {
            return;
        }

        if (inventorySlot.getDescription() == null) {
            super.setVisible(false);
        }
    }

    public void updateDescription(InventorySlotUI inventorySlot) {
        if (inventorySlot.getDescription() != null) {
            description.setText(inventorySlot.getDescription());
            this.pack();
        } else {
            description.setText("");
            this.pack();
        }
    }
}
