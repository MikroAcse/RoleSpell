package com.quillraven.quillyscastle.ui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class InventorySlotTooltipListener extends InputListener {

    private InventorySlotTooltip toolTip;
    private boolean isInside = false;
    private Vector2 currentCoords;
    private Vector2 offset;

    public InventorySlotTooltipListener(InventorySlotTooltip toolTip) {
        this.toolTip = toolTip;
        this.currentCoords = new Vector2(0, 0);
        this.offset = new Vector2(20, 10);
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {
        InventorySlotUI inventorySlot = (InventorySlotUI) event.getListenerActor();
        if (isInside) {
            currentCoords.set(x, y);
            inventorySlot.localToStageCoordinates(currentCoords);

            toolTip.setPosition(currentCoords.x + offset.x, currentCoords.y + offset.y);
        }
        return false;
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        InventorySlotUI inventorySlot = (InventorySlotUI) event.getListenerActor();
        toolTip.setVisible(inventorySlot, false);
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        return true;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        InventorySlotUI inventorySlot = (InventorySlotUI) event.getListenerActor();

        isInside = true;

        currentCoords.set(x, y);
        inventorySlot.localToStageCoordinates(currentCoords);

        toolTip.updateDescription(inventorySlot);
        toolTip.setPosition(currentCoords.x + offset.x, currentCoords.y + offset.y);
        toolTip.toFront();
        toolTip.setVisible(inventorySlot, true);
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        InventorySlotUI inventorySlot = (InventorySlotUI) event.getListenerActor();
        toolTip.setVisible(inventorySlot, false);
        isInside = false;

        currentCoords.set(x, y);
        inventorySlot.localToStageCoordinates(currentCoords);
    }

}
