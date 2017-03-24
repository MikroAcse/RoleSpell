package com.quillraven.quillyscastle.ui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.quillraven.quillyscastle.inventory.Item.ItemType;
import com.quillraven.quillyscastle.inventory.Item.ItemUseType;
import com.quillraven.quillyscastle.utils.Utils;

import java.util.EnumSet;

public class InventorySlotUI extends Stack {
    private final Image background;
    private final Label numItemsLabel;
    private ItemType itemType;
    private String description;
    private String atlasID;
    private int numItems;
    private EnumSet<ItemUseType> allowedItemUseTypes;
    private ItemUseType currentUseType;
    private Image itemGraphic;

    public InventorySlotUI(String backgroundAtlasID, EnumSet<ItemUseType> allowedItemUseTypes) {
        itemGraphic = null;
        description = null;

        if (backgroundAtlasID == null) {
            background = new Image(Utils.UI_SKIN.getDrawable("default-window"));
        } else {
            background = new Image(Utils.ITEMS_TEXTURE_ATLAS.findRegion(backgroundAtlasID));
        }
        background.setScaling(Scaling.fit);

        numItemsLabel = new Label("", Utils.UI_SKIN);
        numItemsLabel.setAlignment(Align.bottomRight);
        numItemsLabel.setVisible(false);
        // disable touch support for the label because otherwise the drag & drop support will not work for
        // stackable items due to the label being in front of the inventoryitems (touch event will not be passed
        // correctly to the inventory item when trying to drag it)
        //
        // background image needs to be touchable in order for the drag and drop support to detect empty inventory slots
        // otherwise empty slots are not detected by drag and drop and items cannot be moved to empty slots
        numItemsLabel.setTouchable(Touchable.disabled);

        add(background);
        add(numItemsLabel);

        this.allowedItemUseTypes = allowedItemUseTypes;
        this.currentUseType = ItemUseType.NONE;
    }

    public void setItem(ItemType itemType, String graphicAtlasID, String description, ItemUseType currentUseType) {
        this.itemType = itemType;
        this.atlasID = graphicAtlasID;
        this.description = description;
        this.currentUseType = currentUseType;

        if (itemGraphic == null) {
            itemGraphic = new Image(Utils.ITEMS_TEXTURE_ATLAS.findRegion(graphicAtlasID));
            itemGraphic.setScaling(Scaling.fit);
            addActorAt(1, itemGraphic);
        } else {
            ((TextureRegionDrawable) itemGraphic.getDrawable()).setRegion(Utils.ITEMS_TEXTURE_ATLAS.findRegion(graphicAtlasID));
        }

        itemGraphic.setVisible(true);
        setNumItems(1);
    }

    public void clearItems() {
        if (itemGraphic != null) {
            itemGraphic.setVisible(false);
        }
        setNumItems(0);
        this.description = null;
        this.currentUseType = ItemUseType.NONE;
    }

    public String getDescription() {
        return description;
    }

    public Image getItemGraphic() {
        return itemGraphic;
    }

    public int getNumItems() {
        return numItems;
    }

    public void setNumItems(int numItems) {
        this.numItems = numItems;
        if (numItems <= 1) {
            numItemsLabel.setVisible(false);
        } else {
            numItemsLabel.getText().setLength(0);
            numItemsLabel.getText().append(numItems);
            numItemsLabel.invalidateHierarchy();
            numItemsLabel.setVisible(true);
        }
    }

    public String getAtlasID() {
        return atlasID;
    }

    public boolean allowsItem(ItemUseType useType) {
        return allowedItemUseTypes.contains(useType);
    }

    public ItemUseType getCurrentUseType() {
        return currentUseType;
    }

    public void setDragged(boolean dragged) {
        // if slot is dragged -> hide graphic and numItemsLabel
        // if slot is not dragged -> hide graphic if there is no item in the slot and hide numItemsLabel if numItems <= 1
        itemGraphic.setVisible(!dragged ? numItems >= 1 : false);
        numItemsLabel.setVisible(!dragged ? numItems > 1 : false);
    }

    public ItemType getItemType() {
        return itemType;
    }
}
