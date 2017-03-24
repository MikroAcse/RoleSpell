package com.quillraven.quillyscastle.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.utils.Scaling;

public class InventoryDragAndDrop extends InventorySlotUISubject {
    private final DragAndDrop dragAndDrop;

    public InventoryDragAndDrop() {
        dragAndDrop = new DragAndDrop();
    }

    public void addItemSlotTable(Table itemSlotTable) {
        dragAndDrop.addSource(new Source(itemSlotTable) {
            private final Payload payload = new Payload();
            private Image dragImage = null;
            private Image invalidImage = null;

            @Override
            public Payload dragStart(InputEvent event, float x, float y, int pointer) {
                final Actor itemSlotTable = getActor();
                final Actor itemGraphic = itemSlotTable.hit(x, y, true);

                payload.setObject(null);
                payload.setDragActor(null);
                payload.setInvalidDragActor(null);

                if (itemGraphic != null) {
                    final InventorySlotUI slot = (InventorySlotUI) itemGraphic.getParent();

                    if (slot != null && slot.getNumItems() > 0) {
                        slot.setDragged(true);

                        if (dragImage == null) {
                            dragImage = new Image(slot.getItemGraphic().getDrawable());
                            dragImage.setSize(slot.getWidth(), slot.getHeight());
                            dragImage.setScaling(Scaling.fit);

                            invalidImage = new Image(slot.getItemGraphic().getDrawable());
                            invalidImage.setSize(slot.getWidth(), slot.getHeight());
                            invalidImage.setScaling(Scaling.fit);
                            invalidImage.setColor(255, 0, 0, 255);

                            dragAndDrop.setDragActorPosition(-slot.getWidth() * 0.5f, slot.getHeight() * 0.5f);
                        } else {
                            dragImage.setDrawable(slot.getItemGraphic().getDrawable());
                            invalidImage.setDrawable(slot.getItemGraphic().getDrawable());
                        }

                        payload.setObject(slot);
                        // show the item that we drag
                        payload.setDragActor(dragImage);
                        payload.setInvalidDragActor(invalidImage);
                    }
                }

                return payload;
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target) {
                final InventorySlotUI slot = (InventorySlotUI) payload.getObject();

                if (slot != null) {
                    slot.setDragged(false);
                }
            }
        });

        dragAndDrop.addTarget(new Target(itemSlotTable) {
            @Override
            public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
                final InventorySlotUI sourceSlot = (InventorySlotUI) payload.getObject();

                if (sourceSlot == null) {
                    // empty slot was dragged
                    return false;
                }

                // can either be item graphic or slot background
                final Actor slotGraphic = getActor().hit(x, y, true);

                // drag is only allowed if we drag inside an inventory slot
                if (slotGraphic != null) {
                    final InventorySlotUI targetSlot = (InventorySlotUI) slotGraphic.getParent();

                    return targetSlot.allowsItem(sourceSlot.getCurrentUseType());
                }

                return false;
            }

            @Override
            public void drop(Source source, Payload payload, float x, float y, int pointer) {
                // drop to a valid inventoryslot is happening
                InventorySlotUI targetSlot = (InventorySlotUI) getActor().hit(x, y, true).getParent();
                InventorySlotUI sourceSlot = (InventorySlotUI) payload.getObject();

                fireItemMoved(sourceSlot, targetSlot);
            }
        });
    }

    public void clear() {
        dragAndDrop.clear();
    }
}
